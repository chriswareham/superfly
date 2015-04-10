/*
 * @(#) CacheMBean.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.util.Map;

import net.chriswareham.di.ManagedComponent;

/**
 * This interface is implemented by managed classes that support the storing and
 * fetching of objects from a cache.
 *
 * @author Chris Wareham
 */
public interface CacheMBean extends ManagedComponent {
    /**
     * Get the size of the cache in bytes.
     *
     * @return the size of the cache in bytes
     */
    long getCacheSize();

    /**
     * Get the meta data of the cached items.
     *
     * @return the meta data of the cached items
     */
    Map<Object, CacheItem> getCacheItems();
}
