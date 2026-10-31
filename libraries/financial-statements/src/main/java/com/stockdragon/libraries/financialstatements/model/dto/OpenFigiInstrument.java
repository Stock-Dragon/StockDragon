package com.stockdragon.libraries.financialstatements.model.dto;

public record OpenFigiInstrument(
        String figi,
        String name,
        String ticker,
        String exchCode,
        String compositeFIGI,
        String shareClassFIGI,
        String securityType,
        String securityType2,
        String marketSector
) {}
