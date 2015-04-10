/*
 * @(#) SerialiserServiceException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import net.chriswareham.di.ComponentException;

/**
 * Thrown when an error occurs in a serialiser service method.
 *
 * @author Chris Wareham
 */
public class SerialiserServiceException extends ComponentException {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the serialiser service exception.
     *
     * @param message the detail message
     */
    public SerialiserServiceException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the serialiser service exception.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public SerialiserServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the serialiser service exception.
     *
     * @param cause the lower level cause of this exception
     */
    public SerialiserServiceException(final Throwable cause) {
        super(cause);
    }
}
