/*
 * @(#) ServletContextResourceResolver.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.InputStream;

import javax.servlet.ServletContext;

import net.chriswareham.di.ResourceResolver;

/**
 * This class is a resolver for servlet context resources.
 *
 * @author Chris Wareham
 */
public class ServletContextResourceResolver implements ResourceResolver {
    /**
     * The servlet context to resolve resources from.
     */
    private ServletContext servletContext;

    /**
     * Set the servlet context to resolve resources from.
     *
     * @param sc the servlet context to resolve resources from
     */
    public void setServletContext(final ServletContext sc) {
        servletContext = sc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourcePath(final String name) {
        return servletContext.getRealPath(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResource(final String name) {
        return servletContext.getResourceAsStream(name);
    }
}
