/*
 * @(#) VariablesActionInterceptor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class provides an interceptor that adds variables to a response.
 *
 * @author Chris Wareham
 */
public class VariablesActionInterceptor implements ActionInterceptor {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(VariablesActionInterceptor.class);

    /**
     * The variables to add to a response.
     */
    private final Map<String, String> variables = new HashMap<>();

    /**
     * Add a variable.
     *
     * @param key the key of the variable
     * @param value the value of the variable
     */
    public void putVariable(final String key, final String value) {
        variables.put(key, value);
    }

    /**
     * Add variables to a response.
     *
     * @param request holds data for the request
     * @param chain the chain of interceptors
     * @return the view name and models for the response
     * @throws ActionException if an error occurs
     */
    @Override
    public ActionResponse intercept(final ActionRequest request, final ActionInterceptorChain chain) throws ActionException {
        LOGGER.debug("intercept(): variables interceptor called");

        ActionResponse response = chain.intercept(request);

        for (String key : variables.keySet()) {
            response.addModel(key, variables.get(key));
        }

        LOGGER.debug("intercept(): returning from variables interceptor");

        return response;
    }
}
