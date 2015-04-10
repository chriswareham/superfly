/*
 * @(#) ImmutableDate.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Date;

/**
 * This class provides an immutable date object.
 *
 * @author Chris Wareham
 */
public class ImmutableDate extends Date {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an immutable date object.
     *
     * @param date the milliseconds since January 1, 1970, 00:00:00 GMT
     */
    public ImmutableDate(final long date) {
        super(date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(final long time) {
        throw new IllegalStateException("Immutable date");
    }

    /**
     * {@inheritDoc}
     * @deprecated As of JDK version 1.1
     */
    @Override
    @Deprecated
    public void setYear(final int year) {
        throw new IllegalStateException("Immutable date");
    }

    /**
     * {@inheritDoc}
     * @deprecated As of JDK version 1.1
     */
    @Override
    @Deprecated
    public void setMonth(final int month) {
        throw new IllegalStateException("Immutable date");
    }

    /**
     * {@inheritDoc}
     * @deprecated As of JDK version 1.1
     */
    @Override
    @Deprecated
    public void setDate(final int date) {
        throw new IllegalStateException("Immutable date");
    }

    /**
     * {@inheritDoc}
     * @deprecated As of JDK version 1.1
     */
    @Override
    @Deprecated
    public void setHours(final int hours) {
        throw new IllegalStateException("Immutable date");
    }

    /**
     * {@inheritDoc}
     * @deprecated As of JDK version 1.1
     */
    @Override
    @Deprecated
    public void setMinutes(final int minutes) {
        throw new IllegalStateException("Immutable date");
    }

    /**
     * {@inheritDoc}
     * @deprecated As of JDK version 1.1
     */
    @Override
    @Deprecated
    public void setSeconds(final int seconds) {
        throw new IllegalStateException("Immutable date");
    }
}
