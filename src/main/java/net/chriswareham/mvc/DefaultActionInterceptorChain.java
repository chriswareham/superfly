/*
 * @(#) DefaultActionInterceptorChain.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class provides a default implementation of an action interceptor chain.
 *
 * @author Chris Wareham
 */
public class DefaultActionInterceptorChain implements ActionInterceptorChain {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultActionInterceptorChain.class);

    /**
     * The chain iterator.
     */
    private Iterator<ActionInterceptor> chain;
    /**
     * The optional action to call at the termination of the chain.
     */
    private Action terminatingAction;

    /**
     * Constructs a new instance of the default action interceptor chain.
     *
     * @param interceptors the interceptors to chain
     */
    public DefaultActionInterceptorChain(final List<ActionInterceptor> interceptors) {
        this(interceptors, null);
    }

    /**
     * Constructs a new instance of the default action interceptor chain.
     *
     * @param interceptors the interceptors to chain
     * @param action the action to call at the termination of the chain
     */
    public DefaultActionInterceptorChain(final List<ActionInterceptor> interceptors, final Action action) {
        chain = interceptors.iterator();
        terminatingAction = action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse intercept(final ActionRequest request) throws ActionException {
        LOGGER.debug("intercept(): interceptor chain called");

        ActionResponse response = null;
        if (chain.hasNext()) {
            response = chain.next().intercept(request, this);
        } else if (terminatingAction != null) {
            LOGGER.debug("intercept(): calling action");
            response = terminatingAction.action(request);
        }

        LOGGER.debug("intercept(): returned to interceptor chain");

        return response;
    }
}
