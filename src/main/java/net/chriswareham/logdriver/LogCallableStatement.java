/*
 * @(#) LogCallableStatement.java
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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
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
 * This class provides a logging JDBC callable statement.
 */
public class LogCallableStatement extends LogPreparedStatement implements CallableStatement {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LogCallableStatement.class);

    /**
     * The wrapped callable statement.
     */
    private CallableStatement statement;
    /**
     * The SQL.
     */
    private String sql;
    /**
     * The bind parameters by name.
     */
    private Map<String, Object> bindParams = new TreeMap<>();
    /**
     * The out parameters by name.
     */
    private Map<Object, OutParam> outParams = new TreeMap<>();

    /**
     * Construct an instance of the logging prepared JDBC statement.
     *
     * @param cs the wrapped prepared statement
     * @param c the connection
     * @param s the SQL
     */
    public LogCallableStatement(final CallableStatement cs, final Connection c, final String s) {
        super(cs, c, s);
        statement = cs;
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
    public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
        statement.registerOutParameter(parameterIndex, sqlType);
        outParams.put(parameterIndex, new OutParam(sqlType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        statement.registerOutParameter(parameterIndex, sqlType, scale);
        outParams.put(parameterIndex, new OutParam(sqlType, scale));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        statement.registerOutParameter(parameterIndex, sqlType, typeName);
        outParams.put(parameterIndex, new OutParam(sqlType, typeName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
        statement.registerOutParameter(parameterName, sqlType);
        outParams.put(parameterName, new OutParam(sqlType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
        statement.registerOutParameter(parameterName, sqlType, scale);
        outParams.put(parameterName, new OutParam(sqlType, scale));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        statement.registerOutParameter(parameterName, sqlType, typeName);
        outParams.put(parameterName, new OutParam(sqlType, typeName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(final int parameterIndex) throws SQLException {
        return statement.getString(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(final String parameterName) throws SQLException {
        return statement.getString(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        return statement.getBoolean(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(final String parameterName) throws SQLException {
        return statement.getBoolean(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(final int parameterIndex) throws SQLException {
        return statement.getByte(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(final String parameterName) throws SQLException {
        return statement.getByte(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(final int parameterIndex) throws SQLException {
        return statement.getShort(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(final String parameterName) throws SQLException {
        return statement.getShort(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(final int parameterIndex) throws SQLException {
        return statement.getInt(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(final String parameterName) throws SQLException {
        return statement.getInt(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(final int parameterIndex) throws SQLException {
        return statement.getLong(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(final String parameterName) throws SQLException {
        return statement.getLong(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(final int parameterIndex) throws SQLException {
        return statement.getFloat(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(final String parameterName) throws SQLException {
        return statement.getFloat(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(final String parameterName) throws SQLException {
        return statement.getDouble(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(final int parameterIndex) throws SQLException {
        return statement.getDouble(parameterIndex);
    }

    /**
     * {@inheritDoc}
     * @deprecated Use {@code getBigDecimal(int parameterIndex)} or {@code getBigDecimal(String parameterName)}
     */
    @Override
    @Deprecated
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
        return statement.getBigDecimal(parameterIndex, scale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        return statement.getBigDecimal(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        return statement.getBigDecimal(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        return statement.getBytes(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(final String parameterName) throws SQLException {
        return statement.getBytes(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
        return statement.getDate(parameterIndex, cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(final int parameterIndex) throws SQLException {
        return statement.getDate(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
        return statement.getDate(parameterName, cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(final String parameterName) throws SQLException {
        return statement.getDate(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
        return statement.getTime(parameterIndex, cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(final int parameterIndex) throws SQLException {
        return statement.getTime(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
        return statement.getTime(parameterName, cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(final String parameterName) throws SQLException {
        return statement.getTime(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
        return statement.getTimestamp(parameterIndex, cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        return statement.getTimestamp(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
        return statement.getTimestamp(parameterName, cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(final String parameterName) throws SQLException {
        return statement.getTimestamp(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(final int parameterIndex) throws SQLException {
        return statement.getObject(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        return statement.getObject(parameterIndex, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        return statement.getObject(parameterIndex, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(final String parameterName) throws SQLException {
        return statement.getObject(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        return statement.getObject(parameterName, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        return statement.getObject(parameterName, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ref getRef(final int parameterIndex) throws SQLException {
        return statement.getRef(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ref getRef(final String parameterName) throws SQLException {
        return statement.getRef(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Blob getBlob(final int parameterIndex) throws SQLException {
        return statement.getBlob(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Blob getBlob(final String parameterName) throws SQLException {
        return statement.getBlob(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clob getClob(final int parameterIndex) throws SQLException {
        return statement.getClob(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clob getClob(final String parameterName) throws SQLException {
        return statement.getClob(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Array getArray(final int parameterIndex) throws SQLException {
        return statement.getArray(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Array getArray(final String parameterName) throws SQLException {
        return statement.getArray(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getURL(final int parameterIndex) throws SQLException {
        return statement.getURL(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getURL(final String parameterName) throws SQLException {
        return statement.getURL(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RowId getRowId(final int parameterIndex) throws SQLException {
        return statement.getRowId(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RowId getRowId(final String parameterName) throws SQLException {
        return statement.getRowId(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NClob getNClob(final int parameterIndex) throws SQLException {
        return statement.getNClob(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NClob getNClob(final String parameterName) throws SQLException {
        return statement.getNClob(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        return statement.getSQLXML(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLXML getSQLXML(final String parameterName) throws SQLException {
        return statement.getSQLXML(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNString(final int parameterIndex) throws SQLException {
        return statement.getNString(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNString(final String parameterName) throws SQLException {
        return statement.getNString(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        return statement.getNCharacterStream(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        return statement.getNCharacterStream(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        return statement.getCharacterStream(parameterIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        return statement.getCharacterStream(parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNull(final String parameterName, final int sqlType) throws SQLException {
        statement.setNull(parameterName, sqlType);
        bindParams.put(parameterName, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        statement.setNull(parameterName, sqlType, typeName);
        bindParams.put(parameterName, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolean(final String parameterName, final boolean x) throws SQLException {
        statement.setBoolean(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setByte(final String parameterName, final byte x) throws SQLException {
        statement.setByte(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShort(final String parameterName, final short x) throws SQLException {
        statement.setShort(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(final String parameterName, final int x) throws SQLException {
        statement.setInt(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLong(final String parameterName, final long x) throws SQLException {
        statement.setLong(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloat(final String parameterName, final float x) throws SQLException {
        statement.setFloat(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDouble(final String parameterName, final double x) throws SQLException {
        statement.setDouble(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
        statement.setBigDecimal(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setString(final String parameterName, final String x) throws SQLException {
        statement.setString(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBytes(final String parameterName, final byte[] x) throws SQLException {
        statement.setBytes(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
        statement.setDate(parameterName, x, cal);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(final String parameterName, final Date x) throws SQLException {
        statement.setDate(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
        statement.setTime(parameterName, x, cal);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(final String parameterName, final Time x) throws SQLException {
        statement.setTime(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        statement.setTimestamp(parameterName, x, cal);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
        statement.setTimestamp(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        statement.setAsciiStream(parameterName, x, length);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        statement.setAsciiStream(parameterName, x, length);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
        statement.setAsciiStream(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        statement.setBinaryStream(parameterName, x, length);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        statement.setBinaryStream(parameterName, x, length);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
        statement.setBinaryStream(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        statement.setObject(parameterName, x, targetSqlType);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        statement.setObject(parameterName, x, targetSqlType);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final String parameterName, final Object x) throws SQLException {
        statement.setObject(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
        statement.setCharacterStream(parameterName, reader, length);
        bindParams.put(parameterName, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
        statement.setCharacterStream(parameterName, reader, length);
        bindParams.put(parameterName, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
        statement.setCharacterStream(parameterName, reader);
        bindParams.put(parameterName, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setURL(final String parameterName, final URL val) throws SQLException {
        statement.setURL(parameterName, val);
        bindParams.put(parameterName, val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRowId(final String parameterName, final RowId x) throws SQLException {
        statement.setRowId(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNString(final String parameterName, final String value) throws SQLException {
        statement.setNString(parameterName, value);
        bindParams.put(parameterName, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        statement.setNCharacterStream(parameterName, value, length);
        bindParams.put(parameterName, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
        statement.setNCharacterStream(parameterName, value);
        bindParams.put(parameterName, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        statement.setNClob(parameterName, reader, length);
        bindParams.put(parameterName, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNClob(final String parameterName, final Reader reader) throws SQLException {
        statement.setNClob(parameterName, reader);
        bindParams.put(parameterName, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNClob(final String parameterName, final NClob value) throws SQLException {
        statement.setNClob(parameterName, value);
        bindParams.put(parameterName, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        statement.setClob(parameterName, reader, length);
        bindParams.put(parameterName, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClob(final String parameterName, final Reader reader) throws SQLException {
        statement.setClob(parameterName, reader);
        bindParams.put(parameterName, reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClob(final String parameterName, final Clob x) throws SQLException {
        statement.setClob(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        statement.setBlob(parameterName, inputStream, length);
        bindParams.put(parameterName, inputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
        statement.setBlob(parameterName, inputStream);
        bindParams.put(parameterName, inputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlob(final String parameterName, final Blob x) throws SQLException {
        statement.setBlob(parameterName, x);
        bindParams.put(parameterName, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
        statement.setSQLXML(parameterName, xmlObject);
        bindParams.put(parameterName, xmlObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasNull() throws SQLException {
        return statement.wasNull();
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
            if (!outParams.isEmpty()) {
                buf.append(" Out parameters: ");
                buf.append(outParams);
            }
            LOGGER.debug(buf);
        }
    }

    /**
     * This class describes an out parameter.
     */
    private static class OutParam {
        /**
         * The description of the out parameter.
         */
        private final String description;

        /**
         * Construct an instance of the class that describes an out parameter.
         *
         * @param type the type
         */
        OutParam(final int type) {
            description = TypeConverter.convert(type);
        }

        /**
         * Construct an instance of the class that describes an out parameter.
         *
         * @param type the type
         * @param scale the scale
         */
        OutParam(final int type, final int scale) {
            description = TypeConverter.convert(type) + "(" + scale + ")";
        }

        /**
         * Construct an instance of the class that describes an out parameter.
         *
         * @param type the type
         * @param name the name
         */
        OutParam(final int type, final String name) {
            description = TypeConverter.convert(type) + " '" + name + "'";
        }

        /**
         * Get the description of the out parameter.
         *
         * @return the description of the out parameter
         */
        @Override
        public String toString() {
            return description;
        }
    }
}
