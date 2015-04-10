/*
 * @(#) TopicListener.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * This interface is implemented by classes that receive events.
 *
 * @author Chris Wareham
 */
public interface TopicListener {
    /**
     * Receive an event.
     *
     * @param id the event identifier
     * @param type the type of event
     */
    void receiveEvent(Object id, EventType type);
}
