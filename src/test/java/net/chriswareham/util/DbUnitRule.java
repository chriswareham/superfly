/*
 * @(#) DbUnitRule.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;

import org.hsqldb.jdbcDriver;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * This class provides a JUnit rule that establishes a data source and runs both
 * test fixture DDL and DML statements.
 *
 * @author Chris Wareham
 */
public class DbUnitRule implements MethodRule {
    /**
     * The test class to use a a base for reading resources referred to in annotations.
     */
    private final Class<?> resourceBase;
    /**
     * The DBUnit database tester.
     */
    private IDatabaseTester databaseTester;
    /**
     * The DBUnit database connection.
     */
    private IDatabaseConnection databaseConnection;
    /**
     * The JDBC database connection used to run test fixture DDL and DML statements.
     */
    private Connection connection;

    /**
     * Construct an instance of the rule.
     *
     * @param rb the test class to use a a base for reading resources referred to in annotations
     * @param dbName the name of the database to use
     */
    public DbUnitRule(final Class<?> rb, final String dbName) {
        this.resourceBase = rb;
        try {
            databaseTester = new JdbcDatabaseTester(jdbcDriver.class.getName(), "jdbc:hsqldb:mem:" + dbName + ";shutdown=true", "SA", "");
            databaseConnection = databaseTester.getConnection();
            connection = databaseConnection.getConnection();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Wraps a test statement to establish a data source and run both test
     * fixture DDL and DML statements.
     *
     * @param base the test statement to be modified
     * @param method the test method
     * @param target the test class instance the test method is for
     * @return a statement wrapping the test statement
     */
    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void evaluate() throws Throwable {
                Ddl ddl = method.getAnnotation(Ddl.class);
                if (ddl != null) {
                    try (java.sql.Statement statement = connection.createStatement()) {
                        for (String value : ddl.value()) {
                            String sql = IOUtils.toString(resourceBase.getResourceAsStream(value));
                            statement.executeUpdate(sql);
                        }
                    }
                }

                Dml dml = method.getAnnotation(Dml.class);
                if (dml != null) {
                    try (java.sql.Statement statement = connection.createStatement()) {
                        for (String value : dml.value()) {
                            for (String sql : IOUtils.readLines(resourceBase.getResourceAsStream(value))) {
                                statement.executeUpdate(sql);
                            }
                        }
                    }
                }

                base.evaluate();

                connection.close();
            }
        };
    }

    /**
     * Get the data source.
     *
     * @return the data source
     */
    public DataSource getDataSource() {
        return new DbUnitDataSource(databaseConnection);
    }

    /**
     * This tag lists the DDL files to be evaluated before a test.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Ddl {
        /**
         * Get the DDL files to be evaluated before a test.
         */
        String[] value();
    }

    /**
     * This tag lists the DML files to be evaluated before a test.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Dml {
        /**
         * Get the DML files to be evaluated before a test.
         */
        String[] value();
    }

    /**
     * This class provides a data source for a DBUnit database connection.
     */
    private static final class DbUnitDataSource implements DataSource {
        /**
         * The DBUnit database connection.
         */
        private final IDatabaseConnection databaseConnection;

        /**
         * Construct a data source for a DBUnit database connection.
         *
         * @param dc the DBUnit database connection
         */
        private DbUnitDataSource(final IDatabaseConnection dc) {
            this.databaseConnection = dc;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Connection getConnection() throws SQLException {
            return databaseConnection.getConnection();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Connection getConnection(final String username, final String password) throws SQLException {
            return databaseConnection.getConnection();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setLoginTimeout(final int seconds) throws SQLException {
            return;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setLogWriter(final PrintWriter out) throws SQLException {
            return;
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
}
