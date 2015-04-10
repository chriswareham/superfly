/*
 * @(#) AbstractAction.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import net.chriswareham.di.Required;

/**
 * This class provides an abstract action.
 *
 * @author Chris Wareham
 */
public abstract class AbstractAction implements Action {
    /**
     * The path of the action.
     */
    private String path;

    /**
     * Get the path of the action.
     *
     * @return the path of the action
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Set the path of the action.
     *
     * @param p the path of the action
     */
    @Override
    @Required
    public void setPath(final String p) {
        path = p;
    }
}
