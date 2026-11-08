package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.model.persistence.RawPayload;
import com.stockdragon.libraries.financialstatements.model.type.StoreOutcome;
import com.stockdragon.libraries.financialstatements.repository.RawPayloadRepository;
import com.stockdragon.libraries.financialstatements.util.ContentHasher;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RawPayloadStoreServiceTest {

    private static final String PAYLOAD = "{\"v\":1}";

    private final RawPayloadRepository repository = mock(RawPayloadRepository.class);
    private final ContentHasher hasher = new ContentHasher();
    private final RawPayloadStoreService store = new RawPayloadStoreService(repository, hasher);

    @Test
    void storesFirstPayload() {
        when(repository.findLatest("EDGAR", "320193", "companyfacts")).thenReturn(Optional.empty());

        assertThat(store.store("EDGAR", "320193", "companyfacts", PAYLOAD)).isEqualTo(StoreOutcome.STORED);
        verify(repository).save(any(RawPayload.class));
    }

    @Test
    void skipsUnchangedPayload() {
        RawPayload previous = new RawPayload("EDGAR", "320193", "companyfacts", 200, hasher.sha256(PAYLOAD), PAYLOAD);
        when(repository.findLatest("EDGAR", "320193", "companyfacts")).thenReturn(Optional.of(previous));

        assertThat(store.store("EDGAR", "320193", "companyfacts", PAYLOAD)).isEqualTo(StoreOutcome.UNCHANGED);
        verify(repository, never()).save(any());
    }

    @Test
    void storesChangedPayload() {
        RawPayload previous = new RawPayload("EDGAR", "320193", "companyfacts", 200, hasher.sha256("{\"v\":0}"), "{\"v\":0}");
        when(repository.findLatest("EDGAR", "320193", "companyfacts")).thenReturn(Optional.of(previous));

        assertThat(store.store("EDGAR", "320193", "companyfacts", PAYLOAD)).isEqualTo(StoreOutcome.STORED);
        verify(repository).save(any(RawPayload.class));
    }

    @Test
    void hashHasExpectedLength() {
        assertThat(hasher.sha256(PAYLOAD)).hasSize(64);
    }
}
