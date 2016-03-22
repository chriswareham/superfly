/*
 * @(#) Integers.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

/**
 * Utility methods for integers.
 *
 * @author Chris Wareham
 */
public final class Integers {
    /**
     * The radix.
     */
    private static final int RADIX = 10;

    /**
     * Utility class - no public constructor.
     */
    private Integers() {
        // empty
    }

    /**
     * Get whether a string is a valid representation of an integer.
     *
     * @param s the string
     * @return whether the string is a valid representation of an integer
     */
    public static boolean isInteger(final String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        int i = 0;
        int limit = -Integer.MAX_VALUE;

        char firstChar = s.charAt(0);

        if (firstChar < '0') {
            // Possible leading "+" or "-"
            if (firstChar == '-') {
                limit = Integer.MIN_VALUE;
            } else if (firstChar != '+') {
                return false;
            }

            if (s.length() == 1) {
                // Cannot have lone "+" or "-"
                return false;
            }

            i++;
        }

        int multmin = limit / RADIX;
        int result = 0;

        while (i < s.length()) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            int digit = Character.digit(s.charAt(i++), RADIX);

            if (digit < 0) {
                return false;
            }

            if (result < multmin) {
                return false;
            }

            result *= RADIX;

            if (result < limit + digit) {
                return false;
            }

            result -= digit;
        }

        return true;
    }
}
