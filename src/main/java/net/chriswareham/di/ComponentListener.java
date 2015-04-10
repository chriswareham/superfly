/*
 * @(#) ComponentListener.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * This interface is implemented by classes that wish to be notified when a
 * component has been created.
 *
 * @author Chris Wareham
 */
public interface ComponentListener {
    /**
     * Called when a component has been created.
     *
     * @param obj the component that has been created
     * @throws ComponentException if an error occurs
     */
    void componentCreated(Object obj) throws ComponentException;
}
