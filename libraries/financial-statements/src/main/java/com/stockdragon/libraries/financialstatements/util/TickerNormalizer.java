package com.stockdragon.libraries.financialstatements.util;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TickerNormalizer {

    public String normalize(String ticker) {
        return ticker.trim().toUpperCase(Locale.ROOT).replace(".", "-");
    }
}
