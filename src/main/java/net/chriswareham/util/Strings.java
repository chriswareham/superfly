/*
 * @(#) Strings.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Collection;

/**
 * Utility methods for strings.
 *
 * @author Chris Wareham
 */
public final class Strings {
    /**
     * Utility class - no public constructor.
     */
    private Strings() {
        // empty
    }

    /**
     * Get whether a string is null or empty.
     *
     * @param s the string
     * @return whether the string is null or empty
     */
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Concatenate a collection of strings.
     *
     * @param strs the collection of strings to concatenate
     * @param sep the separator
     * @return the concatenated strings
     */
    public static String concat(final Collection<String> strs, final String sep) {
        String str = "";
        if (!strs.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            for (String s : strs) {
                if (buf.length() > 0) {
                    buf.append(sep);
                }
                buf.append(s);
            }
            str = buf.toString();
        }
        return str;
    }

    /**
     * Find the longest common substring for two strings.
     *
     * @param str1 the first string
     * @param str2 the second string
     * @return the longest common substring for the two strings
     */
    public static String longestCommonSubstring(final String str1, final String str2) {
        int start = 0;
        int len = 0;

        for (int i = 0; i < str1.length(); ++i) {
            for (int j = 0; j < str2.length(); ++j) {
                int n = 0;

                while (str1.charAt(i + n) == str2.charAt(j + n)) {
                    ++n;
                    if ((i + n >= str1.length()) || (j + n >= str2.length())) {
                        break;
                    }
                }

                if (n > len) {
                    len = n;
                    start = i;
                }
            }
        }

        return str1.substring(start, start + len);
    }

    /**
     * Reformat a string to leading caps.
     *
     * @param str the string to reformat
     * @return the reformatted string
     */
    public static String toLeadingCaps(final String str) {
        int len = str.length();

        StringBuilder buf = new StringBuilder(len);

        boolean prevIsLetter = false;

        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            if (Character.isLetter(c)) {
                if (prevIsLetter) {
                    c = Character.toLowerCase(c);
                } else {
                    c = Character.toUpperCase(c);
                    prevIsLetter = true;
                }
            } else {
                prevIsLetter = false;
            }
            buf.append(c);
        }

        return buf.toString();
    }

    /**
     * Get whether a string contains any member of a set of strings.
     *
     * @param str the string
     * @param strs the set of strings
     * @return whether the string contains any member of the set of strings
     */
    public static boolean containsAny(final String str, final Collection<String> strs) {
        for (String s : strs) {
            if (str.contains(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get whether a string is a member of a set of strings.
     *
     * @param str the string
     * @param strs the set of strings
     * @return whether the string is a member of the set of strings
     */
    public static boolean equalsAny(final String str, final Collection<String> strs) {
        for (String s : strs) {
            if (str.equals(s)) {
                return true;
            }
        }

        return false;
    }
}
