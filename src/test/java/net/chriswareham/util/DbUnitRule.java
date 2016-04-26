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
	 * @param resourceBase the test class to use a a base for reading resources referred to in annotations
	 * @param database the name of the database to use
	 */
	public DbUnitRule(final Class<?> resourceBase, final String database) {
		this.resourceBase = resourceBase;
		try {
			databaseTester = new JdbcDatabaseTester(jdbcDriver.class.getName(), "jdbc:hsqldb:mem:" + database + ";shutdown=true", "SA", "");
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

			@Override
			public void evaluate() throws Throwable {
				Ddl ddl = method.getAnnotation(Ddl.class);
				if (ddl != null) {
					java.sql.Statement statement = connection.createStatement();
					for (String value : ddl.value()) {
						String sql = IOUtils.toString(resourceBase.getResourceAsStream(value));
						statement.executeUpdate(sql);
					}
					statement.close();
				}

				Data data = method.getAnnotation(Data.class);
				if (data != null) {
					java.sql.Statement statement = connection.createStatement();
					for (String value : data.value()) {
						for (String sql : IOUtils.readLines(resourceBase.getResourceAsStream(value))) {
							statement.executeUpdate(sql);
						}
					}
					statement.close();
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

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD })
	public static @interface Ddl {
		String[] value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD })
	public static @interface Data {
		String[] value();
	}

	private static class DbUnitDataSource implements DataSource {

		private final IDatabaseConnection databaseConnection;

		private DbUnitDataSource(final IDatabaseConnection databaseConnection) {
			this.databaseConnection = databaseConnection;
		}

		@Override
		public Connection getConnection() throws SQLException {
			return databaseConnection.getConnection();
		}

		@Override
		public Connection getConnection(final String username, final String password) throws SQLException {
			return databaseConnection.getConnection();
		}

		@Override
		public int getLoginTimeout() throws SQLException {
			return 0;
		}

		@Override
		public void setLoginTimeout(final int seconds) throws SQLException {
			return;
		}

		@Override
		public PrintWriter getLogWriter() throws SQLException {
			return null;
		}

		@Override
		public void setLogWriter(final PrintWriter out) throws SQLException {
			return;
		}

		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			throw new SQLFeatureNotSupportedException();
		}

		@Override
		public <T> T unwrap(final Class<T> iface) throws SQLException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isWrapperFor(final Class<?> iface) throws SQLException {
			throw new UnsupportedOperationException();
		}
	}
}
