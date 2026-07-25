package com.stockdragon.modules.auth.service;

import com.stockdragon.modules.auth.model.dto.AuthResponse;
import com.stockdragon.modules.auth.model.dto.LoginRequest;
import com.stockdragon.modules.auth.model.dto.RegisterRequest;
import com.stockdragon.modules.auth.model.persistence.User;
import com.stockdragon.modules.auth.model.type.UserRole;
import com.stockdragon.modules.auth.repository.UserRepository;
import com.stockdragon.modules.auth.service.api.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void registerCreatesNewUserAndReturnsToken() {
        RegisterRequest request = new RegisterRequest("new@stockdragon.com", "password123");
        when(userRepository.existsByEmail("new@stockdragon.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("fake-jwt-token");
        assertThat(response.email()).isEqualTo("new@stockdragon.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerThrowsWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("existing@stockdragon.com", "password123");
        when(userRepository.existsByEmail("existing@stockdragon.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void loginReturnsTokenForValidCredentials() {
        LoginRequest request = new LoginRequest("test@stockdragon.com", "password123");
        User user = new User("test@stockdragon.com", "hashed-password", UserRole.USER);

        when(userRepository.findByEmail("test@stockdragon.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.token()).isEqualTo("fake-jwt-token");
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginThrowsOnInvalidCredentials() {
        LoginRequest request = new LoginRequest("test@stockdragon.com", "wrong-password");
        doThrow(new BadCredentialsException("bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}
