/*
 * @(#) DefaultComponentFactory.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import org.apache.log4j.Logger;

/**
 * This class is a factory for components.
 *
 * @author Chris Wareham
 */
public class DefaultComponentFactory implements ComponentFactory, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultComponentFactory.class);

    /**
     * The resource resolver.
     */
    private ResourceResolver resourceResolver;
    /**
     * The component resource name.
     */
    private String componentResource;
    /**
     * The component parameters.
     */
    private final Map<String, ComponentParameters> componentParameters = new LinkedHashMap<>();
    /**
     * The components.
     */
    private final Map<String, Object> components = new LinkedHashMap<>();
    /**
     * The component listeners.
     */
    private final List<ComponentListener> listeners = new ArrayList<>();

    /**
     * Set the resource resolver.
     *
     * @param rr the resource resolver
     */
    public void setResourceResolver(final ResourceResolver rr) {
        resourceResolver = rr;
    }

    /**
     * Set the component resource name.
     *
     * @param cr the component resource name
     */
    public void setComponentResource(final String cr) {
        componentResource = cr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws ComponentException {
        try {
            ComponentParametersHandler handler = new ComponentParametersHandler();
            handler.setResourceResolver(resourceResolver);
            handler.setComponentResource(componentResource);
            handler.setComponentParameters(componentParameters);
            handler.parse();
        } catch (SAXException exception) {
            throw new ComponentException("Error parsing component parameters", exception);
        }

        ComponentAnnotationParser parser = new ComponentAnnotationParser();

        for (ComponentParameters parameters : componentParameters.values()) {
            parser.parse(parameters);

            if (parameters.getInstantiation() == ComponentInstantiation.IMMEDIATE) {
                getComponent(parameters.getId());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        for (String name : components.keySet()) {
            Object component = components.get(name);

            try {
                ComponentParameters parameters = componentParameters.get(name);
                if (parameters.getShutdown() != null) {
                    ComponentUtils.call(component, parameters.getType(), parameters.getShutdown());
                }
            } catch (ComponentException exception) {
                LOGGER.error("stop(): error shutting down component '" + name + "'", exception);
            }

            try {
                if (component instanceof LifecycleComponent) {
                    ((LifecycleComponent) component).stop();
                }
            } catch (RuntimeException exception) {
                LOGGER.error("stop(): error stopping component '" + name + "'", exception);
            }
        }

        components.clear();

        componentParameters.clear();

        System.gc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isComponent(final String name) {
        return components.containsKey(name) || componentParameters.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean isComponent(final String name, final Class<T> type) {
        if (components.containsKey(name)) {
            Object component = components.get(name);
            return type.isInstance(component);
        }

        if (componentParameters.containsKey(name)) {
            ComponentParameters parameters = componentParameters.get(name);
            return type.isAssignableFrom(parameters.getType());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getComponentNames() {
        return Collections.unmodifiableCollection(componentParameters.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getComponent(final String name) throws ComponentException {
        if (!isComponent(name)) {
            throw new ComponentException("Error getting component id '" + name + "', not found");
        }

        return getObject(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getComponent(final String name, final Class<T> type) throws ComponentException {
        if (!isComponent(name, type)) {
            throw new ComponentException("Error getting component id '" + name + "' of type '" + type.getName() + "', not found");
        }

        return type.cast(getObject(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponentListener(final ComponentListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponentListener(final ComponentListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fire an event notifying listeners a component has been created.
     *
     * @param component the component that has been created
     * @throws ComponentException if an error occurs
     */
    private void fireComponentCreated(final Object component) throws ComponentException {
        for (ComponentListener listener : listeners) {
            listener.componentCreated(component);
        }
    }

    /**
     * Get a component.
     *
     * @param name the name of the component
     * @return the component
     * @throws ComponentException if an error occurs
     */
    private synchronized Object getObject(final String name) throws ComponentException {
        Object component = null;

        if (components.containsKey(name)) {
            component = components.get(name);
        } else if (componentParameters.containsKey(name)) {
            ComponentParameters parameters = componentParameters.get(name);

            component = createComponent(parameters);

            components.put(name, component);

            if (component instanceof ComponentFactoryAware) {
                ((ComponentFactoryAware) component).setComponentFactory(this);
            }

            if (component instanceof LifecycleComponent) {
                ((LifecycleComponent) component).start();
            }

            if (parameters.getStartup() != null) {
                ComponentUtils.call(component, parameters.getType(), parameters.getStartup());
            }

            fireComponentCreated(component);

            if (component instanceof ComponentListener) {
                addComponentListener((ComponentListener) component);
            }
        }

        return component;
    }

    /**
     * Create a component.
     *
     * @param parameters the component parameters
     * @return the component
     * @throws ComponentException if an error occurs
     */
    private Object createComponent(final ComponentParameters parameters) throws ComponentException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("createComponent(): creating component id:[" + parameters.getId() + "] class:[" + parameters.getType() + "]");
        }

        Class<?> type = parameters.getType();

        Map<String, Method> setters = ComponentUtils.getSetters(type);

        Map<String, Method> adders = ComponentUtils.getAdders(type);

        Map<String, Method> putters = ComponentUtils.getPutters(type);

        Object component = null;

        try {
            component = type.getConstructor().newInstance();
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error creating component id '" + parameters.getId() + "', failed to construct '" + parameters.getType() + "' class", exception);
        }

        for (String name : parameters.getReferenceNames()) {
            String value = parameters.getReference(name);
            Object referent = getComponent(value);
            ComponentUtils.setReference(component, setters, name, referent);
        }

        for (String name : parameters.getPropertyNames()) {
            String value = parameters.getProperty(name);
            ComponentUtils.setProperty(component, setters, name, value);
        }

        for (String name : parameters.getListReferenceNames()) {
            List<String> values = parameters.getListReference(name);
            List<Object> referents = new ArrayList<>(values.size());
            for (String value : values) {
                referents.add(getComponent(value));
            }
            ComponentUtils.addReferences(component, adders, name, referents);
        }

        for (String name : parameters.getListPropertyNames()) {
            List<String> values = parameters.getListProperty(name);
            ComponentUtils.addProperties(component, adders, name, values);
        }

        for (String name : parameters.getMapReferenceNames()) {
            Map<String, List<String>> values = parameters.getMapReference(name);
            Map<String, List<Object>> referents = new LinkedHashMap<>(values.size());
            for (String key : values.keySet()) {
                List<Object> objects = new ArrayList<>();
                for (String value : values.get(key)) {
                    objects.add(getComponent(value));
                }
                referents.put(key, objects);
            }
            ComponentUtils.putReferences(component, putters, name, referents);
        }

        for (String name : parameters.getMapPropertyNames()) {
            Map<String, List<String>> values = parameters.getMapProperty(name);
            ComponentUtils.putProperties(component, putters, name, values);
        }

        return component;
    }
}
