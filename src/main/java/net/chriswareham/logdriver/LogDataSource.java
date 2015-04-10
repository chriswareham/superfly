/*
 * @(#) LogDataSource.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.logdriver;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * This class provides a logging data source.
 */
public class LogDataSource implements DataSource {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LogDataSource.class);

    /**
     * The wrapped data source.
     */
    private final DataSource dataSource;

    /**
     * Create an instance of the logging data source.
     *
     * @param ds the data source to wrap
     */
    public LogDataSource(final DataSource ds) {
        dataSource = ds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        return new LogConnection(dataSource.getConnection());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        return new LogConnection(dataSource.getConnection(username, password));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Setting login timeout to " + seconds + " seconds");
        }
        dataSource.setLoginTimeout(seconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Setting log writer to " + out);
        }
        dataSource.setLogWriter(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(dataSource.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(dataSource.getClass())) {
            return (T) dataSource;
        }
        if (isWrapperFor(iface)) {
            return dataSource.unwrap(iface);
        }
        throw new SQLException(getClass().getName() + " is not a wrapper for " + iface);
    }
}
