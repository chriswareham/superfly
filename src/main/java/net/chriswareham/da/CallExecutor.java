/*
 * @(#) CallExecutor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * This interface is implemented by classes that execute stored procedure calls.
 *
 * @author Chris Wareham
 */
public interface CallExecutor {
    /**
     * Execute a stored procedure call.
     *
     * This method is expected to be implemented as an inner anonymous class,
     * somewhat like a closure, with the variables to populate the call
     * available in the scope that encloses the class.
     *
     * @param statement the statement to populate and execute
     * @throws SQLException if an error occurs
     */
    void call(CallableStatement statement) throws SQLException;
}
