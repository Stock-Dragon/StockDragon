package com.stockdragon.libraries.financialstatements.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(
        basePackages = {"com.stockdragon.libraries.financialstatements"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)}
)
@EntityScan(basePackages = {
        "com.stockdragon.libraries.financialstatements"
})
@EnableJpaRepositories(basePackages = "com.stockdragon.libraries.financialstatements.repository")
@Import({
        OpenFigiRestClientConfig.class,
        SecRestClientConfig.class
})
public class FinancialStatementsConfig {
}
