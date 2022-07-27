package io.github.talelin.latticy.common.util;

import java.security.SecureRandom;

/**
 * @author Gadfly
 */
@SuppressWarnings("SpellCheckingInspection")
public class CaptchaUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String RANDOM_STRING = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWSYZ";

    /**
     * 随机字符的获取
     */
    public static String getRandomString(int num) {
        num = num > 0 ? num : RANDOM_STRING.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int number = RANDOM.nextInt(RANDOM_STRING.length());
            sb.append(RANDOM_STRING.charAt(number));
        }
        return sb.toString();
    }
}