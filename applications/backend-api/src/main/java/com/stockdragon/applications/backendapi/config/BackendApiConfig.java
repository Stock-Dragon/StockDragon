package com.stockdragon.applications.backendapi.config;

import com.stockdragon.libraries.financialstatements.config.FinancialStatementsConfig;
import com.stockdragon.modules.auth.config.AuthConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        AuthConfig.class,
        FinancialStatementsConfig.class
})
@Configuration
public class BackendApiConfig {
}
