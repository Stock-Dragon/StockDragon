package com.stockdragon.libraries.financialstatements.repository;

import com.stockdragon.libraries.financialstatements.model.persistence.ResolvedInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResolvedInstrumentRepository extends JpaRepository<ResolvedInstrument, UUID> {

    @Query("""
        SELECT r FROM ResolvedInstrument r
        WHERE r.isin = :isin
        ORDER BY r.createdAt DESC
        LIMIT 1
    """)
    Optional<ResolvedInstrument> findLatestByIsin(@Param("isin") String isin);
}
