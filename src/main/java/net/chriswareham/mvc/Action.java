/*
 * @(#) Action.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

/**
 * This interface is implemented by classes that handles requests.
 *
 * @author Chris Wareham
 */
public interface Action {
    /**
     * Get the path of the action.
     *
     * @return the path of the action
     */
    String getPath();

    /**
     * Set the path of the action.
     *
     * @param p the path of the action
     */
    void setPath(String p);

    /**
     * Handle a request.
     *
     * @param request holds data for the request
     * @return the view name and models for the response
     * @throws ActionException if an error occurs
     */
    ActionResponse action(ActionRequest request) throws ActionException;
}
