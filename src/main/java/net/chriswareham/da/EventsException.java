/*
 * @(#) EventsException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * Thrown when an error occurs in the events system.
 *
 * @author Chris Wareham
 */
public class EventsException extends Exception {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the EventsException class.
     *
     * @param message the detail message
     */
    public EventsException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the EventsException class.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public EventsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the EventsException class.
     *
     * @param cause the lower level cause of this exception
     */
    public EventsException(final Throwable cause) {
        super(cause);
    }
}
