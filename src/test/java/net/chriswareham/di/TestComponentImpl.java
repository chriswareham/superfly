/*
 * @(#) TestComponentImpl.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class is a test component.
 *
 * @author Chris Wareham
 */
public class TestComponentImpl implements TestComponent, TestComponentMBean, ComponentFactoryAware, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TestComponent.class);

    /**
     * The component factory.
     */
    private ComponentFactory componentFactory;
    /**
     * The managed name.
     */
    private String managedName;
    /**
     * The reference property.
     */
    private TestComponent reference;
    /**
     * The prototype reference property.
     */
    private TestComponent prototypeReference;
    /**
     * The string property.
     */
    private String stringProperty;
    /**
     * The boolean property.
     */
    private boolean booleanProperty;
    /**
     * The integer property.
     */
    private int intProperty;
    /**
     * The long property.
     */
    private long longProperty;
    /**
     * The float property.
     */
    private float floatProperty;
    /**
     * The double property.
     */
    private double doubleProperty;
    /**
     * The reference collection.
     */
    private final List<TestComponent> referenceCollection = new ArrayList<>();
    /**
     * The string collection.
     */
    private final List<String> stringCollection = new ArrayList<>();
    /**
     * The boolean collection.
     */
    private final List<Boolean> booleanCollection = new ArrayList<>();
    /**
     * The integer collection.
     */
    private final List<Integer> intCollection = new ArrayList<>();
    /**
     * The long collection.
     */
    private final List<Long> longCollection = new ArrayList<>();
    /**
     * The float collection.
     */
    private final List<Float> floatCollection = new ArrayList<>();
    /**
     * The double collection.
     */
    private final List<Double> doubleCollection = new ArrayList<>();
    /**
     * The reference map.
     */
    private final Map<String, TestComponent> referenceMap = new HashMap<>();
    /**
     * The string map.
     */
    private final Map<String, String> stringMap = new HashMap<>();
    /**
     * The boolean map.
     */
    private final Map<Boolean, Boolean> booleanMap = new HashMap<>();
    /**
     * The integer map.
     */
    private final Map<Integer, Integer> intMap = new HashMap<>();
    /**
     * The long map.
     */
    private final Map<Long, Long> longMap = new HashMap<>();
    /**
     * The float map.
     */
    private final Map<Float, Float> floatMap = new HashMap<>();
    /**
     * The double map.
     */
    private final Map<Double, Double> doubleMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComponentFactory(final ComponentFactory cf) {
        componentFactory = cf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        LOGGER.debug("Component started");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        LOGGER.debug("Component stopped");
    }

    /**
     * The startup method.
     */
    public void startup() {
        LOGGER.debug("Component startup");
    }

    /**
     * The shutdown method.
     */
    public void shutdown() {
        LOGGER.debug("Component shutdown");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Managed Test Component";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getManagedName() {
        return managedName;
    }

    /**
     * Set the managed name.
     *
     * @param mn the managed name
     */
    public void setManagedName(final String mn) {
        managedName = mn;
    }

    /**
     * Set the reference property.
     *
     * @param r the reference
     */
    public void setReference(final TestComponent r) {
        reference = r;
    }

    /**
     * Set the prototype reference property.
     *
     * @param pr the prototype reference
     */
    public void setPrototypeReference(final TestComponent pr) {
        prototypeReference = pr;
    }

    /**
     * Set the string property.
     *
     * @param sp the string
     */
    public void setStringProperty(final String sp) {
        stringProperty = sp;
    }

    /**
     * Set the boolean property.
     *
     * @param bp the boolean
     */
    public void setBooleanProperty(final boolean bp) {
        booleanProperty = bp;
    }

    /**
     * Set the integer property.
     *
     * @param ip the integer
     */
    public void setIntProperty(final int ip) {
        intProperty = ip;
    }

    /**
     * Set the long property.
     *
     * @param lp the long
     */
    public void setLongProperty(final long lp) {
        longProperty = lp;
    }

    /**
     * Set the float property.
     *
     * @param fp the float
     */
    public void setFloatProperty(final float fp) {
        floatProperty = fp;
    }

    /**
     * Set the double property.
     *
     * @param dp the double
     */
    public void setDoubleProperty(final double dp) {
        doubleProperty = dp;
    }

    /**
     * Add a reference.
     *
     * @param r the reference
     */
    public void addReferenceCollection(final TestComponent r) {
        referenceCollection.add(r);
    }

    /**
     * Add a string.
     *
     * @param s the string
     */
    public void addStringCollection(final String s) {
        stringCollection.add(s);
    }

    /**
     * Add a boolean.
     *
     * @param b the boolean
     */
    public void addBooleanCollection(final boolean b) {
        booleanCollection.add(b);
    }

    /**
     * Add an integer.
     *
     * @param i the integer
     */
    public void addIntCollection(final int i) {
        intCollection.add(i);
    }

    /**
     * Add a long.
     *
     * @param l the long
     */
    public void addLongCollection(final long l) {
        longCollection.add(l);
    }

    /**
     * Add a float.
     *
     * @param f the float
     */
    public void addFloatCollection(final float f) {
        floatCollection.add(f);
    }

    /**
     * Add a double.
     *
     * @param d the double
     */
    public void addDoubleCollection(final double d) {
        doubleCollection.add(d);
    }

    /**
     * Add a reference.
     *
     * @param k the key
     * @param r the reference
     */
    public void putReferenceMap(final String k, final TestComponent r) {
        referenceMap.put(k, r);
    }

    /**
     * Add a string.
     *
     * @param k the key
     * @param s the string
     */
    public void putStringMap(final String k, final String s) {
        stringMap.put(k, s);
    }

    /**
     * Add a boolean.
     *
     * @param k the key
     * @param b the boolean
     */
    public void putBooleanMap(final boolean k, final boolean b) {
        booleanMap.put(k, b);
    }

    /**
     * Add an integer.
     *
     * @param k the key
     * @param i the integer
     */
    public void putIntMap(final int k, final int i) {
        intMap.put(k, i);
    }

    /**
     * Add a long.
     *
     * @param k the key
     * @param l the long
     */
    public void putLongMap(final long k, final long l) {
        longMap.put(k, l);
    }

    /**
     * Add a float.
     *
     * @param k the key
     * @param f the float
     */
    public void putFloatMap(final float k, final float f) {
        floatMap.put(k, f);
    }

    /**
     * Add a double.
     *
     * @param k the key
     * @param d the double
     */
    public void putDoubleMap(final double k, final double d) {
        doubleMap.put(k, d);
    }
}
