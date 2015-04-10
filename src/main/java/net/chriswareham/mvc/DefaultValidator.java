/*
 * @(#) DefaultValidator.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.List;

/**
 * This class provides a default validator optionally checks the number and
 * length of values.
 *
 * @author Chris Wareham
 */
public class DefaultValidator implements Validator {
    /**
     * The minimum number of values.
     */
    private int minValues;
    /**
     * The maximum number of values.
     */
    private int maxValues;
    /**
     * The minimum length of a value.
     */
    private int minLen;
    /**
     * The maximum length of a value.
     */
    private int maxLen;

    /**
     * Set the minimum number of values.
     *
     * @param mv the minimum number of values
     */
    public void setMinValues(final int mv) {
        minValues = mv;
    }

    /**
     * Set the maximum number of values.
     *
     * @param mv the maximum number of values
     */
    public void setMaxValues(final int mv) {
        maxValues = mv;
    }

    /**
     * Set the minimum length of a value.
     *
     * @param ml the minimum length of a value
     */
    public void setMinLen(final int ml) {
        minLen = ml;
    }

    /**
     * Set the maximum length of a value.
     *
     * @param ml the maximum length of a value
     */
    public void setMaxLen(final int ml) {
        maxLen = ml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(final String value) {
        if (minValues > 0 && value.isEmpty()) {
            return false;
        }
        if ((minLen > 0 && value.length() < minLen) || (maxLen > 0 && value.length() > maxLen)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(final List<String> values) {
        if (minValues > values.size()) {
            return false;
        }
        if (maxValues > 0 && values.size() > maxValues) {
            return false;
        }
        if (minLen > 0 || maxLen > 0) {
            for (String value : values) {
                if ((minLen > 0 && value.length() < minLen) || (maxLen > 0 && value.length() > maxLen)) {
                    return false;
                }
            }
        }
        return true;
    }
}
