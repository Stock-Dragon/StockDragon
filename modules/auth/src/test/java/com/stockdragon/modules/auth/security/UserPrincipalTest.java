package com.stockdragon.modules.auth.security;

import com.stockdragon.modules.auth.model.persistence.User;
import com.stockdragon.modules.auth.model.type.UserRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalTest {

    @Test
    void exposesEmailAsUsername() {
        User user = new User("test@stockdragon.com", "hashed-password", UserRole.USER);

        UserPrincipal principal = new UserPrincipal(user);

        assertThat(principal.getUsername()).isEqualTo("test@stockdragon.com");
        assertThat(principal.getPassword()).isEqualTo("hashed-password");
    }

    @Test
    void mapsRoleToGrantedAuthorityWithRolePrefix() {
        User user = new User("admin@stockdragon.com", "hashed-password", UserRole.ADMIN);

        UserPrincipal principal = new UserPrincipal(user);

        assertThat(principal.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_ADMIN");
    }
}