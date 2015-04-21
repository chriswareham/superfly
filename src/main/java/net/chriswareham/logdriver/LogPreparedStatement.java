/*
 * @(#) LogPreparedStatement.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.logdriver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * This class provides a logging JDBC prepared statement.
 */
public class LogPreparedStatement extends LogStatement implements PreparedStatement {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LogPreparedStatement.class);

    /**
     * The wrapped prepared statement.
     */
    private final PreparedStatement statement;
    /**
     * The SQL.
     */
    private final String sql;
    /**
     * The bind parameters by index.
     */
    private final Map<Integer, Object> bindParams = new TreeMap<>();

    /**
     * Construct an instance of the logging prepared JDBC statement.
     *
     * @param ps the wrapped prepared statement
     * @param c the connection
     * @param s the SQL
     */
    public LogPreparedStatement(final PreparedStatement ps, final Connection c, final String s) {
        super(ps, c);
        statement = ps;
        sql = s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute() throws SQLException {
        logStatement("Executing: ");
        return statement.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet executeQuery() throws SQLException {
        logStatement("Executing query: ");
        return statement.executeQuery();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdate() throws SQLException {
        logStatement("Executing update: ");
        return statement.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBatch() throws SQLException {
        logStatement("Adding to batch: ");
        statement.addBatch();
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
    public void clearParameters() throws SQLException {
        statement.clearParameters();
        bindParams.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        statement.setNull(parameterIndex, sqlType, typeName);
        bindParams.put(parameterIndex, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
        statement.setNull(parameterIndex, sqlType);
        bindParams.put(parameterIndex, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        statement.setBoolean(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        statement.setByte(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        statement.setShort(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        statement.setInt(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        statement.setLong(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        statement.setFloat(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        statement.setDouble(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        statement.setBigDecimal(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {
        statement.setString(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        statement.setBytes(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        statement.setDate(parameterIndex, x, cal);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        statement.setDate(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        statement.setTime(parameterIndex, x, cal);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        statement.setTime(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        statement.setTimestamp(parameterIndex, x, cal);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        statement.setTimestamp(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
        statement.setAsciiStream(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        statement.setUnicodeStream(parameterIndex, x, length);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
        statement.setBinaryStream(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        statement.setObject(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
        bindParams.put(parameterIndex, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
        bindParams.put(parameterIndex, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader);
        bindParams.put(parameterIndex, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRef(final int parameterIndex, final Ref x) throws SQLException {
        statement.setRef(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArray(final int parameterIndex, final Array x) throws SQLException {
        statement.setArray(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {
        statement.setURL(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        statement.setRowId(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNString(final int parameterIndex, final String value) throws SQLException {
        statement.setNString(parameterIndex, value);
        bindParams.put(parameterIndex, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        statement.setNCharacterStream(parameterIndex, value, length);
        bindParams.put(parameterIndex, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        statement.setNCharacterStream(parameterIndex, value);
        bindParams.put(parameterIndex, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClob(final int parameterIndex, final Clob x) throws SQLException {
        statement.setClob(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        statement.setClob(parameterIndex, reader, length);
        bindParams.put(parameterIndex, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
        statement.setClob(parameterIndex, reader);
        bindParams.put(parameterIndex, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
        statement.setBlob(parameterIndex, x);
        bindParams.put(parameterIndex, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        statement.setBlob(parameterIndex, inputStream, length);
        bindParams.put(parameterIndex, inputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
        statement.setBlob(parameterIndex, inputStream);
        bindParams.put(parameterIndex, inputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
        statement.setNClob(parameterIndex, value);
        bindParams.put(parameterIndex, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        statement.setNClob(parameterIndex, reader, length);
        bindParams.put(parameterIndex, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
        statement.setNClob(parameterIndex, reader);
        bindParams.put(parameterIndex, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        statement.setSQLXML(parameterIndex, xmlObject);
        bindParams.put(parameterIndex, xmlObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return statement.getMetaData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return statement.getParameterMetaData();
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

    /**
     * Log a statement.
     *
     * @param msg the log message
     */
    private void logStatement(final String msg) {
        if (LOGGER.isDebugEnabled()) {
            StringBuilder buf = new StringBuilder(msg);
            buf.append(sql);
            if (!bindParams.isEmpty()) {
                buf.append(" Bind parameters: ");
                buf.append(bindParams);
            }
            LOGGER.debug(buf);
        }
    }
}
