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
     * @param event the event
     */
    void receiveEvent(Event event);
}
