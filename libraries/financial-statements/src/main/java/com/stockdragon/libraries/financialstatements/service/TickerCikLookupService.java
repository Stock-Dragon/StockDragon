package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.client.SecTickersClient;
import com.stockdragon.libraries.financialstatements.model.dto.SecCompanyTicker;
import com.stockdragon.libraries.financialstatements.util.TickerNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TickerCikLookupService {

    private final SecTickersClient secTickersClient;
    private final TickerNormalizer tickerNormalizer;

    public Optional<Long> findCik(String ticker) {
        Map<String, Long> index = buildIndex(secTickersClient.fetchAll().values());
        return Optional.ofNullable(index.get(tickerNormalizer.normalize(ticker)));
    }

    private Map<String, Long> buildIndex(Collection<SecCompanyTicker> entries) {
        Map<String, Long> index = new HashMap<>();
        for (SecCompanyTicker entry : entries) {
            index.putIfAbsent(tickerNormalizer.normalize(entry.ticker()), entry.cik());
        }
        return index;
    }
}
