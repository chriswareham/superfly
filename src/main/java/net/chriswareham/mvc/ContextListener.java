/*
 * @(#) ContextListener.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import net.chriswareham.di.ComponentException;
import net.chriswareham.di.DefaultComponentFactory;

/**
 * Responds to servlet context events to start and stop the component framework.
 *
 * @author Chris Wareham
 */
public class ContextListener implements ServletContextListener {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ContextListener.class);
    /**
     * The components parameter name.
     */
    private static final String COMPONENTS_PARAMETER = "components";
    /**
     * The component factory attribute name.
     */
    private static final String COMPONENT_FACTORY_ATTRIBUTE = "componentFactory";

    /**
     * The component factory.
     */
    private DefaultComponentFactory componentFactory;

    /**
     * Reads a components parameter from the servlet context parameters and
     * initialises a component factory. The components parameter indicates the
     * name of a resource containing component declarations, and the initialised
     * component factory is then set as a context attribute.
     *
     * <h4>Parameters</h4>
     *
     * <ul>
     *   <li><tt>components</tt> the name of the resource containing component declarations</li>
     * </ul>
     *
     * <h4>Context Attributes</h4>
     *
     * <ul>
     *   <li><tt>componentFactory</tt> - the component factory</li>
     * </ul>
     *
     * @param event the servlet context event
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        String components = context.getInitParameter(COMPONENTS_PARAMETER);

        if (components == null) {
            throw new IllegalStateException("A 'components' parameter is required");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("contextInitialized(): components:[" + components + "]");
        }

        try {
            ServletContextResourceResolver resourceResolver = new ServletContextResourceResolver();
            resourceResolver.setServletContext(context);

            // initialise the component factory

            componentFactory = new DefaultComponentFactory();
            componentFactory.setResourceResolver(resourceResolver);
            componentFactory.setComponentResource(components);
            componentFactory.start();

            // add the component factory to the servlet context

            context.setAttribute(COMPONENT_FACTORY_ATTRIBUTE, componentFactory);
        } catch (RuntimeException | ComponentException exception) {
            throw new IllegalStateException("Failed to initialise the component factory", exception);
        }
    }

    /**
     * Shuts down the component factory.
     *
     * @param event the servlet context event
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        context.removeAttribute(COMPONENT_FACTORY_ATTRIBUTE);

        if (componentFactory != null) {
            componentFactory.stop();
            componentFactory = null;
        }
    }
}
