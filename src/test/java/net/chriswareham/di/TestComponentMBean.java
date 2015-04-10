/*
 * @(#) TestComponentMBean.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * This class is an interface for a managed test component.
 *
 * @author Chris Wareham
 */
public interface TestComponentMBean extends ManagedComponent {
    /**
     * Get the name of the managed test component.
     *
     * @return the name of the managed test component
     */
    String getName();
}
