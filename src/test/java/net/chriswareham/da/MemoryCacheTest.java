/*
 * @(#) MemoryCacheTest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.io.Serializable;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This class provides a unit test for the memory cache.
 *
 * @author Chris Wareham
 */
public class MemoryCacheTest {
    /**
     * The memory cache.
     */
    private MemoryCache<Integer, TestObject> cache;

    /**
     * Setup the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        cache = new MemoryCache<>();
        cache.start();
    }

    /**
     * Teardown the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        cache.stop();
    }

    /**
     * Test the MemoryCache::store() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testStore() throws Exception {
        TestObject obj = new TestObject(1);
        cache.store(obj.getId(), obj);
    }

    /**
     * Test the MemoryCache::fetch() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFetch() throws Exception {
        TestObject obj = new TestObject(2);
        cache.store(obj.getId(), obj);
        TestObject objRead = cache.fetch(obj.getId());
        Assert.assertTrue(objRead != null);
    }

    /**
     * Test the MemoryCache::flushAndStore() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFlushAndStore() throws Exception {
        TestObject obj = new TestObject(3);
        cache.store(obj.getId(), obj);
        cache.flushAndStore(obj.getId(), obj);
        TestObject objRead = cache.fetch(obj.getId());
        Assert.assertTrue(objRead != null);
    }

    /**
     * Test the MemoryCache::flush() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFlush() throws Exception {
        TestObject obj = new TestObject(4);
        cache.store(obj.getId(), obj);
        cache.flush(obj.getId());
        TestObject objRead = cache.fetch(obj.getId());
        Assert.assertTrue(objRead == null);
    }

    /**
     * Test the MemoryCache::flush() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFlushAll() throws Exception {
        TestObject obj = new TestObject(5);
        cache.store(obj.getId(), obj);
        cache.flush();
        TestObject objRead = cache.fetch(obj.getId());
        Assert.assertTrue(objRead == null);
    }

    /**
     * This class provides a test object suitable for caching.
     */
    private static class TestObject implements Serializable {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The id of the test object.
         */
        private final int id;

        /**
         * Constructs a new instance of a test object.
         *
         * @param i the id of the test object
         */
        TestObject(final int i) {
            id = i;
        }

        /**
         * Get the id of the test object.
         *
         * @return the id of the test object
         */
        public int getId() {
            return id;
        }
    }
}
