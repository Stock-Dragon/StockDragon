package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.client.SecTickersClient;
import com.stockdragon.libraries.financialstatements.model.dto.SecCompanyTicker;
import com.stockdragon.libraries.financialstatements.util.TickerNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TickerCikLookupService {

    private static final Duration TTL = Duration.ofHours(24);

    private final SecTickersClient secTickersClient;
    private final TickerNormalizer tickerNormalizer;

    private volatile Map<String, Long> index = Map.of();
    private volatile Instant loadedAt = Instant.MIN;

    public Optional<Long> findCik(String ticker) {
        return Optional.ofNullable(currentIndex().get(tickerNormalizer.normalize(ticker)));
    }

    private Map<String, Long> currentIndex() {
        if (isStale()) {
            refresh();
        }
        return index;
    }

    private synchronized void refresh() {
        if (isStale()) {
            index = buildIndex(secTickersClient.fetchAll().values());
            loadedAt = Instant.now();
        }
    }

    private boolean isStale() {
        return Instant.now().isAfter(loadedAt.plus(TTL));
    }

    private Map<String, Long> buildIndex(Collection<SecCompanyTicker> entries) {
        Map<String, Long> result = new HashMap<>();
        for (SecCompanyTicker entry : entries) {
            result.putIfAbsent(tickerNormalizer.normalize(entry.ticker()), entry.cik());
        }
        return result;
    }
}
