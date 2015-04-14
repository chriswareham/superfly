/*
 * @(#) DiskCacheTest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.io.File;
import java.io.Serializable;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This class provides a unit test for the disk cache.
 *
 * @author Chris Wareham
 */
public class DiskCacheTest {
    /**
     * The cache directory.
     */
    private static final String CACHE_DIRECTORY = "target/cache";
    /**
     * The purge time.
     */
    private static final int PURGE_TIME = 5;
    /**
     * The purge frequency.
     */
    private static final int PURGE_FREQUENCY = 1;

    /**
     * The disk cache.
     */
    private DiskCache<Integer, TestObject> cache;

    /**
     * Setup the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        cache = new DiskCache<>();
        cache.setCacheDirectory(CACHE_DIRECTORY);
        cache.setPurgeTime(PURGE_TIME);
        cache.setPurgeFrequency(PURGE_FREQUENCY);
        cache.start();
    }

    /**
     * Teardown the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        cache.flush();
        cache.stop();

        File cacheDirectory = new File(CACHE_DIRECTORY);
        cacheDirectory.delete();
    }

    /**
     * Test the DiskCache::store() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testStore() throws Exception {
        TestObject obj = new TestObject(1);
        cache.store(obj.getId(), obj);
    }

    /**
     * Test the DiskCache::fetch() method.
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
     * Test the DiskCache::flushAndStore() method.
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
     * Test the DiskCache::flush() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFlush() throws Exception {
        TestObject obj = new TestObject(3);
        cache.store(obj.getId(), obj);
        cache.flush(obj.getId());
        TestObject objRead = cache.fetch(obj.getId());
        Assert.assertTrue(objRead == null);
    }

    /**
     * Test the DiskCache::flush() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFlushAll() throws Exception {
        TestObject obj = new TestObject(3);
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
