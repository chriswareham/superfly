/*
 * @(#) Locales.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for validating and parsing locales.
 *
 * @author Chris Wareham
 */
public final class Locales {
    /** Regular expression that matches a locale. */
    private static final Pattern LOCALE_PATTERN = Pattern.compile("([a-z]{2})_([A-Z]{2})");

    /**
     * Utility class - no public constructor.
     */
    private Locales() {
        // empty
    }

    /**
     * Return whether a string is a valid locale.
     *
     * @param str the string to validate
     * @return whether the string is a valid locale
     */
    public static boolean isLocale(final String str) {
        if (str != null) {
            return LOCALE_PATTERN.matcher(str).matches();
        }
        return false;
    }

    /**
     * Parse a locale from a string.
     *
     * @param str the string to parse
     * @return a locale, or null if one cannot be parsed from the string
     */
    public static Locale parseLocale(final String str) {
        if (str != null) {
            Matcher matcher = LOCALE_PATTERN.matcher(str);
            if (matcher.matches()) {
                return new Locale(matcher.group(1), matcher.group(2));
            }
        }
        return null;
    }
}
