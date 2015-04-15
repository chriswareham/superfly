/*
 * @(#) DiskCache.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import net.chriswareham.di.LifecycleComponent;

/**
 * This class supports the storing and fetching of objects from a disk cache.
 * It also provides a purge thread which flushes objects that have been in the
 * cache longer than a configurable expiration time, and an event listener which
 * flushes objects on receipt of an update or delete event.
 *
 * @param <I> the type of object that is used to identify cached objects
 * @param <T> the type of cached objects
 * @author Chris Wareham
 */
public class DiskCache<I, T> implements Cache<I, T>, TopicListener, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DiskCache.class);

    /**
     * Whether the cache is running.
     */
    private volatile boolean running;
    /**
     * The read-write lock.
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * The directory to cache objects to.
     */
    private File cacheDirectory;
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
     * Set the directory to cache objects to.
     *
     * @param cd the directory to cache objects to
     */
    public void setCacheDirectory(final String cd) {
        cacheDirectory = new File(cd);
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
     * Set the event service.
     *
     * @param es the event service
     */
    public void setEventService(final EventService es) {
        eventService = es;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T fetch(final I id) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("fetch(): id:[" + id + "]");
        }

        Object obj = null;

        if (running) {
            try {
                lock.readLock().lock();
                obj = fetchImpl(id);
            } catch (ClassNotFoundException exception) {
                LOGGER.error("fetch(): error fetching id:[" + id + "]", exception);
            } finally {
                lock.readLock().unlock();
            }
        }

        if (obj != null) {
            // not sure if it's possible to make this type-safe
            @SuppressWarnings("unchecked") T t = (T) obj;
            return t;
        }

        return null;
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
                storeImpl(id, obj);
            } catch (IOException exception) {
                LOGGER.error("store(): error storing id:[" + id + "]", exception);
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
                flushImpl(id);
                storeImpl(id, obj);
            } catch (IOException exception) {
                LOGGER.error("flushAndStore(): error flushing and storing id:[" + id + "]", exception);
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
                flushImpl(id);
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
                if (cacheDirectory.exists()) {
                    for (File file : cacheDirectory.listFiles()) {
                        file.delete();
                    }
                }
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

        if (cacheDirectory.exists()) {
            if (!cacheDirectory.isDirectory()) {
                cacheDirectory.delete();
                cacheDirectory.mkdirs();
            }
        } else {
            cacheDirectory.mkdirs();
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
     * Fetches an object.
     *
     * @param id the id of the object to fetch
     * @return the object, or null if it is not cached
     * @throws ClassNotFoundException if the class of the serialised object cannot be found
     */
    private Object fetchImpl(final I id) throws ClassNotFoundException {
        Object obj = null;

        File file = new File(cacheDirectory, id.toString());

        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                obj = inputStream.readObject();
            } catch (IOException exception) {
                LOGGER.warn("fetchImpl(): id:[" + id + "] file read failed:[" + exception.getMessage() + "]");
                file.delete();
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("fetchImpl(): id:[" + id + "] not cached");
        }

        return obj;
    }

    /**
     * Stores an object.
     *
     * @param id the id of the object to fetch
     * @param obj the object to store
     * @throws IOException if an input or output error occurs
     */
    private void storeImpl(final I id, final T obj) throws IOException {
        File file = new File(cacheDirectory, id.toString());

        if (!file.exists()) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(obj);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("storeImpl(): id:[" + id + "] already cached");
            }
            file.setLastModified(System.currentTimeMillis());
        }
    }

    /**
     * Flushes an object.
     *
     * @param id the id of the object to flush
     */
    private void flushImpl(final I id) {
        File file = new File(cacheDirectory, id.toString());

        if (file.exists()) {
            file.delete();
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("flushImpl(): id:[" + id + "] not cached");
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
                        LOGGER.debug("run(): purge started:[" + dateFormat.format(new Date()) + "]");
                    }

                    if (cacheDirectory.exists()) {
                        long now = System.currentTimeMillis();

                        for (File file : cacheDirectory.listFiles()) {
                            if (file.lastModified() + purgeTime < now) {
                                if (LOGGER.isTraceEnabled()) {
                                    LOGGER.trace("run(): purging file:[" + file.getName() + "]");
                                }
                                file.delete();
                            }
                        }
                    }

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("run(): purge finished:[" + dateFormat.format(new Date()) + "]");
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
}
