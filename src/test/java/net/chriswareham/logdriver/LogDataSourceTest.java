/*
 * @(#) LogDataSourceTest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.logdriver;

import java.sql.Connection;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class provides tests for the logging JDBC data source.
 *
 * @author Chris Wareham
 */
public class LogDataSourceTest {
    /**
     * The login timeout.
     */
    private static final int LOGIN_TIMEOUT = 1000;
    /**
     * The username.
     */
    private static final String USERNAME = "username";
    /**
     * The password.
     */
    private static final String PASSWORD = "password";

    /**
     * The instance to test.
     */
    private LogDataSource logDataSource;

    /**
     * Set up the instance to test.
     *
     * @throws Exception if an error occurs
     */
    @Before
    public void before() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);

        DataSource mockDataSource = Mockito.mock(DataSource.class);
        Mockito.doReturn(mockConnection).when(mockDataSource).getConnection();
        Mockito.doReturn(mockConnection).when(mockDataSource).getConnection(USERNAME, PASSWORD);
        Mockito.doReturn(LOGIN_TIMEOUT).when(mockDataSource).getLoginTimeout();

        logDataSource = new LogDataSource(mockDataSource);
    }

    /**
     * Test getting a connection.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetConnection() throws Exception {
        Assert.assertNotNull(logDataSource.getConnection());
    }

    /**
     * Test getting a connection with a username and password.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetConnectionWithUsernameAndPassword() throws Exception {
        Assert.assertNotNull(logDataSource.getConnection(USERNAME, PASSWORD));
    }

    /**
     * Test getting a login timeout.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetLoginTimeout() throws Exception {
        Assert.assertEquals(LOGIN_TIMEOUT, logDataSource.getLoginTimeout());
    }

    /**
     * Test setting a login timeout.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetLoginTimeout() throws Exception {
        logDataSource.setLoginTimeout(LOGIN_TIMEOUT);
    }
}
