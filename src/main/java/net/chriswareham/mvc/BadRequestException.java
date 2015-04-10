/*
 * @(#) BadRequestException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import javax.servlet.http.HttpServletResponse;

/**
 * Thrown when a bad request has been made.
 *
 * @author Chris Wareham
 */
public class BadRequestException extends RequestException {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the bad request exception.
     *
     * @param message the detail message
     */
    public BadRequestException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the bad request exception.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the bad request exception.
     *
     * @param cause the lower level cause of this exception
     */
    public BadRequestException(final Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getError() {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
}
