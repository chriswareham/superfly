/*
 * @(#) DefaultAction.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import net.chriswareham.di.Required;

/**
 * This class provides a default action.
 *
 * @author Chris Wareham
 */
public class DefaultAction extends AbstractAction {
    /**
     * The name of the view.
     */
    private String viewName;

    /**
     * Set the name of the view.
     *
     * @param vn the name of the view
     */
    @Required
    public void setViewName(final String vn) {
        viewName = vn;
    }

    /**
     * Return a default response with the name of the view.
     *
     * @param request holds data for the request
     * @return the view name and models for the response
     * @throws ActionException if an error occurs
     */
    @Override
    public ActionResponse action(final ActionRequest request) throws ActionException {
        ActionResponse response = new ActionResponse();
        response.setViewName(viewName);
        return response;
    }
}
