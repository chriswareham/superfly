/*
 * @(#) Validator.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.List;

/**
 * This interface is implemented by validators.
 *
 * @author Chris Wareham
 */
public interface Validator {
    /**
     * Validate a value.
     *
     * @param value the value to validate
     * @return whether the value is valid
     */
    boolean validate(String value);

    /**
     * Validate values.
     *
     * @param values the values to validate
     * @return whether the values are valid
     */
    boolean validate(List<String> values);
}
