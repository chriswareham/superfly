/*
 * @(#) LogStatement.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.logdriver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * This class provides a logging JDBC statement.
 */
public class LogStatement implements Statement {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LogStatement.class);

    /**
     * The wrapped statement.
     */
    private final Statement statement;
    /**
     * The connection.
     */
    private final Connection connection;

    /**
     * Construct an instance of the logging JDBC statement.
     *
     * @param s the wrapped statement
     * @param c the connection
     */
    public LogStatement(final Statement s, final Connection c) {
        statement = s;
        connection = c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(final String sql) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing: " + sql);
        }
        return statement.execute(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing: " + sql);
        }
        return statement.execute(sql, autoGeneratedKeys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing: " + sql);
        }
        return statement.execute(sql, columnIndexes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing: " + sql);
        }
        return statement.execute(sql, columnNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing query: " + sql);
        }
        return statement.executeQuery(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdate(final String sql) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing update: " + sql);
        }
        return statement.executeUpdate(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing update: " + sql);
        }
        return statement.executeUpdate(sql, autoGeneratedKeys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing update: " + sql);
        }
        return statement.executeUpdate(sql, columnIndexes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing update: " + sql);
        }
        return statement.executeUpdate(sql, columnNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBatch(final String sql) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding to batch: " + sql);
        }
        statement.addBatch(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] executeBatch() throws SQLException {
        LOGGER.debug("Executing batch");
        return statement.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearBatch() throws SQLException {
        statement.clearBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SQLException {
        statement.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxFieldSize() throws SQLException {
        return statement.getMaxFieldSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        statement.setMaxFieldSize(max);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxRows() throws SQLException {
        return statement.getMaxRows();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxRows(final int max) throws SQLException {
        statement.setMaxRows(max);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
        statement.setEscapeProcessing(enable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQueryTimeout() throws SQLException {
        return statement.getQueryTimeout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        statement.setQueryTimeout(seconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel() throws SQLException {
        statement.cancel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return statement.getWarnings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearWarnings() throws SQLException {
        statement.clearWarnings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorName(final String name) throws SQLException {
        statement.setCursorName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
        return statement.getResultSet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUpdateCount() throws SQLException {
        return statement.getUpdateCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getMoreResults() throws SQLException {
        return statement.getMoreResults();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        statement.setFetchDirection(direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFetchDirection() throws SQLException {
        return statement.getFetchDirection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        statement.setFetchSize(rows);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFetchSize() throws SQLException {
        return statement.getFetchSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResultSetConcurrency() throws SQLException {
        return statement.getResultSetConcurrency();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResultSetType() throws SQLException {
        return statement.getResultSetType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getMoreResults(final int current) throws SQLException {
        return statement.getMoreResults(current);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return statement.getGeneratedKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResultSetHoldability() throws SQLException {
        return statement.getResultSetHoldability();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed() throws SQLException {
        return statement.isClosed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        statement.setPoolable(poolable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPoolable() throws SQLException {
        return statement.isPoolable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeOnCompletion() throws SQLException {
        statement.closeOnCompletion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return statement.isCloseOnCompletion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(statement.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(statement.getClass())) {
            return (T) statement;
        }
        if (isWrapperFor(iface)) {
            return statement.unwrap(iface);
        }
        throw new SQLException(getClass().getName() + " is not a wrapper for " + iface);
    }
}