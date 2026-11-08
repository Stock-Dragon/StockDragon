package com.stockdragon.libraries.financialstatements.model.dto;

import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;

public record CandidateSelection(ResolutionOutcome outcome, OpenFigiInstrument candidate) {

    private static final String ADR = "ADR";

    public static CandidateSelection rejected(ResolutionOutcome outcome) {
        return new CandidateSelection(outcome, null);
    }

    public static CandidateSelection selected(OpenFigiInstrument candidate) {
        return new CandidateSelection(ResolutionOutcome.RESOLVED, candidate);
    }

    public boolean isAdr() {
        return candidate != null && ADR.equals(candidate.securityType());
    }
}
