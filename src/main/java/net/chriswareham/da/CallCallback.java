/*
 * @(#) CallCallback.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * This interface is implemented by classes that handle stored procedure
 * callbacks.
 *
 * @param <T> the return type of the callback
 * @author Chris Wareham
 */
public interface CallCallback<T> {
    /**
     * Handle a stored procedure callback. If the callback does not need to
     * return data then the return type should be declared as <tt>Void</tt> and
     * a null returned.
     *
     * @param connection the connection
     * @param statement the statement
     * @return any returned data
     * @throws SQLException if a data access error occurs
     * @throws QueryException if a nested error occurs
     */
    T callback(QueryConnection connection, CallableStatement statement) throws SQLException, QueryException;
}
