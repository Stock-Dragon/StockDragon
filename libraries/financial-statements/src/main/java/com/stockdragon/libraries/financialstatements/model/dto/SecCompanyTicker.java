package com.stockdragon.libraries.financialstatements.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SecCompanyTicker(
        @JsonProperty("cik_str") long cik,
        String ticker,
        String title
) {}
