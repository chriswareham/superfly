/*
 * @(#) SerialiserTag.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc.tags;

import java.io.Serializable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.chriswareham.da.SerialiserService;
import net.chriswareham.di.ComponentException;
import net.chriswareham.di.ComponentFactory;

/**
 * This class provides a tag to serialise objects.
 *
 * @author Chris Wareham
 */
public class SerialiserTag extends SimpleTagSupport {
    /**
     * The name of the variable to set the serialised object to.
     */
    private String var;
    /**
     * The object to serialise.
     */
    private Serializable obj;

    /**
     * Set the name of the variable to set the editorial to.
     *
     * @param v the name of the variable to set the editorial to
     */
    public void setVar(final String v) {
        var = v;
    }

    /**
     * Set the object to serialise.
     *
     * @param o the object to serialise
     */
    public void setObj(final Serializable o) {
        obj = o;
    }

    /**
     * Serialise an object.
     *
     * @throws JspException if an error occurs
     */
    @Override
    public void doTag() throws JspException {
        try {
            PageContext pageContext = (PageContext) getJspContext();
            ComponentFactory componentFactory = (ComponentFactory) pageContext.getServletContext().getAttribute("componentFactory");
            SerialiserService serialiserService = componentFactory.getComponent("serialiser", SerialiserService.class);
            pageContext.setAttribute(var, serialiserService.serialise(obj));
        } catch (ComponentException exception) {
            throw new JspException("Failed to serialise object", exception);
        }
    }
}
