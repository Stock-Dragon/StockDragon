package com.stockdragon.libraries.financialstatements.service;

import com.stockdragon.libraries.financialstatements.model.persistence.RawPayload;
import com.stockdragon.libraries.financialstatements.model.type.StoreOutcome;
import com.stockdragon.libraries.financialstatements.repository.RawPayloadRepository;
import com.stockdragon.libraries.financialstatements.util.ContentHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RawPayloadStoreService {

    private static final int HTTP_OK = 200;

    private final RawPayloadRepository rawPayloadRepository;
    private final ContentHasher contentHasher;

    @Transactional
    public StoreOutcome store(String source, String sourceKey, String endpoint, String payload) {
        String hash = contentHasher.sha256(payload);
        boolean unchanged = rawPayloadRepository.findLatest(source, sourceKey, endpoint)
                .map(latest -> latest.getContentHash().equals(hash))
                .orElse(false);
        if (unchanged) {
            return StoreOutcome.UNCHANGED;
        }
        rawPayloadRepository.save(new RawPayload(source, sourceKey, endpoint, HTTP_OK, hash, payload));
        return StoreOutcome.STORED;
    }
}
