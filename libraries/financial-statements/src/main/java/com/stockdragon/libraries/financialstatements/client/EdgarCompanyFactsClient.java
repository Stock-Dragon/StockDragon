package com.stockdragon.libraries.financialstatements.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class EdgarCompanyFactsClient {

    private static final String COMPANY_FACTS_PATH = "/api/xbrl/companyfacts/CIK{cik}.json";
    private static final String CIK_FORMAT = "%010d";

    private final RestClient secDataRestClient;

    public String fetchCompanyFacts(long cik) {
        return secDataRestClient.get()
                .uri(COMPANY_FACTS_PATH, CIK_FORMAT.formatted(cik))
                .retrieve()
                .body(String.class);
    }
}
