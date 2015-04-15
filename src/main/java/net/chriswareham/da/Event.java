/*
 * @(#) Event.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.io.Serializable;

/**
 * This class describes an event.
 *
 * @author Chris Wareham
 */
public class Event implements Serializable {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The identifier of the object the event concerns.
     */
    private final Object id;
    /**
     * The event type.
     */
    private final EventType type;

    /**
     * Create an instance of an event.
     *
     * @param i the identifier of the object the event concerns
     * @param t the event type
     */
    public Event(final Object i, final EventType t) {
        id = i;
        type = t;
    }

    /**
     * Get the identifier of the object the event concerns.
     *
     * @return the identifier of the object the event concerns
     */
    public Object getId() {
        return id;
    }

    /**
     * Get the event type.
     *
     * @return the event type
     */
    public EventType getType() {
        return type;
    }
}
