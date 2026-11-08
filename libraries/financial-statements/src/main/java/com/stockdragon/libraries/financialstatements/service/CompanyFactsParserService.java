package com.stockdragon.libraries.financialstatements.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockdragon.libraries.financialstatements.model.dto.AnnualFact;
import com.stockdragon.libraries.financialstatements.model.type.CanonicalItem;
import com.stockdragon.libraries.financialstatements.registry.TagMappingRegistry;
import com.stockdragon.libraries.financialstatements.repository.RawPayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyFactsParserService {

    private static final List<String> TAXONOMY_PRIORITY = List.of("us-gaap", "ifrs-full");

    private final RawPayloadRepository rawPayloadRepository;
    private final TagMappingRegistry registry;
    private final FactsSelectorService selectorService;
    private final ObjectMapper objectMapper;

    public Map<CanonicalItem, List<AnnualFact>> parse(long cik, FactsSelectorService.RestatementPolicy policy) {
        return rawPayloadRepository
                .findLatest("EDGAR", String.valueOf(cik), "companyfacts")
                .map(raw -> parseJson(raw.getPayload(), policy))
                .orElseGet(() -> {
                    log.warn("No companyfacts payload for CIK {}", cik);
                    return Map.of();
                });
    }

    public Map<CanonicalItem, List<AnnualFact>> parseJson(String json, FactsSelectorService.RestatementPolicy policy) {
        try {
            JsonNode facts = objectMapper.readTree(json).path("facts");
            String taxonomy = detectTaxonomy(facts);
            if (taxonomy == null) {
                log.warn("No recognized taxonomy in facts");
                return Map.of();
            }
            return parseAllItems(facts, taxonomy, policy);
        } catch (Exception e) {
            log.error("Failed to parse companyfacts JSON", e);
            return Map.of();
        }
    }

    private Map<CanonicalItem, List<AnnualFact>> parseAllItems(
            JsonNode facts, String taxonomy, FactsSelectorService.RestatementPolicy policy) {
        Map<CanonicalItem, List<AnnualFact>> result = new EnumMap<>(CanonicalItem.class);

        for (CanonicalItem item : CanonicalItem.values()) {
            List<AnnualFact> selected = selectorService.select(
                    facts, taxonomy, item,
                    registry.tagsFor(taxonomy, item),
                    componentTagsFor(taxonomy, item),
                    policy);

            result.put(item, selected.stream()
                    .sorted(Comparator.comparing(AnnualFact::periodEnd))
                    .toList());
        }

        return result;
    }

    private List<String> componentTagsFor(String taxonomy, CanonicalItem item) {
        return item == CanonicalItem.TOTAL_LIABILITIES
                ? registry.liabilityComponentsTags(taxonomy)
                : List.of();
    }

    private String detectTaxonomy(JsonNode facts) {
        return TAXONOMY_PRIORITY.stream()
                .filter(registry::hasTaxonomy)
                .filter(t -> !facts.path(t).isMissingNode())
                .findFirst()
                .orElse(null);
    }
}