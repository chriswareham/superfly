/*
 * @(#) ComponentInstantiation.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

/**
 * This enumeration defines the component instantiation strategies.
 *
 * @author Chris Wareham
 */
public enum ComponentInstantiation {
    /**
     * Immediate instantiation strategy.
     */
    IMMEDIATE,
    /**
     * Demand instantiation strategy.
     */
    DEMAND;
}
