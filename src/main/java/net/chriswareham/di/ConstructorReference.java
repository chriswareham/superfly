/*
 * @(#) ConstructorReference.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.io.Serializable;

/**
 * This class provides a bean for storing a constructor reference.
 *
 * @author Chris Wareham
 */
public class ConstructorReference implements ConstructorArg, Serializable {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The reference type.
     */
    private final Class<?> type;
    /**
     * The reference name.
     */
    private final String name;

    /**
     * Construct an instance of the constructor reference bean.
     *
     * @param t the reference type
     * @param n the reference name
     */
    public ConstructorReference(final Class<?> t, final String n) {
        type = t;
        name = n;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getArg(final ComponentFactory cf) throws ComponentException {
        return cf.getComponent(name);
    }
}
