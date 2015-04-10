/*
 * @(#) ClassPathResourceResolver.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.io.InputStream;
import java.net.URL;

/**
 * This class is a resolver for class path resources.
 *
 * @author Chris Wareham
 */
public class ClassPathResourceResolver implements ResourceResolver {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourcePath(final String name) {
        URL url = getClass().getResource(name);
        return url != null ? url.getPath() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResource(final String name) {
        return getClass().getResourceAsStream(name);
    }
}
