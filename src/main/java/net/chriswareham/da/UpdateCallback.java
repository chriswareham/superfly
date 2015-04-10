/*
 * @(#) UpdateCallback.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * This interface is implemented by classes that handle update callbacks.
 *
 * @author Chris Wareham
 */
public interface UpdateCallback {
    /**
     * Handle an update callback.
     *
     * @param connection the connection
     * @throws QueryException if a nested error occurs
     */
    void callback(QueryConnection connection) throws QueryException;
}
