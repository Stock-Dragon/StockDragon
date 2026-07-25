package com.stockdragon.libraries.greetingslib.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.Random;

@Configuration
@ComponentScan(
        basePackages = {"com.stockdragon.libraries.greetingslib"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)}
)
public class GreetingsLibConfig {

    @Bean
    public Random random() {
        return new Random();
    }
}
