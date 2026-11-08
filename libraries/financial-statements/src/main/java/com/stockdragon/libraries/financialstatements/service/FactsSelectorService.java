package com.stockdragon.libraries.financialstatements.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.stockdragon.libraries.financialstatements.model.dto.AnnualFact;
import com.stockdragon.libraries.financialstatements.model.type.CanonicalItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class FactsSelectorService {

    private static final Set<String> USGAAP_FORMS = Set.of("10-K", "10-K/A");
    private static final Set<String> IFRS_FORMS = Set.of("20-F", "40-F");
    private static final long MIN_DAYS = 335;
    private static final long MAX_DAYS = 400;
    private static final String IFRS_TAXONOMY = "ifrs-full";

    public enum RestatementPolicy { LATEST_RESTATED, AS_ORIGINALLY_REPORTED }

    public List<AnnualFact> select(
            JsonNode facts,
            String taxonomy,
            CanonicalItem item,
            List<String> priorityTags,
            List<String> componentsTags,
            RestatementPolicy policy) {

        for (String tag : priorityTags) {
            List<AnnualFact> result = selectFromTag(facts, taxonomy, tag, item, policy);
            if (!result.isEmpty()) return result;
        }
        if (item == CanonicalItem.TOTAL_LIABILITIES && componentsTags.size() == 2) {
            return composeComponents(facts, taxonomy, item, componentsTags, policy);
        }
        return List.of();
    }

    private AnnualFact sum(AnnualFact current, AnnualFact noncurrent, List<String> tags) {
        return new AnnualFact(
                current.item(),
                current.fiscalYear(),
                current.periodEnd(),
                current.value().add(noncurrent.value()),
                current.currency(),
                tags.getFirst() + "+" + tags.get(1),
                current.form(),
                current.filed()
        );
    }

    private List<AnnualFact> selectFromTag(
            JsonNode facts,
            String taxonomy,
            String tag,
            CanonicalItem item,
            RestatementPolicy policy) {
        JsonNode unitsByTag = facts.path(taxonomy).path(tag).path("units");
        if (unitsByTag.isMissingNode()) return List.of();
        Set<String> allowedForms = allowedForms(taxonomy);
        String currency = dominantCurrency(unitsByTag, allowedForms);
        if (currency == null) return List.of();

        Map<LocalDate, AnnualFact> byEnd = new TreeMap<>();
        unitsByTag.path(currency).forEach(fact ->
                processFactNode(fact, tag, item, currency, allowedForms, policy, byEnd));
        return new ArrayList<>(byEnd.values());
    }

    private void processFactNode(
           JsonNode fact,
           String tag,
           CanonicalItem item,
           String currency,
           Set<String> allowedForms,
           RestatementPolicy policy,
           Map<LocalDate, AnnualFact> accumulator) {
        String form = fact.path("form").asText();
        if (!allowedForms.contains(form)) return;
        LocalDate end = parseDate(fact.path("end").asText(null));
        LocalDate filled = parseDate(fact.path("filed").asText(null));
        if (end == null || filled == null) return;
        if (!isAnnualPeriod(fact)) return;
        AnnualFact candidate = new AnnualFact(
                item, end.getYear(), end,
                new BigDecimal(fact.path("val").asText()),
                currency, tag, form, filled
        );
        accumulator.merge(end, candidate, (existing, incoming) ->
                preferredByPolicy(existing, incoming, policy));
    }

    private List<AnnualFact> composeComponents(
            JsonNode facts,
            String taxonomy,
            CanonicalItem item,
            List<String> componentTags,
            RestatementPolicy policy) {
        List<AnnualFact> current = selectFromTag(facts, taxonomy, componentTags.get(0), item, policy);
        List<AnnualFact> nocurrent = selectFromTag(facts, taxonomy, componentTags.get(1), item, policy);
        Map<LocalDate, AnnualFact> nocurrentByEnd = indexByEnd(nocurrent);
        return current.stream()
                .filter(c -> nocurrentByEnd.containsKey(c.periodEnd()))
                .map(c -> sum(c, nocurrentByEnd.get(c.periodEnd()), componentTags))
                .toList();
    }

    private boolean isAnnualPeriod(JsonNode fact) {
        String startText = fact.path("start").asText(null);
        if (startText == null) return true;
        LocalDate start = parseDate(startText);
        LocalDate end = parseDate(fact.path("end").asText(null));
        if (start == null || end == null) return false;
        long days = ChronoUnit.DAYS.between(start, end);
        return days >= MIN_DAYS && days <= MAX_DAYS;
    }

    private AnnualFact preferredByPolicy(AnnualFact a, AnnualFact b, RestatementPolicy policy) {
        return switch (policy) {
            case LATEST_RESTATED -> b.filed().isAfter(a.filed()) ? b : a;
            case AS_ORIGINALLY_REPORTED -> b.filed().isBefore(a.filed()) ? b : a;
        };
    }

    private String dominantCurrency(JsonNode unitsByTag, Set<String> allowedForms) {
        String best = null;
        long bestCount = -1;
        for (Iterator<Map.Entry<String, JsonNode>> it = unitsByTag.fields(); it.hasNext();) {
            Map.Entry<String, JsonNode> entry = it.next();
            long count = countAnnualForms(entry.getValue(), allowedForms);
            if (count > bestCount) { bestCount = count; best = entry.getKey(); }
        }
        return bestCount > 0 ? best : null;
    }

    private long countAnnualForms(JsonNode facts, Set<String> allowedForms) {
        int count = 0;
        for (JsonNode f : facts) {
            if (allowedForms.contains(f.path("form").asText())) count++;
        }
        return count;
    }

    private Map<LocalDate, AnnualFact> indexByEnd(List<AnnualFact> facts) {
        Map<LocalDate, AnnualFact> map = new HashMap<>();
        facts.forEach(f -> map.put(f.periodEnd(), f));
        return map;
    }

    private Set<String> allowedForms(String taxonomy) {
        return taxonomy.equals(IFRS_TAXONOMY) ? IFRS_FORMS : USGAAP_FORMS;
    }

    private LocalDate parseDate(String text) {
        if (text == null || text.isBlank()) return null;
        try { return LocalDate.parse(text); }
        catch (Exception e) { return null; }
    }
}
