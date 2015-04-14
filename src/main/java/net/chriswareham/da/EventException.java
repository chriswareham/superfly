/*
 * @(#) EventException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * Thrown when an error occurs in the events system.
 *
 * @author Chris Wareham
 */
public class EventException extends Exception {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the event exception.
     *
     * @param message the detail message
     */
    public EventException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the event exception.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public EventException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the event exception.
     *
     * @param cause the lower level cause of this exception
     */
    public EventException(final Throwable cause) {
        super(cause);
    }
}
