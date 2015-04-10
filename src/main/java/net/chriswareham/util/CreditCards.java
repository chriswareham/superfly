/*
 * @(#) CreditCards.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.regex.Pattern;

/**
 * Utility methods for validating credit cards.
 *
 * @author Chris Wareham
 */
public final class CreditCards {
    /**
     * The pattern that matches a credit card number.
     */
    private static final Pattern CREDIT_CARD_PATTERN;
    /**
     * The pattern that matches a credit card security code.
     */
    private static final Pattern SECURITY_CODE_PATTERN;

    static {
        CREDIT_CARD_PATTERN = Pattern.compile("\\d{13,19}");
        SECURITY_CODE_PATTERN = Pattern.compile("\\d{3,4}");
    }

    /**
     * Utility class - no public constructor.
     */
    private CreditCards() {
        // empty
    }

    /**
     * Checks whether a string is a valid credit card number. A valid string
     * must consist of 13 to 19 digits, and pass the Luhn algorithm check.
     *
     * Luhn algorithm description:
     *
     * 1. Starting with the second to last digit and moving left, double the
     *    value of all the alternating digits. For any digits that become 10 or
     *    more, add their digits together. For example, 1111 becomes 2121, while
     *    8763 becomes 7733 (from (1+6)7(1+2)3).
     *
     * 2. Add all these digits together. For example, 1111 becomes 2121, then
     *    2+1+2+1 is 6; while 8763 becomes 7733, then 7+7+3+3 is 20.
     *
     * 3. If the total ends in 0 (put another way, if the total modulus 10 is
     *    0), then the number is valid according to the Luhn formula, else it is
     *    not valid. So, 1111 is not valid (as shown above, it comes out to 6),
     *    while 8763 is valid (as shown above, it comes out to 20).
     *
     * @param creditCardNumber the string to validate.
     * @return whether the string is a valid credit card number
     */
    public static boolean isCreditCardNumber(final String creditCardNumber) {
        if (!CREDIT_CARD_PATTERN.matcher(creditCardNumber).matches()) {
            return false;
        }

        int sum = 0;

        boolean alternate = false;
        for (int i = creditCardNumber.length() - 1; i > -1; --i) {
            int n = Integer.parseInt(creditCardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    /**
     * Checks whether a string is a valid credit card security code. A valid
     * string must consist of 3 or 4 digits. Note that a valid code may have
     * leading zeros.
     *
     * @param securityCode the string to validate.
     * @return whether the string is a valid credit card security code
     */
    public static boolean isSecurityCode(final String securityCode) {
        return SECURITY_CODE_PATTERN.matcher(securityCode).matches();
    }
}
