/*
 * @(#) Floats.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.regex.Pattern;

/**
 * Utility methods for floats.
 *
 * @author Chris Wareham
 */
public final class Floats {
    /**
     * Decimal digits pattern.
     */
    private static final String DIGITS = "(\\p{Digit}+)";
    /**
     * Hexadecimal digits pattern.
     */
    private static final String HEX_DIGITS = "(\\p{XDigit}+)";
    /**
     * The exponent pattern ('e' or 'E' followed by an optionally signed decimal integer).
     */
    private static final String EXPONENT = "[eE][+-]?" + DIGITS;
    /**
     * The float pattern.
     */
    private static final String FLOAT =
        // Optional sign character
        "[+-]?("
        // "NaN" string
        + "NaN|"
        // "Infinity" string
        + "Infinity|"

        // A decimal floating-point string representing a finite positive
        // number without a leading sign has at most five basic pieces:
        // Digits . Digits ExponentPart FloatTypeSuffix
        //
        // Since this method allows integer-only strings as input
        // in addition to strings of floating-point literals, the
        // two sub-patterns below are simplifications of the grammar
        // productions from section 3.10.2 of
        // The Java Language Specification.

        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
        + "(((" + DIGITS + "(\\.)?(" + DIGITS + "?)(" + EXPONENT + ")?)|"

        // . Digits ExponentPart_opt FloatTypeSuffix_opt
        + "(\\.(" + DIGITS + ")(" + EXPONENT + ")?)|"

        // Hexadecimal strings
        + "(("
        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
        + "(0[xX]" + HEX_DIGITS + "(\\.)?)|"

        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
        + "(0[xX]" + HEX_DIGITS + "?(\\.)" + HEX_DIGITS + ")"

        + ")[pP][+-]?" + DIGITS + "))"
        + "[fFdD]?))";

    /**
     * The radix.
     */
    private static final Pattern FLOAT_PATTERN = Pattern.compile(FLOAT);

    /**
     * Utility class - no public constructor.
     */
    private Floats() {
        // empty
    }

    /**
     * Get whether a string is a valid representation of a float.
     *
     * @param s the string
     * @return whether the string is a valid representation of a float
     */
    public static boolean isFloat(final String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        return FLOAT_PATTERN.matcher(s).matches();
    }
}
