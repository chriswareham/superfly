/*
 * @(#) SerialiserServiceTest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This class provides a unit test for methods that methods that serialise and
 * deserialise objects to and from strings.
 *
 * @author Chris Wareham
 */
public class SerialiserServiceTest {
    /**
     * The serialiser service.
     */
    private SerialiserService serialiserService;

    /**
     * Setup the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        SerialiserServiceImpl impl = new SerialiserServiceImpl();
        impl.setKey(impl.generateKey());
        serialiserService = impl;
    }

    /**
     * Teardown the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        serialiserService = null;
    }

    /**
     * Test the serialise method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testSerialise() throws Exception {
        String testString = "TEST STRING";
        String serialised = serialiserService.serialise(testString);
        Assert.assertFalse("Serialised string should not be empty", serialised.isEmpty());
    }

    /**
     * Test the deserialise method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testDeserialise() throws Exception {
        String testString = "TEST STRING";
        String serialised = serialiserService.serialise(testString);
        String deserialised = serialiserService.deserialise(serialised, String.class);
        Assert.assertEquals("Deserialised string should match test string", testString, deserialised);
    }
}
