/*
 * @(#) QueryServiceImpl.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * This class implements methods that execute queries.
 *
 * @author Chris Wareham
 */
public class QueryServiceImpl implements QueryService {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(QueryServiceImpl.class);

    /**
     * The database connection source.
     */
    private DataSource dataSource;

    /**
     * Set the database connection source.
     *
     * @param ds the database connection source
     */
    public void setDataSource(final DataSource ds) {
        dataSource = ds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryConnection getQueryConnection(final boolean t) throws QueryException {
        try {
            Connection connection = dataSource.getConnection();
            if (t) {
                connection.setAutoCommit(false);
            }
            return new QueryConnectionImpl(connection, t);
        } catch (SQLException exception) {
            throw new QueryException("Failed to get data source connection", initCauses(exception));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> listQuery(final String query, final QueryCallback<T> callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return listQuery(connection, query, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> listQuery(final QueryConnection connection, final String query, final QueryCallback<T> callback) throws QueryException {
        List<T> results = new ArrayList<>();

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                T result = callback.callback(connection, resultSet);
                if (result != null) {
                    results.add(result);
                }
            }

            resultSet.close();
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("List query error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("listQuery(): read:[" + results.size() + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> listQuery(final String query, final QueryExecutor executor, final QueryCallback<T> callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return listQuery(connection, query, executor, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> listQuery(final QueryConnection connection, final String query, final QueryExecutor executor, final QueryCallback<T> callback) throws QueryException {
        List<T> results = new ArrayList<>();

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = executor.query(statement);

            while (resultSet.next()) {
                T result = callback.callback(connection, resultSet);
                if (result != null) {
                    results.add(result);
                }
            }

            resultSet.close();
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("List query error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("listQuery(): read:[" + results.size() + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T query(final String query, final QueryCallback<T> callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return query(connection, query, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T query(final QueryConnection connection, final String query, final QueryCallback<T> callback) throws QueryException {
        T result = null;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                result = callback.callback(connection, resultSet);
            }

            resultSet.close();
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Query error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("query(): read:[" + (result != null) + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T query(final String query, final QueryExecutor executor, final QueryCallback<T> callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return query(connection, query, executor, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T query(final QueryConnection connection, final String query, final QueryExecutor executor, final QueryCallback<T> callback) throws QueryException {
        T result = null;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = executor.query(statement);

            if (resultSet.next()) {
                result = callback.callback(connection, resultSet);
            }

            resultSet.close();
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Query error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("query(): read:[" + (result != null) + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final String update) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return update(connection, update);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final QueryConnection connection, final String update) throws QueryException {
        int updated = 0;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try (Statement statement = connection.createStatement()) {
            updated = statement.executeUpdate(update);
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Update error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("update(): updated:[" + updated + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final String update, final UpdateExecutor executor) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return update(connection, update, executor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final QueryConnection connection, final String update, final UpdateExecutor executor) throws QueryException {
        int updated = 0;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            PreparedStatement statement = connection.prepareStatement(update);

            updated = executor.update(statement);
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Update error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("update(): updated:[" + updated + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final String update, final UpdateCallback callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return update(connection, update, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final QueryConnection connection, final String update, final UpdateCallback callback) throws QueryException {
        int updated = 0;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try (Statement statement = connection.createStatement()) {

            updated = statement.executeUpdate(update);

            callback.callback(connection);
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Update error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("update(): updated:[" + updated + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final String update, final UpdateExecutor executor, final UpdateCallback callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return update(connection, update, executor, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final QueryConnection connection, final String update, final UpdateExecutor executor, final UpdateCallback callback) throws QueryException {
        int updated = 0;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            PreparedStatement statement = connection.prepareStatement(update);

            updated = executor.update(statement);

            callback.callback(connection);
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Update error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("update(): updated:[" + updated + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void call(final String call) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            call(connection, call);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void call(final QueryConnection connection, final String call) throws QueryException {
        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            CallableStatement statement = connection.prepareCall(call);

            statement.execute();
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Call error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("call(): elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void call(final String call, final CallExecutor executor) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            call(connection, call, executor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void call(final QueryConnection connection, final String call, final CallExecutor executor) throws QueryException {
        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            CallableStatement statement = connection.prepareCall(call);

            executor.call(statement);
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Call error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("call(): elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T call(final String call, final CallCallback<T> callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return call(connection, call, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T call(final QueryConnection connection, final String call, final CallCallback<T> callback) throws QueryException {
        T result = null;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            CallableStatement statement = connection.prepareCall(call);

            statement.execute();

            result = callback.callback(connection, statement);

        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Call error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("call(): read:[" + (result != null) + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T call(final String call, final CallExecutor executor, final CallCallback<T> callback) throws QueryException {
        try (QueryConnection connection = getQueryConnection(false)) {
            return call(connection, call, executor, callback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T call(final QueryConnection connection, final String call, final CallExecutor executor, final CallCallback<T> callback) throws QueryException {
        T result = null;

        long timestamp = 0;

        if (LOGGER.isTraceEnabled()) {
            timestamp = System.currentTimeMillis();
        }

        try {
            CallableStatement statement = connection.prepareCall(call);

            executor.call(statement);

            result = callback.callback(connection, statement);
        } catch (SQLException exception) {
            if (connection.isTransaction()) {
                connection.setTransactionAborted();
            }
            throw new QueryException("Call error", initCauses(exception));
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("call(): read:[" + (result != null) + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }

        return result;
    }

    /**
     * Set exception causes. The JDBC exceptions implement their own chaining
     * mechanism that predates the one added to the <code>Throwable</code> class
     * with Java 1.4.
     *
     * @param exception the exception to set the causes for
     * @return the exception with the causes set
     */
    private static SQLException initCauses(final SQLException exception) {
        SQLException e = exception;
        while (e.getCause() == null && e.getNextException() != null) {
            e.initCause(e.getNextException());
            e = e.getNextException();
        }
        return exception;
    }

    /**
     * This class implements a data source connection wrapper.
     */
    private static class QueryConnectionImpl implements QueryConnection {
        /**
         * The wrapped data source connection.
         */
        private final Connection connection;
        /**
         * Whether the connection is performing a transaction.
         */
        private final boolean transaction;
        /**
         * Whether the transaction is aborted.
         */
        private boolean transactionAborted;
        /**
         * The cached prepared statements.
         */
        private Map<String, PreparedStatement> preparedCache = Collections.emptyMap();
        /**
         * The cached prepared statements.
         */
        private Map<String, CallableStatement> callableCache = Collections.emptyMap();

        /**
         * Construct an instance of the data source connection wrapper.
         *
         * @param c the wrapped data source connection
         * @param t whether the connection is to be used for a transaction
         */
        private QueryConnectionImpl(final Connection c, final boolean t) {
            connection = c;
            transaction = t;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isTransaction() {
            return transaction;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isTransactionAborted() {
            return transactionAborted;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setTransactionAborted() {
            transactionAborted = true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void commitTransaction() throws QueryException {
            if (transaction && !transactionAborted) {
                try {
                    connection.commit();
                } catch (SQLException exception) {
                    throw new QueryException("Failed to commit transaction", exception);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PreparedStatement prepareStatement(final String sql) throws SQLException {
            PreparedStatement statement = preparedCache.get(sql);
            if (statement == null) {
                if (preparedCache.isEmpty()) {
                    preparedCache = new HashMap<>();
                }
                statement = connection.prepareStatement(sql);
                preparedCache.put(sql, statement);
            }
            return statement;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CallableStatement prepareCall(final String sql) throws SQLException {
            CallableStatement statement = callableCache.get(sql);
            if (statement == null) {
                if (callableCache.isEmpty()) {
                    callableCache = new HashMap<>();
                }
                statement = connection.prepareCall(sql);
                callableCache.put(sql, statement);
            }
            return statement;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() {
            if (transaction && transactionAborted) {
                try {
                    connection.rollback();
                } catch (SQLException exception) {
                    LOGGER.error("close(): failed to rollback aborted transaction", exception);
                }
            }
            closePreparedStatements();
            closeCallableStatements();
            try {
                connection.close();
            } catch (SQLException exception) {
                LOGGER.error("close(): failed to close connection", exception);
            }
        }

        /**
         * Close the cached prepared statements.
         */
        private void closePreparedStatements() {
            for (PreparedStatement statement : preparedCache.values()) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    LOGGER.warn("closePreparedStatements(): failed to close statement", exception);
                }
            }
        }

        /**
         * Close the cached callable statements.
         */
        private void closeCallableStatements() {
            for (CallableStatement statement : callableCache.values()) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    LOGGER.warn("closeCallableStatements(): failed to close statement", exception);
                }
            }
        }
    }
}
