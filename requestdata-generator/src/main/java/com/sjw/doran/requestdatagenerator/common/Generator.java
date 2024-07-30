package com.sjw.doran.requestdatagenerator.common;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Generator {

    public String generateRandomString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public int generateRandomInteger() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return random.nextInt(10000);
    }

    public int generateRandomInteger(int size) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int val = random.nextInt(size);
        if (val <= 0) val += size;
        return val;
    }
}
