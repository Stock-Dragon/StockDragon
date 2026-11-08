package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.api.FinancialStatementsQuery;
import com.stockdragon.libraries.financialstatements.exception.InstrumentNotResolvedException;
import com.stockdragon.libraries.financialstatements.model.dto.AnnualFact;
import com.stockdragon.libraries.financialstatements.model.persistence.ResolvedInstrument;
import com.stockdragon.libraries.financialstatements.model.type.CanonicalItem;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import com.stockdragon.libraries.financialstatements.repository.ResolvedInstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialStatementsQueryService implements FinancialStatementsQuery {

    private final ResolvedInstrumentRepository resolvedInstrumentRepository;
    private final IdentifierResolverService identifierResolverService;
    private final CompanyFactsParserService companyFactsParserService;

    @Override
    public List<AnnualFact> getAnnualFacts(String isin, CanonicalItem item, FactsSelectorService.RestatementPolicy policy) {
        long cik = resolveCik(isin);
        return companyFactsParserService.parse(cik, policy)
                .getOrDefault(item, List.of());
    }

    private long resolveCik(String isin) {
        ResolvedInstrument resolved = resolvedInstrumentRepository.findLatestByIsin(isin)
                .filter(r -> r.getOutcome() == ResolutionOutcome.RESOLVED)
                .orElseGet(() -> identifierResolverService.resolve(isin));
        if (resolved.getOutcome() != ResolutionOutcome.RESOLVED || resolved.getCik() == null) {
            throw new InstrumentNotResolvedException(isin, resolved.getOutcome());
        }
        return resolved.getCik();
    }
}
