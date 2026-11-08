package com.stockdragon.libraries.financialstatements.model.dto;

import com.stockdragon.libraries.financialstatements.model.type.IngestionStatus;

public record IngestionResult(IngestionStatus status, String isin, Long cik, String reason) {

    public static IngestionResult of(IngestionStatus status, String isin, Long cik) {
        return new IngestionResult(status, isin, cik, null);
    }

    public static IngestionResult unresolved(String isin, String reason) {
        return new IngestionResult(IngestionStatus.UNRESOLVED, isin, null, reason);
    }

    public static IngestionResult failed(String isin, Long cik, String reason) {
        return new IngestionResult(IngestionStatus.FAILED, isin, cik, reason);
    }
}
