/*
 * @(#) ManagedComponent.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * This interface is implemented by components that can be managed via JMX.
 *
 * @author Chris Wareham
 */
public interface ManagedComponent {
    /**
     * Get the managed name of the component.
     *
     * @return the managed name of the component
     */
    String getManagedName();
}
