/*
 * @(#) ConstructorArg.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * This interface is implemented by constructor arguments.
 *
 * @author Chris Wareham
 */
public interface ConstructorArg {
    /**
     * Get the constructor argument type.
     *
     * @return the constructor argument type
     */
    Class<?> getType();

    /**
     * Get the constructor argument instance.
     *
     * @param cf the component factory
     * @return the constructor argument instance
     * @throws ComponentException if an error occurs
     */
    Object getArg(ComponentFactory cf) throws ComponentException;
}
