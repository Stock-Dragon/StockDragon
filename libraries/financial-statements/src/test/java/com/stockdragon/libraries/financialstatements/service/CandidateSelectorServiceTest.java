package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.model.dto.OpenFigiInstrument;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CandidateSelectorServiceTest {

    private final CandidateSelectorService selector = new CandidateSelectorService();

    private static OpenFigiInstrument instrument(String ticker, String exchCode, String securityType) {
        return new OpenFigiInstrument(null, null, ticker, exchCode, null, null,
                securityType, null, "Equity");
    }

    @Test
    void selectsUsListing() {
        var result = selector.select(List.of(
                instrument("APC", "GR", "Common Stock"),
                instrument("AAPLEUR", "EU", "Common Stock"),
                instrument("AAPL", "US", "Common Stock"),
                instrument("AAPL", "UW", "Common Stock")));

        assertThat(result.outcome()).isEqualTo(ResolutionOutcome.RESOLVED);
        assertThat(result.candidate().ticker()).isEqualTo("AAPL");
        assertThat(result.isAdr()).isFalse();
    }

    @Test
    void selectsUsListingForForeignIsin() {
        var result = selector.select(List.of(
                instrument("CSA", "GR", "Common Stock"),
                instrument("ACN", "US", "Common Stock")));

        assertThat(result.candidate().ticker()).isEqualTo("ACN");
    }

    @Test
    void selectsAdr() {
        var result = selector.select(List.of(
                instrument("TSFA", "GR", "ADR"),
                instrument("TSM", "US", "ADR")));

        assertThat(result.outcome()).isEqualTo(ResolutionOutcome.RESOLVED);
        assertThat(result.candidate().ticker()).isEqualTo("TSM");
        assertThat(result.isAdr()).isTrue();
    }

    @Test
    void polishCompanyWithOtcListingStillPassesThisStep() {
        var result = selector.select(List.of(
                instrument("PKO", "PW", "Common Stock"),
                instrument("PSZKF", "US", "Common Stock")));

        assertThat(result.candidate().ticker()).isEqualTo("PSZKF");
    }

    @Test
    void rejectsEtf() {
        var result = selector.select(List.of(
                instrument("CSSPX", "SW", "ETP"),
                instrument("CSTNL", "US", "ETP")));

        assertThat(result.outcome()).isEqualTo(ResolutionOutcome.NOT_AN_OPERATING_COMPANY);
    }

    @Test
    void rejectsWhenNoUsListing() {
        var result = selector.select(List.of(instrument("PKO", "PW", "Common Stock")));

        assertThat(result.outcome()).isEqualTo(ResolutionOutcome.NO_US_LISTING);
    }

    @Test
    void rejectsEmptyList() {
        assertThat(selector.select(List.of()).outcome()).isEqualTo(ResolutionOutcome.ISIN_UNKNOWN);
    }

    @Test
    void rejectsAmbiguousTickers() {
        var result = selector.select(List.of(
                instrument("AAA", "US", "Common Stock"),
                instrument("BBB", "US", "Common Stock")));

        assertThat(result.outcome()).isEqualTo(ResolutionOutcome.AMBIGUOUS);
    }
}
