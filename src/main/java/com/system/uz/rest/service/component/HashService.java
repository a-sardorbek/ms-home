package com.system.uz.rest.service.component;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class HashService {

    private static final String NUMERIC = "123456789";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHANUMERIC = NUMERIC + ALPHA;

    private static final Random random = new Random();

    public static String generateFileName() {
        char[] randomNum = new char[30];
        for (int idx = 0; idx < randomNum.length; ++idx)
            randomNum[idx] = ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length()));
        return new String(randomNum).toUpperCase();
    }
}
