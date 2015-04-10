/*
 * @(#) IdFetcher.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * This interface is implemented by classes that fetch ids.
 *
 * @param <I> the type of object that is an id
 * @param <T> the type of object to fetch an id from
 * @author Chris Wareham
 */
public interface IdFetcher<I, T> {
    /**
     * Fetch the id of an object.
     *
     * @param obj the object to fetch the id from
     * @return the id
     */
    I fetchId(T obj);
}
