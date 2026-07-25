package com.stockdragon.libraries.greetingslib.service;

import com.stockdragon.libraries.greetingslib.service.api.GreetingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomGreetingsService implements GreetingsService {

    private static final String[] GREETINGS_TYPES = {"Ahoy!", "Welcome!", "Hi!", "Hello"};

    private final Random random;

    @Override
    public String makeGreeting() {
        return getRandomGreetings();
    }

    private String getRandomGreetings() {
        return GREETINGS_TYPES[randomIndex()];
    }

    private int randomIndex() {
        return random.nextInt(4);
    }
}
