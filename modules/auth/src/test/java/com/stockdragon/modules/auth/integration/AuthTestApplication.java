package com.stockdragon.modules.auth.integration;

import com.stockdragon.modules.auth.config.AuthConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AuthConfig.class)
public class AuthTestApplication {
}
