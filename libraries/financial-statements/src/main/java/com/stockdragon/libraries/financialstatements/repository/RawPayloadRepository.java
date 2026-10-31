package com.stockdragon.libraries.financialstatements.repository;

import com.stockdragon.libraries.financialstatements.model.persistence.RawPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RawPayloadRepository extends JpaRepository<RawPayload, UUID> {

    @Query("""
        SELECT p FROM RawPayload p
        WHERE p.source = :source
            AND p.sourceKey = :sourceKey
            AND p.endpoint = :endpoint
        ORDER BY p.createdAt DESC
        LIMIT 1
    """)
    Optional<RawPayload> findLatest(
            @Param("source") String source,
            @Param("sourceKey") String sourceKey,
            @Param("endpoint") String endpoint
    );
}
