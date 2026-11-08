package com.stockdragon.libraries.financialstatements.client;

import com.stockdragon.libraries.financialstatements.model.dto.SecCompanyTicker;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SecTickersClient {

    private static final String TICKERS_PATH = "/files/company_tickers.json";

    private static final ParameterizedTypeReference<Map<String, SecCompanyTicker>> RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {};

    private final RestClient secRestClient;

    public Map<String, SecCompanyTicker> fetchAll() {
        Map<String, SecCompanyTicker> response = secRestClient.get()
                .uri(TICKERS_PATH)
                .retrieve()
                .body(RESPONSE_TYPE);
        return response == null ? Map.of() : response;
    }
}
