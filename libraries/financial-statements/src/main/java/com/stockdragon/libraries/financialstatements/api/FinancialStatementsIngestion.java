package com.stockdragon.libraries.financialstatements.api;

import com.stockdragon.libraries.financialstatements.model.dto.IngestionResult;

public interface FinancialStatementsIngestion {

    IngestionResult ingest(String isin);
}
