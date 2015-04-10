/*
 * @(#) ActionException.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

/**
 * Thrown when an error occurs in an action method.
 *
 * @author Chris Wareham
 */
public class ActionException extends Exception {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the ActionException class.
     *
     * @param message the detail message
     */
    public ActionException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the ActionException class.
     *
     * @param message the detail message
     * @param cause the lower level cause of this exception
     */
    public ActionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the ActionException class.
     *
     * @param cause the lower level cause of this exception
     */
    public ActionException(final Throwable cause) {
        super(cause);
    }
}
