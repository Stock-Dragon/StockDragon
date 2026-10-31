package com.stockdragon.libraries.financialstatements.model.persistence;

import com.stockdragon.libraries.jpacommons.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "fs_raw_payload")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RawPayload extends BaseEntity {

    @Column(nullable = false, length = 32)
    private String source;

    @Column(name = "source_key", nullable = false, length = 64)
    private String sourceKey;

    @Column(nullable = false, length = 64)
    private String endpoint;

    @Column(name = "http_status", nullable = false)
    private int httpStatus;

    @Column(name = "content_hash", nullable = false, length = 64)
    private String contentHash;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;
}
