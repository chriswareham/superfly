/*
 * @(#) JmsEventServiceImpl.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.log4j.Logger;

import net.chriswareham.di.LifecycleComponent;

/**
 * This class provides a JMS based events service.
 *
 * @author Chris Wareham
 */
public class JmsEventServiceImpl implements EventService, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(JmsEventServiceImpl.class);

    /**
     * The connection factory.
     */
    private ConnectionFactory connectionFactory;
    /**
     * The topics.
     */
    private final Map<String, Destination> topics = new HashMap<>();
    /**
     * The topic listeners.
     */
    private final Map<String, List<TopicListener>> listeners = new HashMap<>();

    /**
     * Set the connection factory.
     *
     * @param cf the connection factory
     */
    public void setConnnectionFactory(final ConnectionFactory cf) {
        connectionFactory = cf;
    }

    /**
     * Add a topic.
     *
     * @param topic the topic to add
     * @param destination the topic destination
     */
    public void addTopic(final String topic, final Destination destination) {
        topics.put(topic, destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTopicListener(final String topic, final TopicListener listener) {
        if (!listeners.containsKey(topic)) {
            listeners.put(topic, new CopyOnWriteArrayList<>());
        }
        listeners.get(topic).add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTopicListener(final String topic, final TopicListener listener) {
        if (listeners.containsKey(topic)) {
            listeners.get(topic).remove(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishEvent(final String topic, final Event event) {
        if (!topics.containsKey(topic)) {
            throw new IllegalArgumentException("Invalid topic '" + topic + "'");
        }

        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();

            MessageProducer producer = session.createProducer(topics.get(topic));
            producer.send(session.createObjectMessage(event));
        } catch (JMSException exception) {
            LOGGER.error("publishEvent(): failed to publish event", exception);
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
//        for (String topic : listeners.keySet()) {
//            for (TopicListener listener : listeners.get(topic)) {
//                XXX
//            }
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
//        XXX
    }

    /**
     * Close a connection.
     *
     * @param connection the connection to close
     */
    private void closeConnection(final Connection connection) {
        if (connection != null) {
            try {
                connection.stop();
            } catch (JMSException exception) {
                LOGGER.error("closeConnection(): erorr stopping connection", exception);
            }
            try {
                connection.close();
            } catch (JMSException exception) {
                LOGGER.error("closeConnection(): error closing connection", exception);
            }
        }
    }
}
