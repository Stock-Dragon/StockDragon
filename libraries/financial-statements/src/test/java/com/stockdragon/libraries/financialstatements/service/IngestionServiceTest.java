package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.client.EdgarCompanyFactsClient;
import com.stockdragon.libraries.financialstatements.model.dto.IngestionResult;
import com.stockdragon.libraries.financialstatements.model.persistence.ResolvedInstrument;
import com.stockdragon.libraries.financialstatements.model.type.IngestionStatus;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import com.stockdragon.libraries.financialstatements.model.type.StoreOutcome;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IngestionServiceTest {

    private static final String ISIN = "US0378331005";
    private static final long CIK = 320193L;

    private final IdentifierResolverService resolver = mock(IdentifierResolverService.class);
    private final EdgarCompanyFactsClient edgarClient = mock(EdgarCompanyFactsClient.class);
    private final RawPayloadStoreService store = mock(RawPayloadStoreService.class);
    private final IngestionService service = new IngestionService(resolver, edgarClient, store);

    private static ResolvedInstrument resolved(ResolutionOutcome outcome, Long cik) {
        return new ResolvedInstrument(ISIN, outcome, "AAPL", cik, "TEST", null);
    }

    @Test
    void fetched() {
        when(resolver.resolve(ISIN)).thenReturn(resolved(ResolutionOutcome.RESOLVED, CIK));
        when(edgarClient.fetchCompanyFacts(CIK)).thenReturn("{}");
        when(store.store("EDGAR", "320193", "companyfacts", "{}")).thenReturn(StoreOutcome.STORED);

        IngestionResult result = service.ingest(ISIN);

        assertThat(result.status()).isEqualTo(IngestionStatus.FETCHED);
        assertThat(result.cik()).isEqualTo(CIK);
    }

    @Test
    void unchanged() {
        when(resolver.resolve(ISIN)).thenReturn(resolved(ResolutionOutcome.RESOLVED, CIK));
        when(edgarClient.fetchCompanyFacts(CIK)).thenReturn("{}");
        when(store.store("EDGAR", "320193", "companyfacts", "{}")).thenReturn(StoreOutcome.UNCHANGED);

        assertThat(service.ingest(ISIN).status()).isEqualTo(IngestionStatus.UNCHANGED);
    }

    @Test
    void unresolved() {
        when(resolver.resolve(ISIN)).thenReturn(resolved(ResolutionOutcome.TICKER_NOT_IN_SEC, null));

        IngestionResult result = service.ingest(ISIN);

        assertThat(result.status()).isEqualTo(IngestionStatus.UNRESOLVED);
        assertThat(result.reason()).isEqualTo("TICKER_NOT_IN_SEC");
        verifyNoInteractions(edgarClient, store);
    }

    @Test
    void edgarFailure() {
        when(resolver.resolve(ISIN)).thenReturn(resolved(ResolutionOutcome.RESOLVED, CIK));
        when(edgarClient.fetchCompanyFacts(CIK)).thenThrow(new RestClientException("boom"));

        IngestionResult result = service.ingest(ISIN);

        assertThat(result.status()).isEqualTo(IngestionStatus.FAILED);
        assertThat(result.cik()).isEqualTo(CIK);
        verifyNoInteractions(store);
    }

    @Test
    void resolutionFailure() {
        when(resolver.resolve(ISIN)).thenThrow(new RestClientException("limit"));

        IngestionResult result = service.ingest(ISIN);

        assertThat(result.status()).isEqualTo(IngestionStatus.FAILED);
        assertThat(result.cik()).isNull();
    }
}
