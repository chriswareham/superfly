/*
 * @(#) Passwords.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Random;

/**
 * Utility methods for generating passwords.
 *
 * @author Chris Wareham
 */
public final class Passwords {
    /**
     * The random number generator.
     */
    private static final Random RANDOM;
    /**
     * The characters to be used when generating passwords.
     */
    private static final String PASSWORD_CHARACTERS;
    /**
     * The length of the string of characters to be used when generating passwords.
     */
    private static final int PASSWORD_CHARACTERS_LENGTH;

    static {
        RANDOM = new Random();
        PASSWORD_CHARACTERS = "_0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        PASSWORD_CHARACTERS_LENGTH = PASSWORD_CHARACTERS.length();
    }

    /**
     * Utility class - no public constructor.
     */
    private Passwords() {
        // empty
    }

    /**
     * Generates a password.
     *
     * @param length the desired length of the password
     * @return the password
     */
    public static synchronized String generatePassword(final int length) {
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            buf.append(PASSWORD_CHARACTERS.charAt(RANDOM.nextInt(PASSWORD_CHARACTERS_LENGTH)));
        }
        return buf.toString();
    }
}
