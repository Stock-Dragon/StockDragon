package com.stockdragon.modules.auth.model.dto;

public record AuthResponse(
        String token,
        String email
) {}
