/*
 * @(#) UpdateExecutor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This interface is implemented by classes that execute updates.
 *
 * @author Chris Wareham
 */
public interface UpdateExecutor {
    /**
     * Execute an update.
     *
     * This method is expected to be implemented as an inner anonymous class,
     * somewhat like a closure, with the variables to populate the statement
     * available in the scope that encloses the class.
     *
     * @param statement the statement to populate and execute
     * @return the number of rows updated
     * @throws SQLException if an error occurs
     */
    int update(PreparedStatement statement) throws SQLException;
}
