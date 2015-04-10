/*
 * @(#) LogDriver.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.logdriver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class provides a logging driver.
 */
public class LogDriver implements Driver {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LogDriver.class);

    static {
        try {
            DriverManager.registerDriver(new LogDriver());
        } catch (SQLException exception) {
            LOGGER.error("Couldn't register driver", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection connect(final String url, final Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }
        try {
            String str = parseUrl(url);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading class for driver " + str);
            }
            return new LogConnection(DriverManager.getConnection(str, info));
        } catch (ClassNotFoundException exception) {
            throw new SQLException("Couldn't load class for driver", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean acceptsURL(final String url) throws SQLException {
        return url.startsWith("jdbc:log");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMajorVersion() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinorVersion() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Parse a JDBC connection URL and return the connection string.
     *
     * @param url the URL to parse
     * @return the JBDC connection string
     * @throws ClassNotFoundException if the JDBC driver class cannot be found
     */
    private String parseUrl(final String url) throws ClassNotFoundException {
        int i = url.indexOf(":log:") + ":log:".length();
        int j = url.indexOf(':', i);
        String className = url.substring(i, j);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Real JDBC driver class: " + className);
        }
        Class.forName(className);
        String connStr = url.replaceFirst(":log:" + className, "");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Real JDBC connection string: " + connStr);
        }
        return connStr;
    }
}
