/*
 * @(#) ComponentParameters.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class provides a bean for storing component parameters.
 *
 * @author Chris Wareham
 */
public class ComponentParameters implements Serializable {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The id of the component.
     */
    private String id;
    /**
     * The type of the component.
     */
    private Class<?> type;
    /**
     * The instantiation strategy of the component.
     */
    private ComponentInstantiation instantiation;
    /**
     * The startup method of the component.
     */
    private String startup;
    /**
     * The shutdown method of the component.
     */
    private String shutdown;
    /**
     * The component constructor arguments.
     */
    private final List<ConstructorArg> constructorArgs = new ArrayList<>();
    /**
     * The component references.
     */
    private final Map<String, String> references = new LinkedHashMap<>();
    /**
     * The component properties.
     */
    private final Map<String, String> properties = new LinkedHashMap<>();
    /**
     * The component list references.
     */
    private final Map<String, List<String>> listReferences = new LinkedHashMap<>();
    /**
     * The component list properties.
     */
    private final Map<String, List<String>> listProperties = new LinkedHashMap<>();
    /**
     * The component map references.
     */
    private final Map<String, Map<String, String>> mapReferences = new LinkedHashMap<>();
    /**
     * The component map properties.
     */
    private final Map<String, Map<String, String>> mapProperties = new LinkedHashMap<>();

    /**
     * Get the id of the component.
     *
     * @return the id of the component
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id of the component.
     *
     * @param i the id of the component
     */
    public void setId(final String i) {
        id = i;
    }

    /**
     * Get the type of the component.
     *
     * @return the type of the component
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Set the type of the component.
     *
     * @param t the type of the component
     */
    public void setType(final Class<?> t) {
        type = t;
    }

    /**
     * Get the instantiation strategy of the component.
     *
     * @return the instantiation strategy of the component
     */
    public ComponentInstantiation getInstantiation() {
        return instantiation;
    }

    /**
     * Get the startup method of the component.
     *
     * @return the startup method of the component
     */
    public String getStartup() {
        return startup;
    }

    /**
     * Set the startup method of the component.
     *
     * @param s the startup method of the component
     */
    public void setStartup(final String s) {
        startup = s;
    }

    /**
     * Get the shutdown method of the component.
     *
     * @return the shutdown method of the component
     */
    public String getShutdown() {
        return shutdown;
    }

    /**
     * Set the shutdown method of the component.
     *
     * @param s the shutdown method of the component
     */
    public void setShutdown(final String s) {
        shutdown = s;
    }

    /**
     * Set the instantiation strategy of the component.
     *
     * @param i the instantiation strategy of the component
     */
    public void setInstantiation(final ComponentInstantiation i) {
        instantiation = i;
    }

    /**
     * Get the component constructor arguments.
     *
     * @return the constructor arguments
     */
    public List<ConstructorArg> getConstructorArgs() {
        return Collections.unmodifiableList(constructorArgs);
    }

    /**
     * Add a constructor argument.
     *
     * @param constructorArg the constructor argument to add
     */
    public void addConstructorArg(final ConstructorArg constructorArg) {
        constructorArgs.add(constructorArg);
    }

    /**
     * Get whether the component contains a parameter.
     *
     * @param name the parameter name
     * @return whether the component contains the parameter
     */
    public boolean containsParameter(final String name) {
        return containsReference(name) || containsProperty(name) || containsListProperty(name) || containsMapProperty(name);
    }

    /**
     * Get whether the component contains a reference.
     *
     * @param name the reference name
     * @return whether the component contains the reference
     */
    public boolean containsReference(final String name) {
        return references.containsKey(name);
    }

    /**
     * Get the component reference names.
     *
     * @return the component reference names
     */
    public Set<String> getReferenceNames() {
        return Collections.unmodifiableSet(references.keySet());
    }

    /**
     * Get a component reference.
     *
     * @param name the reference name
     * @return the component reference
     */
    public String getReference(final String name) {
        return references.get(name);
    }

    /**
     * Add a component reference.
     *
     * @param name the reference name
     * @param reference the component reference
     */
    public void addReference(final String name, final String reference) {
        references.put(name, reference);
    }

    /**
     * Get whether the component contains a property.
     *
     * @param name the property name
     * @return whether the component contains the property
     */
    public boolean containsProperty(final String name) {
        return properties.containsKey(name);
    }

    /**
     * Get the component property names.
     *
     * @return the component property names
     */
    public Set<String> getPropertyNames() {
        return Collections.unmodifiableSet(properties.keySet());
    }

    /**
     * Get a component property.
     *
     * @param name the property name
     * @return the component property
     */
    public String getProperty(final String name) {
        return properties.get(name);
    }

    /**
     * Add a component property.
     *
     * @param name the property name
     * @param property the component property
     */
    public void addProperty(final String name, final String property) {
        properties.put(name, property);
    }

    /**
     * Get whether the component contains a list reference.
     *
     * @param name the list reference name
     * @return whether the component contains the list reference
     */
    public boolean containsListReference(final String name) {
        return listReferences.containsKey(name);
    }

    /**
     * Get the component list reference names.
     *
     * @return the component list reference names
     */
    public Set<String> getListReferenceNames() {
        return Collections.unmodifiableSet(listReferences.keySet());
    }

    /**
     * Get a component list reference.
     *
     * @param name the list reference name
     * @return the component list reference
     */
    public List<String> getListReference(final String name) {
        return Collections.unmodifiableList(listReferences.get(name));
    }

    /**
     * Add a component list reference.
     *
     * @param name the list reference name
     * @param reference the component list reference
     */
    public void addListReference(final String name, final String reference) {
        if (!listReferences.containsKey(name)) {
            listReferences.put(name, new ArrayList<>());
        }
        listReferences.get(name).add(reference);
    }

    /**
     * Get whether the component contains a list property.
     *
     * @param name the list property name
     * @return whether the component contains the list property
     */
    public boolean containsListProperty(final String name) {
        return listProperties.containsKey(name);
    }

    /**
     * Get the component list property names.
     *
     * @return the component list property names
     */
    public Set<String> getListPropertyNames() {
        return Collections.unmodifiableSet(listProperties.keySet());
    }

    /**
     * Get a component list property.
     *
     * @param name the list property name
     * @return the component list property
     */
    public List<String> getListProperty(final String name) {
        return Collections.unmodifiableList(listProperties.get(name));
    }

    /**
     * Add a component list property.
     *
     * @param name the list property name
     * @param property the component list property
     */
    public void addListProperty(final String name, final String property) {
        if (!listProperties.containsKey(name)) {
            listProperties.put(name, new ArrayList<>());
        }
        listProperties.get(name).add(property);
    }

    /**
     * Get whether the component contains a map reference.
     *
     * @param name the reference name
     * @return whether the component contains the map reference
     */
    public boolean containsMapReference(final String name) {
        return mapReferences.containsKey(name);
    }

    /**
     * Get the component map reference names.
     *
     * @return the component map reference names
     */
    public Set<String> getMapReferenceNames() {
        return Collections.unmodifiableSet(mapReferences.keySet());
    }

    /**
     * Get a component map reference.
     *
     * @param name the map reference name
     * @return the component map reference
     */
    public Map<String, String> getMapReference(final String name) {
        return Collections.unmodifiableMap(mapReferences.get(name));
    }

    /**
     * Add a component map reference.
     *
     * @param name the map reference name
     * @param key the component map reference key
     * @param value the component map reference value
     */
    public void addMapReference(final String name, final String key, final String value) {
        if (!mapReferences.containsKey(name)) {
            mapReferences.put(name, new LinkedHashMap<>());
        }
        Map<String, String> values = mapReferences.get(name);
        values.put(key, value);
    }

    /**
     * Get whether the component contains a map property.
     *
     * @param name the property name
     * @return whether the component contains the map property
     */
    public boolean containsMapProperty(final String name) {
        return mapProperties.containsKey(name);
    }

    /**
     * Get the component map property names.
     *
     * @return the component map property names
     */
    public Set<String> getMapPropertyNames() {
        return Collections.unmodifiableSet(mapProperties.keySet());
    }

    /**
     * Get a component map property.
     *
     * @param name the map property name
     * @return the component map property
     */
    public Map<String, String> getMapProperty(final String name) {
        return Collections.unmodifiableMap(mapProperties.get(name));
    }

    /**
     * Add a component map property.
     *
     * @param name the map property name
     * @param key the component map property key
     * @param value the component map property value
     */
    public void addMapProperty(final String name, final String key, final String value) {
        if (!mapProperties.containsKey(name)) {
            mapProperties.put(name, new LinkedHashMap<>());
        }
        Map<String, String> values = mapProperties.get(name);
        values.put(key, value);
    }
}
