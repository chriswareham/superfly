/*
 * @(#) QueryCallback.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface is implemented by classes that handle query callbacks.
 *
 * @param <T> the return type of the callback
 * @author Chris Wareham
 */
public interface QueryCallback<T> {
    /**
     * Handle a query callback. If the callback does not need to return data
     * then the return type should be declared as <tt>Void</tt> and a null
     * returned.
     *
     * @param connection the connection
     * @param resultSet the result set
     * @return any returned data
     * @throws SQLException if a data access error occurs
     * @throws QueryException if a nested error occurs
     */
    T callback(QueryConnection connection, ResultSet resultSet) throws SQLException, QueryException;
}
