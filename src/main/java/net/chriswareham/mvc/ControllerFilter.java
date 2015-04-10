/*
 * @(#) ControllerFilter.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.chriswareham.di.ComponentException;
import net.chriswareham.di.ComponentFactory;

/**
 * This class provides a filter that implements the Front Controller pattern
 * from "Core J2EE Patterns" (ISBN 0-1314-2246-4).
 *
 * @author Chris Wareham
 */
public class ControllerFilter implements Filter {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ControllerFilter.class);
    /**
     * The default request character encoding.
     */
    private static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * The component factory attribute name.
     */
    private static final String COMPONENT_FACTORY_ATTRIBUTE = "componentFactory";

    /**
     * The servlet context.
     */
    private ServletContext context;
    /**
     * The character encoding to set each request to.
     */
    private String encoding;
    /**
     * The request processor.
     */
    private DefaultRequestProcessor requestProcessor;

    /**
     * Reads an encoding parameter from the filter parameters and initialises a
     * request processor. The optional encoding parameter indicates a character
     * encoding to set each request to.
     *
     * <h4>Parameters</h4>
     *
     * <ul>
     *   <li><tt>encoding</tt> - the character encoding to set each request to</li>
     * </ul>
     *
     * @param config the filter configuration
     * @throws ServletException if an error occurs
     */
    @Override
    public void init(final FilterConfig config) throws ServletException {
        context = config.getServletContext();

        encoding = config.getInitParameter("encoding");

        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("init(): encoding:[" + encoding + "]");
        }

        ComponentFactory componentFactory = (ComponentFactory) context.getAttribute(COMPONENT_FACTORY_ATTRIBUTE);

        if (componentFactory == null) {
            throw new ServletException("Component factory not found");
        }

        try {
            // initialise the request processor

            requestProcessor = new DefaultRequestProcessor();
            requestProcessor.setComponentFactory(componentFactory);
            requestProcessor.start();
        } catch (RuntimeException | ComponentException exception) {
            throw new ServletException("Failed to initialise the request processor", exception);
        }
    }

    /**
     * Shuts down the request processor.
     */
    @Override
    public void destroy() {
        if (requestProcessor != null) {
            requestProcessor.stop();
            requestProcessor = null;
        }
    }

    /**
     * Process a request.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the invocation chain
     * @throws IOException if an input or output error occurs
     * @throws ServletException if an error occurs processing the request
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding(encoding);

        // Set standard HTTP/1.0 no caching header.
        res.setHeader("Pragma", "no-cache");
        // Set standard HTTP/1.1 no caching header.
        res.setHeader("Cache-Control", "no-store, no-cache");

        requestProcessor.process(req, res, context);
    }
}
