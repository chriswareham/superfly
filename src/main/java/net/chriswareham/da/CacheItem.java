/*
 * @(#) CacheItem.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * This class provides a bean that stores meta data about a cache item.
 *
 * @author Chris Wareham
 */
public class CacheItem {
    /**
     * The size of the cached item in bytes.
     */
    private final long byteCount;
    /**
     * The number of times the cached item has been referenced.
     */
    private long referenceCount;

    /**
     * Construct an instance of cache item class.
     *
     * @param bc the size of the cached item in bytes
     */
    public CacheItem(final long bc) {
        byteCount = bc;
    }

    /**
     * Get the size of the cached item in bytes.
     *
     * @return the size of the cached item in bytes
     */
    public long getByteCount() {
        return byteCount;
    }

    /**
     * Get the number of times the cached item has been referenced.
     *
     * @return the number of times the cached item has been referenced
     */
    public long getReferenceCount() {
        return referenceCount;
    }

    /**
     * Record a reference to the cached item.
     */
    public void referenced() {
        ++referenceCount;
    }
}
