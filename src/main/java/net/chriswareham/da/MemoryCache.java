/*
 * @(#) MemoryCache.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import net.chriswareham.di.LifecycleComponent;

/**
 * This class supports the storing and fetching of objects from a memory cache.
 * It also provides an event listener which flushes objects on receipt of an
 * update or delete event.
 *
 * @param <I> the type of object that is used to identify cached objects
 * @param <T> the type of cached objects
 * @author Chris Wareham
 */
public class MemoryCache<I, T> implements Cache<I, T>, TopicListener, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MemoryCache.class);

    /**
     * Whether the cache is running.
     */
    private volatile boolean running;
    /**
     * The read-write lock.
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * The object cache map, keyed on id.
     */
    private final Map<Key<I>, T> cache = new HashMap<>();
    /**
     * How long an object is cached before the purger thread flushes it.
     */
    private long purgeTime;
    /**
     * How long between runs of the purger thread.
     */
    private long purgeFrequency;
    /**
     * The purger thread.
     */
    private Thread purger;
    /**
     * The event service.
     */
    private EventService eventService;

    /**
     * Set the event service.
     *
     * @param es the event service
     */
    public void setEventService(final EventService es) {
        eventService = es;
    }

    /**
     * Set the number of minutes an object is cached before the purge thread flushes it.
     *
     * @param pt the number of minutes an object is cached before the purge thread flushes it
     */
    public void setPurgeTime(final int pt) {
        purgeTime = 60000L * pt;
    }

    /**
     * Set the number of minutes between runs of the purge thread.
     *
     * @param pf the number of minutes between runs of the purge thread
     */
    public void setPurgeFrequency(final int pf) {
        purgeFrequency = 60000L * pf;
    }

    /**
     * Fetches all objects.
     *
     * @return the objects
     */
    public List<T> fetch() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("fetch(): all");
        }

        List<T> objs = Collections.emptyList();

        if (running) {
            try {
                lock.readLock().lock();
                if (!cache.isEmpty()) {
                    objs = new ArrayList<>(cache.values());
                }
            } finally {
                lock.readLock().unlock();
            }
        }

        return objs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T fetch(final I id) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("fetch(): id:[" + id + "]");
        }

        T obj = null;

        if (running) {
            try {
                lock.readLock().lock();
                Key<I> key = new Key<>(id);
                if (cache.containsKey(key)) {
                    obj = cache.get(key);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("fetch(): id:[" + id + "] not cached");
                }
            } finally {
                lock.readLock().unlock();
            }
        }

        return obj;
    }

    /**
     * Stores objects.
     *
     * @param objs the objects to store
     * @param idFetcher the id fetcher for the objects
     */
    public void store(final List<T> objs, final IdFetcher<I, T> idFetcher) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("store(): all");
        }

        if (running) {
            try {
                lock.writeLock().lock();
                for (T obj : objs) {
                    I id = idFetcher.fetchId(obj);
                    Key<I> key = new Key<>(id);
                    if (!cache.containsKey(key)) {
                        cache.put(key, obj);
                    } else if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("store(): id:[" + id + "] already cached");
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store(final I id, final T obj) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("store(): id:[" + id + "]");
        }

        if (running) {
            try {
                lock.writeLock().lock();
                Key<I> key = new Key<>(id);
                if (!cache.containsKey(key)) {
                    cache.put(key, obj);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("store(): id:[" + id + "] already cached");
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flushAndStore(final I id, final T obj) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("flushAndStore(): id:[" + id + "]");
        }

        if (running) {
            try {
                lock.writeLock().lock();
                Key<I> key = new Key<>(id);
                if (cache.containsKey(key)) {
                    cache.remove(key);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("flushAndStore(): id:[" + id + "] not cached");
                }
                cache.put(key, obj);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush(final I id) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("flush(): id:[" + id + "]");
        }

        if (running) {
            try {
                lock.writeLock().lock();
                Key<I> key = new Key<>(id);
                if (cache.containsKey(key)) {
                    cache.remove(key);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("flush(): id:[" + id + "] not cached");
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("flush(): all");
        }

        if (running) {
            try {
                lock.writeLock().lock();
                cache.clear();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * Starts the cache.
     */
    @Override
    public void start() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start(): starting cache");
        }

        if (running) {
            throw new IllegalStateException("Cache has already been started");
        }

        running = true;

        if (eventService != null) {
            eventService.addTopicListener("", this);
        }

        if (purgeFrequency > 0) {
            purger = new Purger();
            purger.start();
        }
    }

    /**
     * Stops the cache.
     */
    @Override
    public void stop() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("stop(): stopping cache");
        }

        if (!running) {
            throw new IllegalStateException("Cache has already been stopped");
        }

        running = false;

        if (eventService != null) {
            eventService.removeTopicListener("", this);
        }

        if (purger != null) {
            purger.interrupt();
            purger = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void receiveEvent(final Event event) {
        switch (event.getType()) {
        case UPDATE:
        case DELETE:
            flush((I) event.getId());
            break;
        }
    }

    /**
     * This class provides a purger thread.
     */
    private class Purger extends Thread {
        /**
         * The date formatter.
         */
        private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        /**
         * The purge thread run loop.
         */
        @Override
        public void run() {
            while (running) {
                try {
                    lock.writeLock().lock();

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("run(): purge started:[" + dateFormat.format(new Date()) + "] cache size:[" + cache.size() + "]");
                    }

                    if (purgeTime > 0) {
                        long expiry = System.currentTimeMillis() - purgeTime;
                        for (Iterator<Key<I>> i = cache.keySet().iterator(); i.hasNext();) {
                            if (i.next().expired(expiry)) {
                                i.remove();
                            }
                        }
                    } else {
                        cache.clear();
                    }

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("run(): purge started:[" + dateFormat.format(new Date()) + "] cache size:[" + cache.size() + "]");
                    }
                } finally {
                    lock.writeLock().unlock();
                }

                try {
                    Thread.sleep(purgeFrequency);
                } catch (InterruptedException exception) {
                    continue;
                }
            }
        }
    }

    /**
     * This class provides a wrapper for cached object identifiers that adds a
     * timestamp.
     *
     * @param <I> the type of object that is used to identify cached objects
     */
    private static class Key<I> {
        /**
         * The cached object identifier.
         */
        private final I key;
        /**
         * The cache timestamp.
         */
        private final long timestamp;

        /**
         * Construct an instance of the wrapper for a cached object identifier.
         *
         * @param k the cached object identifier
         */
        Key(final I k) {
            key = k;
            timestamp = System.currentTimeMillis();
        }

        /**
         * Get whether the cached object has expired.
         *
         * @param expiry the expiry time
         * @return whether the cached object has expired
         */
        public boolean expired(final long expiry) {
            return timestamp < expiry;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Key)) {
                return false;
            }
            Key<?> k = (Key) obj;
            return key != null ? key.equals(k.key) : k.key == null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return key != null ? key.hashCode() : 0;
        }
    }
}
