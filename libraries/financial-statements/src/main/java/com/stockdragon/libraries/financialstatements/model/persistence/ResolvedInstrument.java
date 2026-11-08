package com.stockdragon.libraries.financialstatements.model.persistence;

import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;
import com.stockdragon.libraries.jpacommons.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "fs_resolved_instrument")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResolvedInstrument extends BaseEntity {

    @Column(nullable = false, length = 12)
    private String isin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ResolutionOutcome outcome;

    @Column(length = 32)
    private String ticker;

    private Long cik;

    @Column(name = "resolution_source", nullable = false, length = 64)
    private String resolutionSource;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB")
    private String details;
}
