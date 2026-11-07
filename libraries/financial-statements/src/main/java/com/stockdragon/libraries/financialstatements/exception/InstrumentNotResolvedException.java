package com.stockdragon.libraries.financialstatements.exception;

import com.stockdragon.libraries.financialstatements.model.type.ResolutionOutcome;

public class InstrumentNotResolvedException extends RuntimeException{

    public InstrumentNotResolvedException(String isin, ResolutionOutcome outcome) {
        super("Cannot resolve ISIN %s: %s".formatted(isin, outcome));
    }
}
