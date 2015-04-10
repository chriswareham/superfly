/*
 * @(#) ActionInterceptor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

/**
 * This interface is implemented by classes that intercept requests.
 *
 * @author Chris Wareham
 */
public interface ActionInterceptor {
    /**
     * Intercept a request.
     *
     * @param request holds data for the request
     * @param chain the chain of interceptors
     * @return the view name and models for the response
     * @throws ActionException if an error occurs
     */
    ActionResponse intercept(ActionRequest request, ActionInterceptorChain chain) throws ActionException;
}
