package com.stockdragon.modules.auth.service;

import com.stockdragon.modules.auth.model.dto.AuthResponse;
import com.stockdragon.modules.auth.model.dto.LoginRequest;
import com.stockdragon.modules.auth.model.dto.RegisterRequest;
import com.stockdragon.modules.auth.model.persistence.User;
import com.stockdragon.modules.auth.model.type.UserRole;
import com.stockdragon.modules.auth.repository.UserRepository;
import com.stockdragon.modules.auth.security.UserPrincipal;
import com.stockdragon.modules.auth.service.api.AuthService;
import com.stockdragon.modules.auth.service.api.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String ERROR_EMAIL_ALREADY_IN_USE = "Email already in use";
    private static final String ERROR_INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(ERROR_EMAIL_ALREADY_IN_USE);
        }
        User user = new User(request.email(), passwordEncoder.encode(request.password()), UserRole.USER);
        userRepository.save(user);
        String token = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponse(token, user.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (Exception e) {
            throw new BadCredentialsException(ERROR_INVALID_EMAIL_OR_PASSWORD);
        }
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException(ERROR_INVALID_EMAIL_OR_PASSWORD));
        String token = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponse(token, user.getEmail());
    }
}
