package com.stockdragon.modules.auth.service;

import com.stockdragon.modules.auth.model.persistence.User;
import com.stockdragon.modules.auth.model.type.UserRole;
import com.stockdragon.modules.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl service;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        service = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadsUserByEmail() {
        User user = new User("test@stockdragon.com", "hash", UserRole.USER);
        when(userRepository.findByEmail("test@stockdragon.com")).thenReturn(Optional.of(user));

        var result = service.loadUserByUsername("test@stockdragon.com");

        assertThat(result.getUsername()).isEqualTo("test@stockdragon.com");
    }

    @Test
    void throwsWhenUserNotFound() {
        when(userRepository.findByEmail("missing@stockdragon.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("missing@stockdragon.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
