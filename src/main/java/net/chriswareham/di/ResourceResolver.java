/*
 * @(#) ResourceResolver.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.io.InputStream;

/**
 * This interface is implemented by classes that can resolve resource names to
 * an input stream.
 *
 * @author Chris Wareham
 */
public interface ResourceResolver {
    /**
     * Get the path of a resource.
     *
     * @param name the name of the resource
     * @return a path, or null if the resource cannot be found
     */
    String getResourcePath(String name);

    /**
     * Get a resource as an input stream.
     *
     * @param name the name of the resource
     * @return an input stream, or null if the resource cannot be found
     */
    InputStream getResource(String name);
}
