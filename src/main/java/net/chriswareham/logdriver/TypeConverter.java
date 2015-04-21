/*
 * @(#) TypeConverter.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.logdriver;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a converter for JDBC SQL types.
 */
public final class TypeConverter {
    /**
     * The JDBC SQL types.
     */
    private static final Map<Integer, String> TYPES = new HashMap<>();

    static {
        TYPES.put(2003, "ARRAY");
        TYPES.put(-5, "BIGINT");
        TYPES.put(-2, "BINARY");
        TYPES.put(-7, "BIT");
        TYPES.put(2004, "BLOB");
        TYPES.put(16, "BOOLEAN");
        TYPES.put(1, "CHAR");
        TYPES.put(2005, "CLOB");
        TYPES.put(70, "DATALINK");
        TYPES.put(91, "DATE");
        TYPES.put(3, "DECIMAL");
        TYPES.put(2001, "DISTINCT");
        TYPES.put(8, "DOUBLE");
        TYPES.put(6, "FLOAT");
        TYPES.put(4, "INTEGER");
        TYPES.put(2000, "JAVA_OBJECT");
        TYPES.put(-4, "LONGVARBINARY");
        TYPES.put(-1, "LONGVARCHAR");
        TYPES.put(0, "NULL");
        TYPES.put(2, "NUMERIC");
        TYPES.put(1111, "OTHER");
        TYPES.put(7, "REAL");
        TYPES.put(2006, "REF");
        TYPES.put(5, "SMALLINT");
        TYPES.put(2002, "STRUCT");
        TYPES.put(92, "TIME");
        TYPES.put(93, "TIMESTAMP");
        TYPES.put(-6, "TINYINT");
        TYPES.put(12, "VARCHAR");
        TYPES.put(-3, "VARBINARY");
    }

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private TypeConverter() {
        // private constructor to prevent instantiation of utility class
    }

    /**
     * Get a string representation for an JDBC SQL type.
     *
     * @param type the JDBC SQL type
     * @return the string representation of the JDBC SQL type
     */
    public static String convert(final int type) {
        return TYPES.containsKey(type) ? TYPES.get(type) : "";
    }
}
