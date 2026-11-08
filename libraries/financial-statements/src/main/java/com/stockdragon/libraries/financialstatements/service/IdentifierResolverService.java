package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.client.OpenFigiClient;
import com.stockdragon.libraries.financialstatements.model.dto.CandidateSelection;
import com.stockdragon.libraries.financialstatements.model.dto.OpenFigiInstrument;
import com.stockdragon.libraries.financialstatements.model.dto.ResolutionDetails;
import com.stockdragon.libraries.financialstatements.model.persistence.ResolvedInstrument;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import com.stockdragon.libraries.financialstatements.repository.ResolvedInstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdentifierResolverService {

    private static final String RESOLUTION_SOURCE = "OPENFIGI+SEC_TICKERS";

    private final OpenFigiClient openFigiClient;
    private final CandidateSelectorService candidateSelectorService;
    private final TickerCikLookupService tickerCikLookupService;
    private final ResolvedInstrumentRepository resolvedInstrumentRepository;
    private final JsonMapper jsonMapper;

    public ResolvedInstrument resolve(String isin) {
        CandidateSelection selection = candidateSelectorService.select(openFigiClient.findByIsin(isin));
        if (selection.outcome() != ResolutionOutcome.RESOLVED) {
            return save(isin, selection.outcome(), null, null, null);
        }
        OpenFigiInstrument candidate = selection.candidate();
        Optional<Long> cik = tickerCikLookupService.findCik(candidate.ticker());
        if (cik.isEmpty()) {
            return save(isin, ResolutionOutcome.TICKER_NOT_IN_SEC, candidate.ticker(), null, detailsOf(selection));
        }
        return save(isin, ResolutionOutcome.RESOLVED, candidate.ticker(), cik.get(), detailsOf(selection));
    }

    private String detailsOf(CandidateSelection selection) {
        OpenFigiInstrument candidate = selection.candidate();
        return jsonMapper.writeValueAsString(
                new ResolutionDetails(selection.isAdr(), candidate.figi(), candidate.name())
        );
    }

    private ResolvedInstrument save(String isin, ResolutionOutcome outcome, String ticker, Long cik, String details) {
        return resolvedInstrumentRepository.save(new ResolvedInstrument(isin, outcome, ticker, cik, RESOLUTION_SOURCE, details));
    }
}
