package com.stockdragon.libraries.financialstatements.api;

import com.stockdragon.libraries.financialstatements.model.dto.AnnualFact;
import com.stockdragon.libraries.financialstatements.model.type.CanonicalItem;
import com.stockdragon.libraries.financialstatements.service.FactsSelectorService.RestatementPolicy;

import java.util.List;

public interface FinancialStatementsQuery {

    List<AnnualFact> getAnnualFacts(String isin, CanonicalItem item, RestatementPolicy policy);

    default List<AnnualFact> getAnnualFacts(String isin, CanonicalItem item) {
        return getAnnualFacts(isin, item, RestatementPolicy.LATEST_RESTATED);
    }
}
