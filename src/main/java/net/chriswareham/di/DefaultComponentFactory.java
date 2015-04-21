/*
 * @(#) DefaultComponentFactory.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.lang.reflect.Constructor;
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

        Object component = instantiateComponent(parameters);

        Map<String, Method> setters = ComponentUtils.getSetters(parameters.getType());

        for (String name : parameters.getPropertyNames()) {
            Method setter = setters.get(name);
            String value = parameters.getProperty(name);
            ComponentUtils.setProperty(component, setter, name, value);
        }

        for (String name : parameters.getReferenceNames()) {
            Method setter = setters.get(name);
            Object reference = getComponent(parameters.getReference(name));
            ComponentUtils.setReference(component, setter, name, reference);
        }

        Map<String, Method> adders = ComponentUtils.getAdders(parameters.getType());

        for (String name : parameters.getListPropertyNames()) {
            Method adder = adders.get(name);
            List<String> values = parameters.getListProperty(name);
            ComponentUtils.addProperties(component, adder, name, values);
        }

        for (String name : parameters.getListReferenceNames()) {
            Method adder = adders.get(name);
            List<Object> references = new ArrayList<>();
            for (String reference : parameters.getListReference(name)) {
                references.add(getComponent(reference));
            }
            ComponentUtils.addReferences(component, adder, name, references);
        }

        Map<String, Method> putters = ComponentUtils.getPutters(parameters.getType());

        for (String name : parameters.getMapPropertyNames()) {
            Method putter = putters.get(name);
            Map<String, String> values = parameters.getMapProperty(name);
            ComponentUtils.putProperties(component, putter, name, values);
        }

        for (String name : parameters.getMapReferenceNames()) {
            Method putter = putters.get(name);
            Map<String, Object> references = new LinkedHashMap<>();
            for (Map.Entry<String, String> reference : parameters.getMapReference(name).entrySet()) {
                references.put(reference.getKey(), getComponent(reference.getValue()));
            }
            ComponentUtils.putReferences(component, putter, name, references);
        }

        return component;
    }

    /**
     * Instantiate a component.
     *
     * @param parameters the component parameters
     * @return the component
     * @throws ComponentException if an error occurs
     */
    private Object instantiateComponent(final ComponentParameters parameters) throws ComponentException {
        Object component = null;

        try {
            Class<?> type = parameters.getType();

            List<ConstructorArg> args = parameters.getConstructorArgs();
            if (args.isEmpty()) {
                component = type.getConstructor().newInstance();
            } else {
                Constructor<?>[] constructors = type.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    if (matches(constructor, args)) {
                        Object[] objs = new Object[args.size()];
                        for (int i = 0; i < args.size(); ++i) {
                            objs[i] = args.get(i).getArg(this);
                        }
                        component = constructor.newInstance(objs);
                        break;
                    }
                }
                if (component == null) {
                    throw new IllegalArgumentException("No matching constructor");
                }
            }
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error creating component id '" + parameters.getId() + "', failed to construct '" + parameters.getType() + "' class", exception);
        }

        return component;
    }

    /**
     * Get whether a constructor matches component arguments.
     *
     * @param constructor the constructor
     * @param args the component arguments
     * @return whether the constructor matches the component arguments
     */
    private boolean matches(final Constructor<?> constructor, final List<ConstructorArg> args) {
        Class<?>[] types = constructor.getParameterTypes();
        if (types.length != args.size()) {
            return false;
        }
        for (int i = 0; i < args.size(); ++i) {
            if (!types[i].isAssignableFrom(args.get(i).getType())) {
                return false;
            }
        }
        return true;
    }
}
