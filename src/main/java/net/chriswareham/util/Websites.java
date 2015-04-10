/*
 * @(#) Websites.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

/**
 * Utility methods for validating website addresses.
 *
 * @author Chris Wareham
 */
public final class Websites {
    /**
     * Utility class - no public constructor.
     */
    private Websites() {
        // empty
    }

    /**
     * Return whether a string is a valid website address. A valid website
     * address must start with either "http://" or "https://".
     *
     * @param websiteAddress the string to validate
     * @return whether the string is a valid website address
     */
    public static boolean isWebsiteAddress(final String websiteAddress) {
        return websiteAddress.startsWith("http://") || websiteAddress.startsWith("https://");
    }
}
