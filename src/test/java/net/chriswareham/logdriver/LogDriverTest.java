/*
 * @(#) LogDriverTest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.logdriver;

import org.junit.Assert;
import org.junit.Test;

/**
 * This class provides tests for the logging JDBC driver.
 *
 * @author Chris Wareham
 */
public class LogDriverTest {
    /**
     * The instance to test.
     */
    private final LogDriver logDriver = new LogDriver();

    /**
     * Test the driver accepts valid URLs.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testAcceptsUrlValid() throws Exception {
        Assert.assertTrue(logDriver.acceptsURL("jdbc:log:xxx"));
    }

    /**
     * Test the driver does not accept invalid URLs.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testAcceptsUrlInvalid() throws Exception {
        Assert.assertFalse(logDriver.acceptsURL("xxx:log:xxx"));
    }
}
