/*
 * @(#) Heap.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import org.apache.log4j.Logger;

/**
 * Utility methods for logging heap usage.
 *
 * @author Chris Wareham
 */
public final class Heap {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Heap.class);

    /**
     * Utility class - no public constructor.
     */
    private Heap() {
        // empty
    }

    /**
     * Log heap usage.
     */
    public static void log() {
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        LOGGER.info("Memory max:[" + max + "] allocated:[" + total + "] used:[" + (total - free) + "] free:[" + free + "]");
    }
}
