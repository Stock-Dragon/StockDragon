package com.stockdragon.libraries.financialstatements.config;

import aQute.bnd.annotation.jpms.Open;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(OpenFigiProperties.class)
public class OpenFigiRestClientConfig {

    private static final String API_KEY_HEADER = "X-OPENFIGI-APIKEY";

    @Bean
    protected RestClient openFigiRestClient(OpenFigiProperties properties) {
        RestClient.Builder builder = RestClient.builder().baseUrl(properties.baseUrl());
        if (properties.hasApiKey()) {
            builder.defaultHeader(API_KEY_HEADER, properties.apiKey());
        }
        return builder.build();
    }
}
