package com.stockdragon.libraries.financialstatements.model.dto;

import java.util.List;

public record OpenFigiMappingResult(
        List<OpenFigiInstrument> data,
        String warning
) {}
