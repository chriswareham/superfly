/*
 * @(#) ComponentException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * Thrown when an error occurs in a framework component method.
 *
 * @author Chris Wareham
 */
public class ComponentException extends Exception {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the ComponentException class.
     *
     * @param message the detail message
     */
    public ComponentException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the ComponentException class.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public ComponentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the ComponentException class.
     *
     * @param cause the lower level cause of this exception
     */
    public ComponentException(final Throwable cause) {
        super(cause);
    }
}
