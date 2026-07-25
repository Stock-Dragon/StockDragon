package com.stockdragon.modules.auth.service;

import com.stockdragon.modules.auth.service.api.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-min-32-characters-long-for-hs256");
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", 60L);
    }

    @Test
    void generatesTokenContainingUsername() {
        UserDetails userDetails = User.withUsername("test@stockdragon.com").password("irrelevant").authorities("ROLE_USER").build();

        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUserName(token)).isEqualTo("test@stockdragon.com");
    }

    @Test
    void tokenIsValidForMatchingUser() {
        UserDetails userDetails = User.withUsername("test@stockdragon.com").password("irrelevant").authorities("ROLE_USER").build();

        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void tokenIsInvalidForDifferentUser() {
        UserDetails original = User.withUsername("test@stockdragon.com").password("irrelevant").authorities("ROLE_USER").build();
        UserDetails different = User.withUsername("other@stockdragon.com").password("irrelevant").authorities("ROLE_USER").build();

        String token = jwtService.generateToken(original);

        assertThat(jwtService.isTokenValid(token, different)).isFalse();
    }

    @Test
    void expiredTokenThrowsExpiredJwtException() {
        JwtServiceImpl impl = new JwtServiceImpl();
        ReflectionTestUtils.setField(impl, "secret", "test-secret-key-min-32-characters-long-for-hs256");
        ReflectionTestUtils.setField(impl, "expirationMinutes", -1L);

        UserDetails userDetails = User.withUsername("test@stockdragon.com").password("irrelevant").authorities("ROLE_USER").build();
        String token = impl.generateToken(userDetails);

        assertThatThrownBy(() -> impl.isTokenValid(token, userDetails))
                .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }
}
