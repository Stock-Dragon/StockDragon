package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.api.FinancialStatementsIngestion;
import com.stockdragon.libraries.financialstatements.client.EdgarCompanyFactsClient;
import com.stockdragon.libraries.financialstatements.model.dto.IngestionResult;
import com.stockdragon.libraries.financialstatements.model.persistence.ResolvedInstrument;
import com.stockdragon.libraries.financialstatements.model.type.IngestionStatus;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import com.stockdragon.libraries.financialstatements.model.type.StoreOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngestionService implements FinancialStatementsIngestion {

    private static final String SOURCE = "EDGAR";
    private static final String ENDPOINT = "companyfacts";

    private static final String MSG_FETCH_FAILED = "EDGAR fetch failed for CIK {}";
    private static final String MSG_RESOLUTION_FAILED = "Identifier resolution failed for {}";
    private static final String ERROR_RESOLUTION_ERROR = "RESOLUTION_ERROR: ";
    private static final String ERROR_EDGAR_ERROR = "EDGAR_ERROR: ";

    private final IdentifierResolverService identifierResolverService;
    private final EdgarCompanyFactsClient edgarCompanyFactsClient;
    private final RawPayloadStoreService rawPayloadStoreService;

    @Override
    public IngestionResult ingest(String isin) {
        ResolvedInstrument resolved;
        try {
            resolved = identifierResolverService.resolve(isin);
        } catch (RestClientException e) {
            log.warn(MSG_RESOLUTION_FAILED, isin, e);
            return IngestionResult.failed(isin, null, ERROR_RESOLUTION_ERROR + describe(e));
        }
        if (resolved.getOutcome() != ResolutionOutcome.RESOLVED) {
            return IngestionResult.unresolved(isin, resolved.getOutcome().name());
        }
        return fetchAndStore(isin, resolved.getCik());
    }

    private IngestionResult fetchAndStore(String isin, long cik) {
        try {
            String payload = edgarCompanyFactsClient.fetchCompanyFacts(cik);
            StoreOutcome outcome = rawPayloadStoreService.store(SOURCE, String.valueOf(cik), ENDPOINT, payload);
            return IngestionResult.of(toStatus(outcome), isin, cik);
        } catch (RestClientException e) {
            log.warn(MSG_FETCH_FAILED, cik, e);
            return IngestionResult.failed(isin, cik, ERROR_EDGAR_ERROR + describe(e));
        }
    }

    private IngestionStatus toStatus(StoreOutcome outcome) {
        return outcome == StoreOutcome.STORED ? IngestionStatus.FETCHED : IngestionStatus.UNCHANGED;
    }

    private String describe(RestClientException e) {
        if (e instanceof HttpStatusCodeException http) {
            return "HTTP_" + http.getStatusCode().value();
        }
        return e.getClass().getSimpleName();
    }
}
