/*
 * @(#) ActionInterceptorChain.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

/**
 * Interface to be implemented by classes that chain interceptors.
 *
 * @author Chris Wareham
 */
public interface ActionInterceptorChain {
    /**
     * Call the next interceptor in the chain or the action at the end of the chain.
     *
     * @param request holds data for the request
     * @return the view name and models for the response
     * @throws ActionException if an error occurs
     */
    ActionResponse intercept(ActionRequest request) throws ActionException;
}
