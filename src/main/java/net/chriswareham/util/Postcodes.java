/*
 * @(#) Postcodes.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for parsing and formatting UK postcodes.
 *
 * @author Chris Wareham
 */
public final class Postcodes {
    /**
     * The pattern that matches a full postcode.
     */
    private static final Pattern POSTCODE_PATTERN;
    /**
     * The pattern that matches an outcode.
     */
    private static final Pattern OUTCODE_PATTERN;
    /**
     * The pattern that captures a short outcode.
     */
    private static final Pattern SHORT_OUTCODE_PATTERN;
    /**
     * The pattern that captures a long outcode.
     */
    private static final Pattern LONG_OUTCODE_PATTERN;
    /**
     * The pattern that normalises a postcode.
     */
    private static final Pattern NORMALISE_PATTERN;

    static {
        POSTCODE_PATTERN = Pattern.compile("[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][A-Z-[CIKMOV]]{2}");
        OUTCODE_PATTERN = Pattern.compile("[A-Z]{1,2}[0-9R][0-9A-Z]?");
        SHORT_OUTCODE_PATTERN = Pattern.compile("([A-Z]{1,2}[0-9R][0-9]?).*");
        LONG_OUTCODE_PATTERN = Pattern.compile("([A-Z]{1,2}[0-9R][0-9A-Z]?).*");
        NORMALISE_PATTERN = Pattern.compile("([A-Z]{1,2}[0-9R][0-9A-Z]?)([0-9][A-Z-[CIKMOV]]{2})");
    }

    /**
     * Utility class - no public constructor.
     */
    private Postcodes() {
        // empty
    }

    /**
     * Return whether a string is a valid full postcode. A valid full postcode
     * must conform to the rules for a full postcode BS 7666.
     *
     * @param code the string to validate
     * @return whether the string is a valid full postcode
     */
    public static boolean isFullPostcode(final String code) {
        return POSTCODE_PATTERN.matcher(code).matches();
    }

    /**
     * Return whether a string is a valid postcode. A valid postcode must
     * conform to the rules for a full postcode or outcode as specified in BS
     * 7666.
     *
     * @param code the string to validate
     * @return whether the string is a valid postcode
     */
    public static boolean isPostcode(final String code) {
        return POSTCODE_PATTERN.matcher(code).matches() || OUTCODE_PATTERN.matcher(code).matches();
    }

    /**
     * Return whether a string is a valid outcode. A valid outcode must conform
     * to the rules for an outcode as specified in BS 7666.
     *
     * @param code the string to validate
     * @return whether the string is a valid outcode
     */
    public static boolean isOutcode(final String code) {
        return OUTCODE_PATTERN.matcher(code).matches();
    }

    /**
     * Parse the short form of an outcode from a string. For example, SW1 from
     * SW1A 1AA.
     *
     * @param code the string to parse the outcode from
     * @return the short outcode, or the empty string if it cannot be parsed
     */
    public static String parseShortOutcode(final String code) {
        Matcher matcher = SHORT_OUTCODE_PATTERN.matcher(code);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * Parse the long form of an outcode from a string. For example, SW1A from
     * SW1A 1AA.
     *
     * @param code the string to parse the outcode from
     * @return the long outcode, or the empty string if it cannot be parsed
     */
    public static String parseLongOutcode(final String code) {
        Matcher matcher = LONG_OUTCODE_PATTERN.matcher(code);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * Normalise a postcode by adding a missing space between the incode and
     * outcode.
     *
     * @param code the string to normalise a postcode from
     * @return the normalised postcode, or null if the string is an invalid postcode
     */
    public static String normalisePostcode(final String code) {
        if (isPostcode(code)) {
            return code;
        }
        Matcher matcher = NORMALISE_PATTERN.matcher(code);
        if (matcher.matches()) {
            return matcher.group(1) + ' ' + matcher.group(2);
        }
        return null;
    }
}
