/*
 * @(#) Telephones.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for parsing and formatting telephone numbers.
 *
 * @author Chris Wareham
 */
public final class Telephones {
    /**
     * The pattern that splits telephone numbers.
     */
    private static final Pattern SPLIT_PATTERN;
    /**
     * The pattern that matches non-numeric characters.
     */
    private static final Pattern NON_NUMERIC_PATTERN;
    /**
     * The map of patterns and replacement strings for telephone numbers.
     */
    private static final Map<Pattern, String> TELEPHONE_PATTERNS;

    static {
        SPLIT_PATTERN = Pattern.compile("[\\/,|]");
        NON_NUMERIC_PATTERN = Pattern.compile("[^0-9]");
        TELEPHONE_PATTERNS = new LinkedHashMap<>(4);
        // Geographic codes
        TELEPHONE_PATTERNS.put(Pattern.compile("^((440?)|0)(1)([0-9]{2})([0-9]{3})([0-9]{3,4})"), "+44 (0)$3$4 $5 $6");
        // Geographic codes (high density)
        TELEPHONE_PATTERNS.put(Pattern.compile("^((440?)|0)(2[0-9])([0-9]{4})([0-9]{4})"), "+44 (0)$3 $4 $5");
        // Non-geographic, freephone and premium rate codes
        TELEPHONE_PATTERNS.put(Pattern.compile("^((440?)|0)([389])([0-9]{2})([0-9]{4,})"), "0$3$4 $5");
        // Pager and mobile codes
        TELEPHONE_PATTERNS.put(Pattern.compile("^((440?)|0)(7)([0-9]{2})([0-9]{3})([0-9]{4})"), "+44 (0)$3$4 $5 $6");
    }

    /**
     * Utility class - no public constructor.
     */
    private Telephones() {
        // empty
    }

    /**
     * Return whether a string is a valid telephone number. a valid telephone
     * number must consist of 9 or 10 digits, excluding the 0 prefix.
     *
     * @param telephone the string to validate
     * @return whether the string is a valid telephone number
     */
    public static boolean isTelephone(final String telephone) {
        String str = NON_NUMERIC_PATTERN.matcher(telephone).replaceAll("");

        for (Pattern pattern : TELEPHONE_PATTERNS.keySet()) {
            if (pattern.matcher(str).matches()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Parse a telephone number from a string. Non-numeric characters are
     * removed, and a valid telephone number should then consist of 9 or 10
     * digits, excluding the 0 prefix.
     *
     * @param telephone the string number to validate
     * @return the telephone number, or the empty string if it is invalid
     */
    public static String parseTelephone(final String telephone) {
        String str = telephone;
        String[] strs = SPLIT_PATTERN.split(str);
        if (strs.length > 0) {
            str = strs[0];
        }

        str = NON_NUMERIC_PATTERN.matcher(str).replaceAll("");

        for (Pattern pattern : TELEPHONE_PATTERNS.keySet()) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()) {
                return matcher.replaceFirst(TELEPHONE_PATTERNS.get(pattern));
            }
        }

        return "";
    }
}
