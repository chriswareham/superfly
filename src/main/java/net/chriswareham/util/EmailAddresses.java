/*
 * @(#) EmailAddresses.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for validating email addresses conforming to RFC 822.
 *
 * @author Chris Wareham
 */
public final class EmailAddresses {
    /**
     * Regular expression that matches one or more ASCII characters.
     */
    private static final String ASCII_REGEX = "\\p{ASCII}+";
    /**
     * Regular expression that matches two or more tokens separated by an '@' character.
     */
    private static final String EMAIL_REGEX = "\\s*?(.+)@(.+?)\\s*";

    /**
     * Regular expression that matches an IP address enclosed in square brackets.
     */
    private static final String DOMAIN_IP_REGEX = "\\[(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\]";
    /**
     * Regular expression that matches a sub domain.
     */
    private static final String DOMAIN_SUB_REGEX = "\\p{Alnum}(?>[\\p{Alnum}-]*\\p{Alnum})*";
    /**
     * Regular expression that matches a top level domain.
     */
    private static final String DOMAIN_TLD_REGEX = "\\p{Alpha}{2,6}";
    /**
     * Regular expression that matches a domain name.
     */
    private static final String DOMAIN_NAME_REGEX = "(?:" + DOMAIN_SUB_REGEX + "\\.)*" + "(" + DOMAIN_TLD_REGEX + ")";

    /**
     * Regular expression that matches special characters in a user name.
     */
    private static final String SPECIAL_CHARS = "\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]";
    /**
     * Regular expression that matches valid characters in a user name.
     */
    private static final String VALID_CHARS = "[^\\s" + SPECIAL_CHARS + "]";
    /**
     * Regular expression that matches quoted characters in a username.
     */
    private static final String QUOTED_CHARS = "(\"[^\"]*\")";
    /**
     * Regular expression that matches a word component in a user name.
     */
    private static final String WORD = "((" + VALID_CHARS + "|')+|" + QUOTED_CHARS + ")";
    /**
     * Regular expression that matches a user name.
     */
    private static final String USER_REGEX = "\\s*" + WORD + "(\\." + WORD + ")*";

    /**
     * Pattern that matches one or more ASCII characters.
     */
    private static final Pattern ASCII_PATTERN = Pattern.compile(ASCII_REGEX);
    /**
     * Pattern that matches two or more tokens separated by an '@' character.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    /**
     * Pattern that matches an IP address enclosed in square brackets.
     */
    private static final Pattern DOMAIN_IP_PATTERN = Pattern.compile(DOMAIN_IP_REGEX);
    /**
     * Pattern that matches a domain name.
     */
    private static final Pattern DOMAIN_NAME_PATTERN = Pattern.compile(DOMAIN_NAME_REGEX);
    /**
     * Pattern that matches a user name.
     */
    private static final Pattern USER_PATTERN = Pattern.compile(USER_REGEX);

    /**
     * Utility class - no public constructor.
     */
    private EmailAddresses() {
        // empty
    }

    /**
     * Return whether a string is a valid email address. A valid email address
     * must conform to the rules for remote addresses as specified in RFC 2822.
     *
     * @param emailAddress the string to validate
     * @return whether the string is a valid email address
     */
    public static boolean isEmailAddress(final String emailAddress) {
        if (emailAddress.endsWith(".")) {
            return false;
        }

        if (!ASCII_PATTERN.matcher(emailAddress).matches()) {
            return false;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(emailAddress);

        if (!matcher.matches()) {
            return false;
        }

        if (!isValidUser(matcher.group(1))) {
            return false;
        }

        if (!isValidDomain(matcher.group(2))) {
            return false;
        }

        return true;
    }

    /**
     * Return whether the domain component of an email address is valid.
     *
     * @param domain the domain to validate
     * @return whether the domain is valid
     */
    private static boolean isValidDomain(final String domain) {
        Matcher matcher = DOMAIN_IP_PATTERN.matcher(domain);

        if (!matcher.matches()) {
            return DOMAIN_NAME_PATTERN.matcher(domain).matches();
        }

        for (int i = 1; i < 5; ++i) {
            String octet = matcher.group(i);

            try {
                int dec = Integer.parseInt(octet);

                if (dec < 0 || dec > 255) {
                    return false;
                }
            } catch (NumberFormatException exception) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return whether the user component of an email address is valid.
     *
     * @param user the user to validate
     * @return whether the user is valid
     */
    private static boolean isValidUser(final String user) {
        return USER_PATTERN.matcher(user).matches();
    }
}
