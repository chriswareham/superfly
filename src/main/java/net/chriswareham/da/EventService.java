/*
 * @(#) EventService.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

/**
 * This interface is implemented by classes that provide an events service.
 *
 * @author Chris Wareham
 */
public interface EventService {
    /**
     * Add a topic listener.
     *
     * @param topic the topic
     * @param listener the listener
     */
    void addTopicListener(String topic, TopicListener listener);

    /**
     * Remove a topic listener.
     *
     * @param topic the topic
     * @param listener the listener
     */
    void removeTopicListener(String topic, TopicListener listener);

    /**
     * Publish an event.
     *
     * @param topic the topic
     * @param event the event
     */
    void publishEvent(String topic, Event event);
}
