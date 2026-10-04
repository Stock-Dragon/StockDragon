package com.stockdragon.modules.llmgateway.exceptions;

public class UnsupportedLlmProviderException extends RuntimeException {

    public UnsupportedLlmProviderException(String message) {
        super(message);
    }
}