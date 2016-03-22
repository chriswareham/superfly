/*
 * @(#) Booleans.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

/**
 * Utility methods for booleans.
 *
 * @author Chris Wareham
 */
public final class Booleans {
    /**
     * Utility class - no public constructor.
     */
    private Booleans() {
        // empty
    }

    /**
     * Get whether a string is a valid representation of a boolean.
     *
     * @param s the string
     * @return whether the string is a valid representation of a boolean
     */
    public static boolean isBoolean(final String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        return "true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s);
    }
}
