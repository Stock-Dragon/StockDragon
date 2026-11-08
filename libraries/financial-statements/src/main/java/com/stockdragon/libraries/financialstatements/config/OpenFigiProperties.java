package com.stockdragon.libraries.financialstatements.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "financial-statements.openfigi")
public record OpenFigiProperties(@NotBlank String baseUrl, String apiKey) {

    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isBlank();
    }
}
