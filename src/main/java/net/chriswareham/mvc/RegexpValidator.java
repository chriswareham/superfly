/*
 * @(#) RegexpValidator.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.List;
import java.util.regex.Pattern;

/**
 * This class provides a validator that ensures values match a pattern.
 *
 * @author Chris Wareham
 */
public class RegexpValidator extends DefaultValidator {
    /**
     * The valid pattern.
     */
    private Pattern pattern;

    /**
     * Set the valid pattern.
     *
     * @param vp the valid pattern
     */
    public void setPattern(final String vp) {
        pattern = Pattern.compile(vp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(final String value) {
        if (!super.validate(value)) {
            return false;
        }
        if (!value.isEmpty() && !pattern.matcher(value).matches()) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(final List<String> values) {
        if (!super.validate(values)) {
            return false;
        }
        for (String value : values) {
            if (!pattern.matcher(value).matches()) {
                return false;
            }
        }
        return true;
    }
}
