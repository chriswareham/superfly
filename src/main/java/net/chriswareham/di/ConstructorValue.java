/*
 * @(#) ConstructorValue.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.io.Serializable;

/**
 * This class provides a bean for storing a constructor value.
 *
 * @author Chris Wareham
 */
public class ConstructorValue implements ConstructorArg, Serializable {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The value type.
     */
    private final Class<?> type;
    /**
     * The value.
     */
    private final Object value;

    /**
     * Construct an instance of the constructor value bean.
     *
     * @param t the value type
     * @param v the value
     */
    public ConstructorValue(final Class<?> t, final Object v) {
        type = t;
        value = v;
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
        return value;
    }
}
