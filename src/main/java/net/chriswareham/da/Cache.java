/*
 * @(#) Cache.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * This interface is implemented by classes that support the storing and
 * fetching of objects from a cache.
 *
 * @param <I> the type of object that is used to identify cached objects
 * @param <T> the type of cached objects
 * @author Chris Wareham
 */
public interface Cache<I, T> {
    /**
     * Fetches an object.
     *
     * @param id the id of the object to fetch
     * @return the object, or null if it is not cached
     */
    T fetch(I id);

    /**
     * Stores an object.
     *
     * @param id the id of the object to store
     * @param obj the object to store
     */
    void store(I id, T obj);

    /**
     * Flushes and stores an object.
     *
     * @param id the id of the object to flush and store
     * @param obj the object to flush and store
     */
    void flushAndStore(I id, T obj);

    /**
     * Flushes an object.
     *
     * @param id the id of the object to flush
     */
    void flush(I id);

    /**
     * Flushes all objects from the cache.
     */
    void flush();
}
