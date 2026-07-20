package com.stockdragon.applications.backendapi.config;

import com.stockdragon.modules.example.config.ExampleModuleConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(
        ExampleModuleConfig.class
)
@Configuration
public class BackendApiConfig {
}
