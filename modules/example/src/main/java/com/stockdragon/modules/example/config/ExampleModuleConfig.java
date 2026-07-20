package com.stockdragon.modules.example.config;

import com.stockdragon.libraries.examplelib.config.ExampleLibConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Import(
        ExampleLibConfig.class
)
@Configuration
@ComponentScan(
        basePackages = {"com.stockdragon.modules.example"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)}
)
public class ExampleModuleConfig {
}
