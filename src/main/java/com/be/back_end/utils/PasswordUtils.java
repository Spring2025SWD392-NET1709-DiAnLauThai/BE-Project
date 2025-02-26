package com.be.back_end.utils;

import java.security.SecureRandom;

public class PasswordUtils {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%&*()_+-=[]|,./?><";
    private static final String PASSWORD_ALLOW = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHAR;
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(12);
        
        // Ensure at least one character from each category
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        password.append(SPECIAL_CHAR.charAt(random.nextInt(SPECIAL_CHAR.length())));
        
        // Fill rest of the password
        for (int i = 4; i < 12; i++) {
            password.append(PASSWORD_ALLOW.charAt(random.nextInt(PASSWORD_ALLOW.length())));
        }
        
        return password.toString();
    }
}
