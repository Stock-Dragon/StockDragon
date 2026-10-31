package com.stockdragon.libraries.financialstatements.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "financial-statements.sec")
public record SecProperties(@NotBlank String baseUrl, @NotBlank String userAgent) {}
