/*
 * @(#) ComponentFactory.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.util.Collection;

/**
 * This interface is implemented by classes that are factories for components.
 *
 * @author Chris Wareham
 */
public interface ComponentFactory {
    /**
     * Get whether a component exists.
     *
     * @param name the name of the component
     * @return whether the component exists
     */
    boolean isComponent(String name);

    /**
     * Get whether a component of a specific type exists.
     *
     * @param <T> the type of the component
     * @param name the name of the component
     * @param type the type of the component
     * @return whether the component of the specific type exists
     */
    <T> boolean isComponent(String name, Class<T> type);

    /**
     * Get the component names.
     *
     * @return the components names
     */
    Collection<String> getComponentNames();

    /**
     * Get a component.
     *
     * @param name the name of the component
     * @return the component
     * @throws ComponentException if the component does not exist
     */
    Object getComponent(String name) throws ComponentException;

    /**
     * Get a component of a specific type.
     *
     * @param <T> the type of the component
     * @param name the name of the component
     * @param type the type of the component
     * @return the component
     * @throws ComponentException if the component of the specific type does not exist
     */
    <T> T getComponent(String name, Class<T> type) throws ComponentException;

    /**
     * Add a component listener.
     *
     * @param listener the component listener to add
     */
    void addComponentListener(final ComponentListener listener);

    /**
     * Remove a component listener.
     *
     * @param listener the component listener to remove
     */
    void removeComponentListener(final ComponentListener listener);
}
