package com.stockdragon.libraries.financialstatements.integration;

import com.stockdragon.libraries.financialstatements.config.FinancialStatementsConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FinancialStatementsConfig.class)
public class FinancialStatementsTestApplication {
}
