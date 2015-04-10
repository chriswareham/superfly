/*
 * @(#) ResourceNotFoundException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import javax.servlet.http.HttpServletResponse;

/**
 * Thrown when an resource cannot be found.
 *
 * @author Chris Wareham
 */
public class ResourceNotFoundException extends RequestException {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the ResourceNotFoundException class.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the ResourceNotFoundException class.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the ResourceNotFoundException class.
     *
     * @param cause the lower level cause of this exception
     */
    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getError() {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
