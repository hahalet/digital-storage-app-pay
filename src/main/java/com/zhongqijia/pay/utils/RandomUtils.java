package com.zhongqijia.pay.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {
    private static final String ALPHAMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public RandomUtils() {
    }

    public static String genRandomStringByLength(int length) {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length());
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(number));
        }

        return sb.toString();
    }
}