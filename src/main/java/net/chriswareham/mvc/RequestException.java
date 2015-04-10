/*
 * @(#) ActionException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

/**
 * Thrown when an error occurs as a result of an action request.
 *
 * @author Chris Wareham
 */
public abstract class RequestException extends ActionException {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the request exception.
     *
     * @param message the detail message
     */
    public RequestException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the request exception.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public RequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the request exception.
     *
     * @param cause the lower level cause of this exception
     */
    public RequestException(final Throwable cause) {
        super(cause);
    }

    /**
     * Get the HTTP Status Code for the error.
     *
     * @return the HTTP Status Code for the error
     */
    public abstract int getError();
}
