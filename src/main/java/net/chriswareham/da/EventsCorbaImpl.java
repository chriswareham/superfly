/*
 * @(#) EventsCorbaImpl.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

//import java.util.HashMap;
import java.util.HashSet;
//import java.util.Map;
import java.util.Properties;
import java.util.Set;
//import java.util.concurrent.locks.ReadWriteLock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
//import org.omg.CosEventChannelAdmin.ConsumerAdmin;
//import org.omg.CosEventChannelAdmin.EventChannel;
//import org.omg.CosEventChannelAdmin.EventChannelHelper;
//import org.omg.CosEventChannelAdmin.ProxyPushConsumer;
//import org.omg.CosEventChannelAdmin.ProxyPushSupplier;
//import org.omg.CosEventChannelAdmin.SupplierAdmin;
//import org.omg.CosEventComm.Disconnected;
//import org.omg.CosEventComm.PushConsumer;
//import org.omg.CosEventComm.PushConsumerHelper;
//import org.omg.CosEventComm.PushConsumerPOA;
//import org.omg.CosEventComm.PushSupplierPOA;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import org.apache.log4j.Logger;

import net.chriswareham.di.LifecycleComponent;
import org.omg.CORBA.UserException;

/**
 * This class provides a CORBA based events service.
 *
 * @author Chris Wareham
 */
public class EventsCorbaImpl implements Events, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(EventsCorbaImpl.class);

    /**
     * The Object Request Broker.
     */
    private ORB orb;
    /**
     * The topics.
     */
    private final Set<String> topics = new HashSet<>();
/*
    / **
     * The publishers.
     * /
    private Map<String, ProxyPushConsumer> publishers = new HashMap<>();
    / **
     * The subscribers.
     * /
    private Map<String, ProxyPushSupplier> subscribers = new HashMap<>();
    / **
     * The consumers.
     * /
    private Map<String, Consumer> consumers = new HashMap<>();
*/
    /**
     * Add a topic.
     *
     * @param t the topic to add
     */
    public void addTopic(final String t) {
        topics.add(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTopicListener(final String topic, final TopicListener listener) {
//        Consumer consumer = consumers.get(topic);
//        consumer.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTopicListener(final String topic, final TopicListener listener) {
//        Consumer consumer = consumers.get(topic);
//        consumer.removeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishEvent(final String topic, final Object id, final EventType type) {
//        try {
            Any any = orb.create_any();
            any.insert_string(id.toString());
            any.insert_string(type.toString());

//            ProxyPushConsumer proxyPushConsumer = publishers.get(topic);
//            proxyPushConsumer.push(any);
//        } catch (Disconnected exception) {
//            LOGGER.error("publishEvent(): publisher disconnected");
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (orb != null) {
            throw new IllegalStateException("Events system has already been started");
        }

        try {
            orb = ORB.init(new String[0], new Properties());
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to initialise ORB", exception);
        }

        POA poa = null;

        try {
            org.omg.CORBA.Object obj = orb.resolve_initial_references("RootPOA");
            poa = POAHelper.narrow(obj);
        } catch (InvalidName exception) {
            throw new IllegalStateException("Unable to resolve Root POA", exception);
        }

        try {
            poa.the_POAManager().activate();
        } catch (AdapterInactive exception) {
            throw new IllegalStateException("Unable to activate Root POA", exception);
        }

        NamingContextExt namingContextExt = null;

        try {
            org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService");
            namingContextExt = NamingContextExtHelper.narrow(obj);
        } catch (InvalidName exception) {
            throw new IllegalStateException("Unable to resolve Name Service", exception);
        }

        for (String topic : topics) {
            try {
                org.omg.CORBA.Object obj = namingContextExt.resolve_str(topic);
/*                EventChannel eventChannel = EventChannelHelper.narrow(obj);

                Supplier supplier = new Supplier(topic);

                SupplierAdmin supplierAdmin = eventChannel.for_suppliers();
                ProxyPushConsumer proxyPushConsumer = supplierAdmin.obtain_push_consumer();
                proxyPushConsumer.connect_push_supplier(supplier._this(orb));

                publishers.put(topic, proxyPushConsumer);

                Consumer consumer = new Consumer(topic);
                PushConsumer pushConsumer = PushConsumerHelper.narrow(poa.servant_to_reference(consumer));

                ConsumerAdmin consumerAdmin = eventChannel.for_consumers();
                ProxyPushSupplier proxyPushSupplier = consumerAdmin.obtain_push_supplier();
                proxyPushSupplier.connect_push_consumer(pushConsumer);

                subscribers.put(topic, proxyPushSupplier);

                consumers.put(topic, consumer); */
            } catch (UserException exception) {
                throw new IllegalStateException("Error connecting publisher and subscriber for topic " + topic, exception);
            }
        }

        orb.run();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if (orb == null) {
            throw new IllegalStateException("Events system has already been stopped");
        }
/*
        consumers.clear();

        // Disconnect the publishers

        for (String topic : publishers.keySet()) {
            try {
                ProxyPushConsumer proxyPushConsumer = publishers.get(topic);
                proxyPushConsumer.disconnect_push_consumer();
            } catch (Exception exception) {
                LOGGER.error("stop(): error disconnecting publisher for topic " + topic, exception);
            }
        }

        publishers.clear();

        // Disconnect the suppliers

        for (String topic : subscribers.keySet()) {
            try {
                ProxyPushSupplier proxyPushSupplier = subscribers.get(topic);
                proxyPushSupplier.disconnect_push_supplier();
            } catch (Exception exception) {
                LOGGER.error("stop(): error disconnecting subscriber for topic " + topic, exception);
            }
        }

        subscribers.clear();
*/
        // Shutdown the ORB

        try {
            orb.shutdown(false);
            orb.destroy();
        } catch (Exception exception) {
            LOGGER.error("stop(): error stopping events system", exception);
        }

        orb = null;
    }
/*
    / **
     * This class dispatches events to listeners.
     * /
    private static class Supplier extends PushSupplierPOA {
        / **
         * The topic.
         * /
        private String topic;

        / **
         * Constructs a new instance of the consumer.
         *
         * @param t the topic
         * /
        public Supplier(final String t) {
            topic = t;
        }

        / **
         * Called when the consumer is disconnected.
         * /
        public void disconnect_push_supplier() {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Supplier disconnected for topic " + topic);
            }
        }
    }

    / **
     * This class dispatches events to listeners.
     * /
    private static class Consumer extends PushConsumerPOA {
        / **
         * The topic.
         * /
        private String topic;
        / **
         * The read-write lock.
         * /
        private ReadWriteLock lock = new ReentrantReadWriteLock();
        / **
         * The listeners to dispatch events to.
         * /
        private List<TopicListener> listeners = new ArrayList<TopicListener>();

        / **
         * Constructs a new instance of the consumer.
         *
         * @param t the topic
         * /
        public Consumer(final String t) {
            topic = t;
        }

        public void addListener(final TopicListener listener) {
            try {
                lock.writeLock().lock();
                listeners.add(listener);
            } finally {
                lock.writeLock().unlock();
            }
        }

        public void removeListener(final TopicListener listener) {
            try {
                lock.writeLock().lock();
                listeners.remove(listener);
            } finally {
                lock.writeLock().unlock();
            }
        }

        / **
         * Called when the consumer is disconnected.
         * /
        public void disconnect_push_consumer() {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Consumer disconnected for topic " + topic);
            }
        }

        / **
         * Called when an event is pushed by the supplier.
         *
         * @param data the event data
         * @throws Disconnected if the consumer is disconnected
         * /
        public void push(final Any data) throws Disconnected {
            try {
                lock.readLock().lock();
                Event event = EventHelper.extract(data);
                for (TopicListener listener : listeners) {
                    listener.receiveMessage(event.id, event.type);
                }
            } finally {
                lock.readLock().unlock();
            }
        }
    }
*/
}
