/*
 * @(#) DataSourceImpl.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DataSourceConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import net.chriswareham.di.ComponentException;
import net.chriswareham.di.LifecycleComponent;
import net.chriswareham.logdriver.LogDataSource;

/**
 * This class provides a pooling data source wrapper.
 *
 * @author Chris Wareham
 */
public class DataSourceImpl implements DataSource, LifecycleComponent {
    /**
     * The source for database connections.
     */
    private DataSource dataSource;
    /**
     * The minimum number of idle connections in the pool.
     */
    private int minIdle;
    /**
     * The maximum number of idle connections in the pool.
     */
    private int maxIdle;
    /**
     * The maximum number of active connections in the pool.
     */
    private int maxActive;
    /**
     * The maximum number of milliseconds to block when the pool is exhausted.
     */
    private long maxWait;
    /**
     * The database connection pool.
     */
    private PoolingDataSource poolingDataSource;

    /**
     * Set the source for database connections.
     *
     * @param ds the source for database connections
     */
    public void setDataSource(final DataSource ds) {
        dataSource = ds;
    }

    /**
     * Set the minimum number of idle connections in the pool.
     *
     * @param m the minimum number of idle connections in the pool
     */
    public void setMinIdle(final int m) {
        minIdle = m;
    }

    /**
     * Set the maximum number of idle connections in the pool.
     *
     * @param m the maximum number of idle connections in the pool
     */
    public void setMaxIdle(final int m) {
        maxIdle = m;
    }

    /**
     * Set the maximum number of active connections in the pool.
     *
     * @param m the maximum number of active connections in the pool
     */
    public void setMaxActive(final int m) {
        maxActive = m;
    }

    /**
     * Set the maximum number of milliseconds to block when the pool is exhausted.
     *
     * @param m the maximum number of milliseconds to block when the pool is exhausted
     */
    public void setMaxWait(final long m) {
        maxWait = m;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws ComponentException {
        try {
            GenericObjectPool objectPool = new GenericObjectPool();
            objectPool.setMinIdle(minIdle);
            objectPool.setMaxIdle(maxIdle);
            objectPool.setMaxActive(maxActive);
            objectPool.setMaxWait(maxWait);
            if (maxActive > 0) {
                objectPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
            } else {
                objectPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
            }
            objectPool.setTimeBetweenEvictionRunsMillis(900000L);
            objectPool.setSoftMinEvictableIdleTimeMillis(300000L);
            objectPool.setNumTestsPerEvictionRun(-2);

            DataSourceConnectionFactory connectionFactory = new DataSourceConnectionFactory(new LogDataSource(dataSource));
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, objectPool, null, "SELECT TRUE", false, true);
            poolingDataSource = new PoolingDataSource(objectPool);
        } catch (RuntimeException exception) {
            throw new ComponentException("Error creating database connection pool", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        poolingDataSource = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        return poolingDataSource.getConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        return poolingDataSource.getConnection(username, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return poolingDataSource.getLoginTimeout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        poolingDataSource.setLoginTimeout(seconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return poolingDataSource.getLogWriter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        poolingDataSource.setLogWriter(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
