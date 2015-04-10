/*
 * @(#) QueryService.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.util.List;

/**
 * This interface is implemented by classes that execute queries.
 *
 * @author Chris Wareham
 */
public interface QueryService {
    /**
     * Get a data source connection wrapper.
     *
     * @param t whether the connection is to be used for a transaction
     * @return a data source connection wrapper
     * @throws QueryException if an error occurs
     */
    QueryConnection getQueryConnection(boolean t) throws QueryException;

    /**
     * Execute a query that returns a list of results.
     *
     * @param <T> the type of results to return
     * @param query the query to execute
     * @param callback the callback executed for each result
     * @return the results
     * @throws QueryException if an error occurs
     */
    <T> List<T> listQuery(String query, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute a query that returns a list of objects.
     *
     * @param <T> the type of results to return
     * @param connection the query connection
     * @param query the query to execute
     * @param callback the callback executed for each result
     * @return the results
     * @throws QueryException if an error occurs
     */
    <T> List<T> listQuery(QueryConnection connection, String query, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute a query that returns a list of objects.
     *
     * @param <T> the type of results to return
     * @param query the query to execute
     * @param executor the query executor
     * @param callback the callback executed for each result
     * @return the results
     * @throws QueryException if an error occurs
     */
    <T> List<T> listQuery(String query, QueryExecutor executor, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute a query that returns a list of objects.
     *
     * @param <T> the type of results to return
     * @param connection the query connection
     * @param query the query to execute
     * @param executor the query executor
     * @param callback the callback executed for each result
     * @return the results
     * @throws QueryException if an error occurs
     */
    <T> List<T> listQuery(QueryConnection connection, String query, QueryExecutor executor, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute a query that returns a single result.
     *
     * @param <T> the type of result to return
     * @param query the query to execute
     * @param callback the callback executed for a result
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T query(String query, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute a query that returns a single result.
     *
     * @param <T> the type of result to return
     * @param connection the query connection
     * @param query the query to execute
     * @param callback the callback executed for a result
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T query(QueryConnection connection, String query, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute a query that returns a single result.
     *
     * @param <T> the type of result to return
     * @param query the query to execute
     * @param executor the query executor
     * @param callback the callback executed for a result
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T query(String query, QueryExecutor executor, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute a query that returns a single result.
     *
     * @param <T> the type of result to return
     * @param connection the query connection
     * @param query the query to execute
     * @param executor the query executor
     * @param callback the callback executed for a result
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T query(QueryConnection connection, String query, QueryExecutor executor, QueryCallback<T> callback) throws QueryException;

    /**
     * Execute an update.
     *
     * @param update the update to execute
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(String update) throws QueryException;

    /**
     * Execute an update.
     *
     * @param connection the query connection
     * @param update the update to execute
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(QueryConnection connection, String update) throws QueryException;

    /**
     * Execute an update.
     *
     * @param update the update to execute
     * @param executor the update executor
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(String update, UpdateExecutor executor) throws QueryException;

    /**
     * Execute an update.
     *
     * @param connection the query connection
     * @param update the update to execute
     * @param executor the update executor
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(QueryConnection connection, String update, UpdateExecutor executor) throws QueryException;

    /**
     * Execute an update.
     *
     * @param update the update to execute
     * @param callback the callback executed after the update
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(String update, UpdateCallback callback) throws QueryException;

    /**
     * Execute an update.
     *
     * @param connection the query connection
     * @param update the update to execute
     * @param callback the callback executed after the update
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(QueryConnection connection, String update, UpdateCallback callback) throws QueryException;

    /**
     * Execute an update.
     *
     * @param update the update to execute
     * @param executor the update executor
     * @param callback the callback executed after the update
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(String update, UpdateExecutor executor, UpdateCallback callback) throws QueryException;

    /**
     * Execute an update.
     *
     * @param connection the query connection
     * @param update the update to execute
     * @param executor the update executor
     * @param callback the callback executed after the update
     * @return the number of rows updated
     * @throws QueryException if an error occurs
     */
    int update(QueryConnection connection, String update, UpdateExecutor executor, UpdateCallback callback) throws QueryException;

    /**
     * Execute a stored procedure.
     *
     * @param call the stored procedure call to execute
     * @throws QueryException if an error occurs
     */
    void call(String call) throws QueryException;

    /**
     * Execute a stored procedure.
     *
     * @param connection the query connection
     * @param call the stored procedure call to execute
     * @throws QueryException if an error occurs
     */
    void call(QueryConnection connection, String call) throws QueryException;

    /**
     * Execute a stored procedure.
     *
     * @param call the stored procedure call to execute
     * @param executor the call executor
     * @throws QueryException if an error occurs
     */
    void call(String call, CallExecutor executor) throws QueryException;

    /**
     * Execute a stored procedure.
     *
     * @param connection the query connection
     * @param call the stored procedure call to execute
     * @param executor the call executor
     * @throws QueryException if an error occurs
     */
    void call(QueryConnection connection, String call, CallExecutor executor) throws QueryException;

    /**
     * Execute a stored procedure that returns an object.
     *
     * @param <T> the type of object to return
     * @param call the stored procedure call to execute
     * @param callback the callback executed after the call
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T call(String call, CallCallback<T> callback) throws QueryException;

    /**
     * Execute a stored procedure that returns an object.
     *
     * @param <T> the type of object to return
     * @param connection the query connection
     * @param call the stored procedure call to execute
     * @param callback the callback executed after the call
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T call(QueryConnection connection, String call, CallCallback<T> callback) throws QueryException;

    /**
     * Execute a stored procedure that returns an object.
     *
     * @param <T> the type of object to return
     * @param call the stored procedure call to execute
     * @param executor the call executor
     * @param callback the callback executed after the call
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T call(String call, CallExecutor executor, CallCallback<T> callback) throws QueryException;

    /**
     * Execute a stored procedure that returns an object.
     *
     * @param <T> the type of object to return
     * @param connection the query connection
     * @param call the stored procedure call to execute
     * @param executor the call executor
     * @param callback the callback executed after the call
     * @return the result
     * @throws QueryException if an error occurs
     */
    <T> T call(QueryConnection connection, String call, CallExecutor executor, CallCallback<T> callback) throws QueryException;
}
