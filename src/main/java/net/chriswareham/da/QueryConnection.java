/*
 * @(#) QueryConnection.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This interface is implemented by data source connection wrappers.
 *
 * @author Chris Wareham
 */
public interface QueryConnection extends AutoCloseable {
    /**
     * Get whether the connection is performing a transaction.
     *
     * @return whether the connection is performing a transaction
     */
    boolean isTransaction();

    /**
     * Get whether the transaction is aborted.
     *
     * @return whether the transaction is aborted
     */
    boolean isTransactionAborted();

    /**
     * Set the transaction to aborted.
     */
    void setTransactionAborted();

    /**
     * Commit the transaction.
     *
     * @throws QueryException if the commit fails
     */
    void commitTransaction() throws QueryException;

    /**
     * Create a statement.
     *
     * @return a statement
     * @throws SQLException if an error occurs
     */
    Statement createStatement() throws SQLException;

    /**
     * Create a prepared statement.
     *
     * @param sql the SQL for the prepared statement
     * @return a prepared statement
     * @throws SQLException if an error occurs
     */
    PreparedStatement prepareStatement(String sql) throws SQLException;

    /**
     * Create a callable statement.
     *
     * @param sql the SQL for the callable statement
     * @return a callable statement
     * @throws SQLException if an error occurs
     */
    CallableStatement prepareCall(String sql) throws SQLException;

    /**
     * Close the connection.
     */
    @Override
    void close();
}
