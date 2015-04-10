/*
 * @(#) ComponentFactoryAware.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * This interface is implemented by components that are aware of their component
 * factory.
 *
 * @author Chris Wareham
 */
public interface ComponentFactoryAware {
    /**
     * Set the component factory.
     *
     * @param cf the component factory
     */
    void setComponentFactory(ComponentFactory cf);
}
