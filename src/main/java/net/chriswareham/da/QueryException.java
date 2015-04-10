/*
 * @(#) QueryException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * Thrown when an error occurs in a query method.
 *
 * @author Chris Wareham
 */
public class QueryException extends Exception {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the query exception.
     *
     * @param message the detail message
     */
    public QueryException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the query exception.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public QueryException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the query exception.
     *
     * @param cause the lower level cause of this exception
     */
    public QueryException(final Throwable cause) {
        super(cause);
    }
}
