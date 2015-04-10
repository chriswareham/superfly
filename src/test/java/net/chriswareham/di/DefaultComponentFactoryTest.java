/*
 * @(#) DefaultComponentFactoryTest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This class provides a unit test for a factory for components.
 *
 * @author Chris Wareham
 */
public class DefaultComponentFactoryTest {
    /**
     * The component resource.
     */
    private static final String COMPONENT_RESOURCE = "/testcomponents.xml";
    /**
     * The component name.
     */
    private static final String COMPONENT_NAME = "component";
    /**
     * The prototype component name.
     */
    private static final String PROTOTYPE_COMPONENT_NAME = "prototypeComponent";
    /**
     * The include component name.
     */
    private static final String INCLUDE_COMPONENT_NAME = "includeComponent";
    /**
     * The unknown component name.
     */
    private static final String UNKNOWN_COMPONENT_NAME = "unknownComponent";

    /**
     * The component factory.
     */
    private DefaultComponentFactory componentFactory;

    /**
     * Setup the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        componentFactory = new DefaultComponentFactory();
        componentFactory.setResourceResolver(new ClassPathResourceResolver());
        componentFactory.setComponentResource(COMPONENT_RESOURCE);
        componentFactory.start();
    }

    /**
     * Teardown the test fixture.
     *
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        componentFactory.stop();
    }

    /**
     * Test the ComponentFactory::getComponentNames() method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetComponentNames() throws Exception {
        Assert.assertFalse("Component names should not be empty", componentFactory.getComponentNames().isEmpty());
    }

    /**
     * Test the ComponentFactory::isComponent(String) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsComponent() throws Exception {
        Assert.assertTrue("Component should exist", componentFactory.isComponent(COMPONENT_NAME));
    }

    /**
     * Test the ComponentFactory::isComponent(String) method for a prototype component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsPrototypeComponent() throws Exception {
        Assert.assertTrue("Prototype component should exist", componentFactory.isComponent(PROTOTYPE_COMPONENT_NAME));
    }

        /**
     * Test the ComponentFactory::isComponent(String) method for an include component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsIncludeComponent() throws Exception {
        Assert.assertTrue("Include component should exist", componentFactory.isComponent(INCLUDE_COMPONENT_NAME));
    }

        /**
     * Test the ComponentFactory::isComponent(String) method for an unknown component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsUnknownComponent() throws Exception {
        Assert.assertFalse("Unknown component should not exist", componentFactory.isComponent(UNKNOWN_COMPONENT_NAME));
    }

    /**
     * Test the ComponentFactory::isComponent(String, Class) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsComponentClass() throws Exception {
        Assert.assertTrue("Component should exist", componentFactory.isComponent(COMPONENT_NAME, TestComponent.class));
    }

    /**
     * Test the ComponentFactory::isComponent(String, Class) method fails for an incorrect class.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsComponentClassFails() throws Exception {
        Assert.assertFalse("Component should not exist for incorrect class", componentFactory.isComponent(COMPONENT_NAME, String.class));
    }

    /**
     * Test the ComponentFactory::isComponent(String, Class) method for a prototype component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsPrototypeComponentClass() throws Exception {
        Assert.assertTrue("Prototype component should exist", componentFactory.isComponent(PROTOTYPE_COMPONENT_NAME, TestComponent.class));
    }

    /**
     * Test the ComponentFactory::isComponent(String, Class) method for an include component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsIncludeComponentClass() throws Exception {
        Assert.assertTrue("Include component should exist", componentFactory.isComponent(INCLUDE_COMPONENT_NAME, TestComponent.class));
    }

    /**
     * Test the ComponentFactory::isComponent(String, Class) method for an unknown component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsUnknownComponentClass() throws Exception {
        Assert.assertFalse("Unknown component should not exist", componentFactory.isComponent(UNKNOWN_COMPONENT_NAME, TestComponent.class));
    }

    /**
     * Test the ComponentFactory::getComponent(String) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetComponent() throws Exception {
        Assert.assertNotNull("Component should exist", componentFactory.getComponent(COMPONENT_NAME));
    }

    /**
     * Test the ComponentFactory::getComponent(String) method for a prototype component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetPrototypeComponent() throws Exception {
        Assert.assertNotNull("Prototype component should exist", componentFactory.getComponent(PROTOTYPE_COMPONENT_NAME));
    }

    /**
     * Test the ComponentFactory::getComponent(String) method for an include component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetIncludeComponent() throws Exception {
        Assert.assertNotNull("Include component should exist", componentFactory.getComponent(INCLUDE_COMPONENT_NAME));
    }

    /**
     * Test the ComponentFactory::getComponent(String) method for an unknown component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetUnknownComponent() throws Exception {
        try {
            componentFactory.getComponent(UNKNOWN_COMPONENT_NAME);
            Assert.fail("Unknown component should not exist");
        } catch (ComponentException exception) {
            Assert.assertTrue(true);
        }
    }

    /**
     * Test the ComponentFactory::getComponent(String, Class) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetComponentClass() throws Exception {
        Assert.assertNotNull("Component should exist", componentFactory.getComponent(COMPONENT_NAME, TestComponent.class));
    }

    /**
     * Test the ComponentFactory::getComponent(String, Class) method fails for an incorrect class.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetComponentClassFails() throws Exception {
        try {
            componentFactory.getComponent(COMPONENT_NAME, String.class);
            Assert.fail("Component should not exist for incorrect class");
        } catch (ComponentException exception) {
            Assert.assertTrue(true);
        }
    }

    /**
     * Test the ComponentFactory::getComponent(String, Class) method for a prototype component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetPrototypeComponentClass() throws Exception {
        Assert.assertNotNull("Prototype component should exist", componentFactory.getComponent(PROTOTYPE_COMPONENT_NAME, TestComponent.class));
    }

    /**
     * Test the ComponentFactory::getComponent(String, Class) method for an include component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetIncludeComponentClass() throws Exception {
        Assert.assertNotNull("Include component should exist", componentFactory.getComponent(INCLUDE_COMPONENT_NAME, TestComponent.class));
    }

    /**
     * Test the ComponentFactory::getComponent(String, Class) method for an unknown component.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetUnknownComponentClass() throws Exception {
        try {
            componentFactory.getComponent(UNKNOWN_COMPONENT_NAME, TestComponent.class);
            Assert.fail("Unknown component should not exist");
        } catch (ComponentException exception) {
            Assert.assertTrue(true);
        }
    }
}
