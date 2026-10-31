package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.model.dto.CandidateSelection;
import com.stockdragon.libraries.financialstatements.model.dto.OpenFigiInstrument;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CandidateSelectorService {

    private static final String US_EXCHANGE = "US";
    private static final String EQUITY_SECTOR = "Equity";
    private static final Set<String> OPERATING_TYPES = Set.of("Common Stock", "ADR");

    public CandidateSelection select(List<OpenFigiInstrument> candidates) {
        if (candidates.isEmpty()) {
            return CandidateSelection.rejected(ResolutionOutcome.ISIN_UNKNOWN);
        }
        List<OpenFigiInstrument> usEquities = onlyUsEquities(candidates);
        if (usEquities.isEmpty()) {
            return CandidateSelection.rejected(ResolutionOutcome.NO_US_LISTING);
        }
        List<OpenFigiInstrument> operating = onlyOperatingCompanies(usEquities);
        if (operating.isEmpty()) {
            return CandidateSelection.rejected(ResolutionOutcome.NOT_AN_OPERATING_COMPANY);
        }
        if (distinctTickers(operating).size() > 1) {
            return CandidateSelection.rejected(ResolutionOutcome.AMBIGUOUS);
        }
        return CandidateSelection.selected(operating.getFirst());
    }

    private List<OpenFigiInstrument> onlyUsEquities(List<OpenFigiInstrument> candidates) {
        return candidates.stream()
                .filter(c -> US_EXCHANGE.equals(c.exchCode()))
                .filter(c -> EQUITY_SECTOR.equals(c.marketSector()))
                .toList();
    }

    private List<OpenFigiInstrument> onlyOperatingCompanies(List<OpenFigiInstrument> candidates) {
        return candidates.stream()
                .filter(c -> OPERATING_TYPES.contains(c.securityType()))
                .toList();
    }

    private Set<String> distinctTickers(List<OpenFigiInstrument> candidates) {
        return candidates.stream()
                .map(OpenFigiInstrument::ticker)
                .collect(Collectors.toSet());
    }
}
