/*
 * @(#) LifecycleComponent.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * This interface is implemented by classes that are components with a
 * lifecycle.
 *
 * @author Chris Wareham
 */
public interface LifecycleComponent {
    /**
     * Start the component lifecycle.
     *
     * @throws ComponentException if an error occurs
     */
    void start() throws ComponentException;

    /**
     * Stop the component lifecycle.
     */
    void stop();
}
