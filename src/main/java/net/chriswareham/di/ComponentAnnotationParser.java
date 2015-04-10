/*
 * @(#) ComponentAnnotationParser.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.lang.reflect.Method;

/**
 * This class provides an annotation parser that checks for required component
 * parameters.
 *
 * @author Chris Wareham
 */
public class ComponentAnnotationParser {
    /**
     * Check for required component parameters.
     *
     * @param parameters the component parameters
     * @throws ComponentException if any required component parameters are missing
     */
    public void parse(final ComponentParameters parameters) throws ComponentException {
        StringBuilder errors = new StringBuilder();

        Class<?> type = parameters.getType();

        for (Method method : type.getMethods()) {
            if (method.isAnnotationPresent(Required.class)) {
                String name = method.getName();

                if (name.startsWith("set")) {
                    String parameter = ComponentUtils.parameterName(name);
                    if (!parameters.containsProperty(parameter) && !parameters.containsReference(parameter)) {
                        appendError(errors, parameter, "required property or reference");
                    }
                } else if (name.startsWith("add")) {
                    String parameter = ComponentUtils.parameterName(name);
                    if (!parameters.containsListProperty(parameter) && !parameters.containsListReference(parameter)) {
                        appendError(errors, parameter, "required list property or reference");
                    }
                } else if (name.startsWith("put")) {
                    String parameter = ComponentUtils.parameterName(name);
                    if (!parameters.containsMapProperty(parameter) && !parameters.containsMapReference(parameter)) {
                        appendError(errors, parameter, "required map property or reference");
                    }
                } else {
                    appendError(errors, name, "invalid property or reference method");
                }
            }
        }

        if (errors.length() > 0) {
            throw new ComponentException("Component " + parameters.getId() + ": " + errors);
        }
    }

    /**
     * Append a component parameter error to a buffer.
     *
     * @param buf the buffer to append to
     * @param method the method name
     * @param error the component parameter error
     */
    private static void appendError(final StringBuilder buf, final String method, final String error) {
        if (buf.length() > 1) {
            buf.append(", ");
        }
        buf.append(error);
        buf.append(' ');
        buf.append(method);
    }
}
