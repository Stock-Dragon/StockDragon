package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.client.OpenFigiClient;
import com.stockdragon.libraries.financialstatements.model.dto.OpenFigiInstrument;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import com.stockdragon.libraries.financialstatements.repository.ResolvedInstrumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IdentifierResolverTest {

    private static final String ISIN = "US0000000000";

    private final OpenFigiClient openFigiClient = mock(OpenFigiClient.class);
    private final TickerCikLookupService tickerCikLookup = mock(TickerCikLookupService.class);
    private final ResolvedInstrumentRepository repository = mock(ResolvedInstrumentRepository.class);
    private IdentifierResolverService resolver;

    @BeforeEach
    void setUp() {
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        resolver = new IdentifierResolverService(openFigiClient, new CandidateSelectorService(),
                tickerCikLookup, repository, JsonMapper.builder().build());
    }

    private static OpenFigiInstrument instrument(String ticker, String exchCode, String securityType) {
        return new OpenFigiInstrument("BBG000TEST", "TEST INC", ticker, exchCode, null, null,
                securityType, null, "Equity");
    }

    @Test
    void resolvesCommonStock() {
        when(openFigiClient.findByIsin(ISIN)).thenReturn(List.of(instrument("AAPL", "US", "Common Stock")));
        when(tickerCikLookup.findCik("AAPL")).thenReturn(Optional.of(320193L));

        var result = resolver.resolve(ISIN);

        assertThat(result.getOutcome()).isEqualTo(ResolutionOutcome.RESOLVED);
        assertThat(result.getTicker()).isEqualTo("AAPL");
        assertThat(result.getCik()).isEqualTo(320193L);
        assertThat(result.getDetails()).contains("\"adr\":false");
    }

    @Test
    void marksAdr() {
        when(openFigiClient.findByIsin(ISIN)).thenReturn(List.of(instrument("TSM", "US", "ADR")));
        when(tickerCikLookup.findCik("TSM")).thenReturn(Optional.of(1046179L));

        var result = resolver.resolve(ISIN);

        assertThat(result.getOutcome()).isEqualTo(ResolutionOutcome.RESOLVED);
        assertThat(result.getDetails()).contains("\"adr\":true");
    }

    @Test
    void tickerMissingInSec() {
        when(openFigiClient.findByIsin(ISIN)).thenReturn(List.of(instrument("PSZKF", "US", "Common Stock")));
        when(tickerCikLookup.findCik("PSZKF")).thenReturn(Optional.empty());

        var result = resolver.resolve(ISIN);

        assertThat(result.getOutcome()).isEqualTo(ResolutionOutcome.TICKER_NOT_IN_SEC);
        assertThat(result.getTicker()).isEqualTo("PSZKF");
        assertThat(result.getCik()).isNull();
    }

    @Test
    void rejectedBySelector() {
        when(openFigiClient.findByIsin(ISIN)).thenReturn(List.of(instrument("CSTNL", "US", "ETP")));

        var result = resolver.resolve(ISIN);

        assertThat(result.getOutcome()).isEqualTo(ResolutionOutcome.NOT_AN_OPERATING_COMPANY);
        assertThat(result.getTicker()).isNull();
        org.mockito.Mockito.verifyNoInteractions(tickerCikLookup);
    }

    @Test
    void unknownIsin() {
        when(openFigiClient.findByIsin(ISIN)).thenReturn(List.of());

        assertThat(resolver.resolve(ISIN).getOutcome()).isEqualTo(ResolutionOutcome.ISIN_UNKNOWN);
    }
}
