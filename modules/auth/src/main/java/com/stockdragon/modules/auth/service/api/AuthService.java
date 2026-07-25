package com.stockdragon.modules.auth.service.api;

import com.stockdragon.modules.auth.model.dto.AuthResponse;
import com.stockdragon.modules.auth.model.dto.LoginRequest;
import com.stockdragon.modules.auth.model.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
