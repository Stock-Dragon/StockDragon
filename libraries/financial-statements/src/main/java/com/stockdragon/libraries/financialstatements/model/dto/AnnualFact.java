package com.stockdragon.libraries.financialstatements.model.dto;

import com.stockdragon.libraries.financialstatements.model.type.CanonicalItem;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AnnualFact(
        CanonicalItem item,
        int fiscalYear,
        LocalDate periodEnd,
        BigDecimal value,
        String currency,
        String sourceTag,
        String form,
        LocalDate filed
) {}
