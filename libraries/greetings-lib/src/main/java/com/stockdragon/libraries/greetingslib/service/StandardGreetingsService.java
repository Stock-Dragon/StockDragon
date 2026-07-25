package com.stockdragon.libraries.greetingslib.service;

import com.stockdragon.libraries.greetingslib.service.api.GreetingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StandardGreetingsService implements GreetingsService {

    private static final String STANDARD_GREETING = "Hello World!";

    @Override
    public String makeGreeting() {
        return STANDARD_GREETING;
    }
}
