/*
 * @(#) Hash.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for hashing strings.
 *
 * @author Chris Wareham
 */
public final class Hash {
    /**
     * Hash algorithm.
     */
    private static final String HASH_ALGORITHM = "MD5";
    /**
     * Hex digits.
     */
    private static final char[] HEX_DIGITS = {'0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' , '9' , 'a' , 'b' , 'c' , 'd' , 'e' , 'f'};

    /**
     * Utility class - no public constructor.
     */
    private Hash() {
        // empty
    }

    /**
     * Encode a string into a hashed hex string.
     *
     * @param str the string to encode
     * @return the hashed hex string
     */
    public static String hash(final String str) {
        return hash(str.getBytes());
    }

    /**
     * Encode an array of bytes into a hashed hex string.
     *
     * @param bytes the array of bytes to encode
     * @return the hashed hex string
     */
    public static String hash(final byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] digest = messageDigest.digest(bytes);

            int len = digest.length;
            StringBuilder buf = new StringBuilder(2 * len);
            for (int i = 0; i < len; ++i) {
                buf.append(HEX_DIGITS[(0xF0 & digest[i]) >>> 4]);
                buf.append(HEX_DIGITS[0x0F & digest[i]]);
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
