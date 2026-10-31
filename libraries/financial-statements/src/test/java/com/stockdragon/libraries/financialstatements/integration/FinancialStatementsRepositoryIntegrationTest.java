package com.stockdragon.libraries.financialstatements.integration;

import com.stockdragon.libraries.financialstatements.model.persistence.RawPayload;
import com.stockdragon.libraries.financialstatements.model.persistence.ResolvedInstrument;
import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import com.stockdragon.libraries.financialstatements.repository.RawPayloadRepository;
import com.stockdragon.libraries.financialstatements.repository.ResolvedInstrumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = FinancialStatementsTestApplication.class)
public class FinancialStatementsRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("financial-statements.openfigi.base-url", () -> "http://localhost");
    }

    @Autowired
    private RawPayloadRepository rawPayloadRepository;

    @Autowired
    private ResolvedInstrumentRepository resolvedInstrumentRepository;

    private static final String HASH_1 = "a".repeat(64);
    private static final String HASH_2 = "b".repeat(64);

    @Test
    void findLatestReturnsMostRecentPayload() throws Exception {
        rawPayloadRepository.save(new RawPayload("EDGAR", "320193", "companyfacts", 200, HASH_1, "{\"v\":1}"));
        Thread.sleep(10);
        rawPayloadRepository.save(new RawPayload("EDGAR", "320193", "companyfacts", 200, HASH_2, "{\"v\":2}"));

        var latest = rawPayloadRepository.findLatest("EDGAR", "320193", "companyfacts");

        assertThat(latest).isPresent();
        assertThat(latest.get().getContentHash()).isEqualTo(HASH_2);
        assertThat(latest.get().getPayload()).isEqualTo("{\"v\":2}");
    }

    @Test
    void findLatestReturnsEmptyWhenNothingStored() {
        assertThat(rawPayloadRepository.findLatest("EDGAR", "999", "companyfacts")).isEmpty();
    }

    @Test
    void resolvedInstrumentWithJsonDetailsRoundTrips() {
        resolvedInstrumentRepository.save(new ResolvedInstrument(
                "US0378331005", ResolutionOutcome.RESOLVED, "AAPL", 320193L,
                "OPENFIGI+SEC_TICKERS", "{\"candidates\":[\"AAPL\"]}"));

        var found = resolvedInstrumentRepository.findLatestByIsin("US0378331005");

        assertThat(found).isPresent();
        assertThat(found.get().getCik()).isEqualTo(320193L);
        assertThat(found.get().getDetails()).contains("AAPL");
    }
}
