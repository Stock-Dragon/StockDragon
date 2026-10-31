package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.client.SecTickersClient;
import com.stockdragon.libraries.financialstatements.model.dto.SecCompanyTicker;
import com.stockdragon.libraries.financialstatements.util.TickerNormalizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TickerCikLookupServiceTest {

    private final SecTickersClient client = mock(SecTickersClient.class);
    private TickerCikLookupService lookup;

    @BeforeEach
    void setUp() {
        when(client.fetchAll()).thenReturn(Map.of(
                "0", new SecCompanyTicker(320193L, "AAPL", "Apple Inc."),
                "1", new SecCompanyTicker(1067983L, "BRK-B", "BERKSHIRE HATHAWAY INC")));
        lookup = new TickerCikLookupService(client, new TickerNormalizer());
    }

    @Test
    void findsCik() {
        assertThat(lookup.findCik("AAPL")).contains(320193L);
    }

    @Test
    void ignoresCase() {
        assertThat(lookup.findCik("aapl")).contains(320193L);
    }

    @Test
    void normalizesClassSeparator() {
        assertThat(lookup.findCik("BRK.B")).contains(1067983L);
    }

    @Test
    void returnsEmptyForUnknownTicker() {
        assertThat(lookup.findCik("PSZKF")).isEmpty();
    }
}
