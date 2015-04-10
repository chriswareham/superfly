/*
 * @(#) ResourceExpiredException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import javax.servlet.http.HttpServletResponse;

/**
 * Thrown when a resource has expired.
 *
 * @author Chris Wareham
 */
public class ResourceExpiredException extends RequestException {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the ResourceExpiredException class.
     *
     * @param message the detail message
     */
    public ResourceExpiredException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the ResourceExpiredException class.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public ResourceExpiredException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the ResourceExpiredException class.
     *
     * @param cause the lower level cause of this exception
     */
    public ResourceExpiredException(final Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getError() {
        return HttpServletResponse.SC_GONE;
    }
}
