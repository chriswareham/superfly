/*
 * @(#) ComponentUtils.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides utilities for introspecting components.
 *
 * @author Chris Wareham
 */
public final class ComponentUtils {
    /**
     * Utility class - no public constructor.
     */
    private ComponentUtils() {
        // empty
    }

    /**
     * Call a method.
     *
     * @param component the component to call a method for
     * @param type the type of the component
     * @param name the name of the method to call
     * @throws ComponentException if an error occurs
     */
    public static void call(final Object component, final Class<?> type, final String name) throws ComponentException {
        try {
            Method method = type.getMethod(name);
            method.invoke(component);
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error calling method '" + name + "'", exception);
        }
    }

    /**
     * Get the setter methods of a component. Setter methods are those with a
     * name beginning with set, that return void, and accept a single argument.
     *
     * @param type the type to get the setter methods for
     * @return the setter methods keyed by name
     * @throws ComponentException if an error occurs
     */
    public static Map<String, Method> getSetters(final Class<?> type) throws ComponentException {
        Map<String, Method> setters = new HashMap<>();

        for (Method method : type.getMethods()) {
            String name = method.getName();
            if (name.startsWith("set")) {
                if (name.length() == 3) {
                    throw new ComponentException("Error getting setter '" + name + "' for '" + type.getName() + "', invalid name");
                }

                Class<?> returnType = method.getReturnType();
                if (returnType != void.class) {
                    throw new ComponentException("Error getting setter '" + name + "' for '" + type.getName() + "', does not return void");
                }

                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    throw new ComponentException("Error getting setter '" + name + "' for '" + type.getName() + "', does not accept one argument");
                }

                String setterName = parameterName(name);

                if (setters.containsKey(setterName)) {
                    throw new ComponentException("Error getting setter '" + name + "' for '" + type.getName() + "', overloaded name");
                }

                setters.put(setterName, method);
            }
        }

        return setters;
    }

    /**
     * Get the adder methods of a component. Adder methods are those with a name
     * name beginning with add, that return void, and accept a single argument.
     *
     * @param type the type to get the adder methods for
     * @return the adder methods keyed by name
     * @throws ComponentException if an error occurs
     */
    public static Map<String, Method> getAdders(final Class<?> type) throws ComponentException {
        Map<String, Method> adders = new HashMap<>();

        for (Method method : type.getMethods()) {
            String name = method.getName();
            if (name.startsWith("add")) {
                if (name.length() == 3) {
                    throw new ComponentException("Error getting adder '" + name + "' for '" + type.getName() + "', invalid name");
                }

                Class<?> returnType = method.getReturnType();
                if (returnType != void.class) {
                    throw new ComponentException("Error getting adder '" + name + "' for '" + type.getName() + "', does not return void");
                }

                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    throw new ComponentException("Error getting adder '" + name + "' for '" + type.getName() + "', does not accept one argument");
                }

                String adderName = parameterName(name);

                if (adders.containsKey(adderName)) {
                    throw new ComponentException("Error getting adder '" + name + "' for '" + type.getName() + "', overloaded name");
                }

                adders.put(adderName, method);
            }
        }

        return adders;
    }

    /**
     * Get the putter methods of a component. Putter methods are those with a
     * name beginning with put, that return void, and accept two arguments.
     *
     * @param type the type to get the putter methods for
     * @return the putter methods keyed by name
     * @throws ComponentException if an error occurs
     */
    public static Map<String, Method> getPutters(final Class<?> type) throws ComponentException {
        Map<String, Method> putters = new HashMap<>();

        for (Method method : type.getMethods()) {
            String name = method.getName();
            if (name.startsWith("put")) {
                if (name.length() == 3) {
                    throw new ComponentException("Error getting putter '" + name + "' for '" + type.getName() + "', invalid name");
                }

                Class<?> returnType = method.getReturnType();
                if (returnType != void.class) {
                    throw new ComponentException("Error getting putter '" + name + "' for '" + type.getName() + "', does not return void");
                }

                Class<?>[] types = method.getParameterTypes();
                if (types.length != 2) {
                    throw new ComponentException("Error getting putter '" + name + "' for '" + type.getName() + "', does not accept two arguments");
                }

                String putterName = parameterName(name);

                if (putters.containsKey(putterName)) {
                    throw new ComponentException("Error getting putter '" + name + "' for '" + type.getName() + "', overloaded name");
                }

                putters.put(putterName, method);
            }
        }

        return putters;
    }

    /**
     * Set a component reference.
     *
     * @param component the component to set the reference for
     * @param setters the setter methods
     * @param reference the reference name
     * @param referent the component the reference refers to
     * @throws ComponentException if an error occurs
     */
    public static void setReference(final Object component, final Map<String, Method> setters, final String reference, final Object referent) throws ComponentException {
        Method setter = setters.get(reference);

        if (setter == null) {
            throw new ComponentException("Error setting reference '" + reference + "' for '" + component.getClass().getName() + "', no setter");
        }

        try {
            setter.invoke(component, referent);
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error setting reference '" + reference + "' for '" + component.getClass().getName() + "', failed to invoke setter", exception);
        }
    }

    /**
     * Set a component property.
     *
     * @param component the component to set the property for
     * @param setters the setter methods
     * @param property the property name
     * @param value the property value
     * @throws ComponentException if an error occurs
     */
    public static void setProperty(final Object component, final Map<String, Method> setters, final String property, final String value) throws ComponentException {
        Method setter = setters.get(property);

        if (setter == null) {
            throw new ComponentException("Error setting property '" + property + "' for '" + component.getClass().getName() + "', no setter");
        }

        Class<?> type = setter.getParameterTypes()[0];

        try {
            invoke(component, setter, type, value);
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error setting property '" + property + "' for '" + component.getClass().getName() + "', failed to invoke setter", exception);
        }
    }

    /**
     * Add component references.
     *
     * @param component the component to add the references for
     * @param adders the adder methods
     * @param reference the reference name
     * @param referents the components the reference refers to
     * @throws ComponentException if an error occurs
     */
    public static void addReferences(final Object component, final Map<String, Method> adders, final String reference, final List<Object> referents) throws ComponentException {
        Method adder = adders.get(reference);

        if (adder == null) {
            throw new ComponentException("Error adding references '" + reference + "' for '" + component.getClass().getName() + "', no adder");
        }

        try {
            for (Object referent : referents) {
                adder.invoke(component, referent);
            }
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error adding references '" + reference + "' for '" + component.getClass().getName() + "', failed to invoke adder", exception);
        }
    }

    /**
     * Add component properties.
     *
     * @param component the component to add the property for
     * @param adders the adder methods
     * @param property the property name
     * @param values the property values
     * @throws ComponentException if an error occurs
     */
    public static void addProperties(final Object component, final Map<String, Method> adders, final String property, final List<String> values) throws ComponentException {
        Method adder = adders.get(property);

        if (adder == null) {
            throw new ComponentException("Error adding property '" + property + "' for '" + component.getClass().getName() + "', no adder");
        }

        Class<?> type = adder.getParameterTypes()[0];

        try {
            for (String value : values) {
                invoke(component, adder, type, value);
            }
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error adding property '" + property + "' for '" + component.getClass().getName() + "', failed to invoke adder", exception);
        }
    }

    /**
     * Put component references.
     *
     * @param component the component to put the references for
     * @param putters the putter methods
     * @param reference the reference name
     * @param referents the components the reference refers to
     * @throws ComponentException if an error occurs
     */
    public static void putReferences(final Object component, final Map<String, Method> putters, final String reference, final Map<String, List<Object>> referents) throws ComponentException {
        Method putter = putters.get(reference);

        if (putter == null) {
            throw new ComponentException("Error putting references '" + reference + "' for '" + component.getClass().getName() + "', no putter");
        }

        try {
            for (String key : referents.keySet()) {
                for (Object referent : referents.get(key)) {
                    putter.invoke(component, key, referent);
                }
            }
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error putting references '" + reference + "' for '" + component.getClass().getName() + "', failed to invoke putter", exception);
        }
    }

    /**
     * Put component properties.
     *
     * @param component the component to put the properties for
     * @param putters the putter methods
     * @param property the property name
     * @param values the property values
     * @throws ComponentException if an error occurs
     */
    public static void putProperties(final Object component, final Map<String, Method> putters, final String property, final Map<String, List<String>> values) throws ComponentException {
        Method putter = putters.get(property);

        if (putter == null) {
            throw new ComponentException("Error putting properties '" + property + "' for '" + component.getClass().getName() + "', no putter");
        }

        Class<?> keyType = putter.getParameterTypes()[0];
        Class<?> valueType = putter.getParameterTypes()[1];

        try {
            for (String key : values.keySet()) {
                for (String value : values.get(key)) {
                    invoke(component, putter, keyType, valueType, key, value);
                }
            }
        } catch (ReflectiveOperationException | IllegalArgumentException exception) {
            throw new ComponentException("Error putting properties '" + property + "' for '" + component.getClass().getName() + "', failed to invoke putter", exception);
        }
    }

    /**
     * Invoke a method.
     *
     * @param component the component to invoke the method on
     * @param method the method to invoke
     * @param type the property type
     * @param value the property value
     * @throws ReflectiveOperationException if an error occurs
     */
    private static void invoke(final Object component, final Method method, final Class<?> type, final String value) throws ReflectiveOperationException {
        Object obj = null;

        if (type == String.class) {
            obj = value;
        } else if (type == boolean.class) {
            obj = Boolean.valueOf(value);
        } else if (type == int.class) {
            obj = Integer.valueOf(value);
        } else if (type == long.class) {
            obj = Long.valueOf(value);
        } else if (type == float.class) {
            obj = Float.valueOf(value);
        } else if (type == double.class) {
            obj = Double.valueOf(value);
        } else {
            throw new IllegalArgumentException("Unsupported parameter type " + type.getName());
        }

        method.invoke(component, obj);
    }

    /**
     * Invoke a method.
     *
     * @param component the component to invoke the method on
     * @param method the method to invoke
     * @param keyType the key property type
     * @param valueType the value property type
     * @param key the property key
     * @param value the property value
     * @throws ReflectiveOperationException if an error occurs
     */
    private static void invoke(final Object component, final Method method, final Class<?> keyType, final Class<?> valueType, final String key, final String value) throws ReflectiveOperationException {
        Object keyObj = null;

        if (keyType == String.class) {
            keyObj = key;
        } else if (keyType == boolean.class) {
            keyObj = Boolean.valueOf(key);
        } else if (keyType == int.class) {
            keyObj = Integer.valueOf(key);
        } else if (keyType == long.class) {
            keyObj = Long.valueOf(key);
        } else if (keyType == float.class) {
            keyObj = Float.valueOf(key);
        } else if (keyType == double.class) {
            keyObj = Double.valueOf(key);
        } else {
            throw new IllegalArgumentException("Unsupported key parameter type " + keyType.getName());
        }

        Object valueObj = null;

        if (valueType == String.class) {
            valueObj = value;
        } else if (valueType == boolean.class) {
            valueObj = Boolean.valueOf(value);
        } else if (valueType == int.class) {
            valueObj = Integer.valueOf(value);
        } else if (valueType == long.class) {
            valueObj = Long.valueOf(value);
        } else if (valueType == float.class) {
            valueObj = Float.valueOf(value);
        } else if (valueType == double.class) {
            valueObj = Double.valueOf(value);
        } else {
            throw new IllegalArgumentException("Unsupported value parameter type " + valueType.getName());
        }

        method.invoke(component, keyObj, valueObj);
    }

    /**
     * Get a parameter name from a method name.
     *
     * @param methodName the method name
     * @return the parameter name
     */
    public static String parameterName(final String methodName) {
        StringBuilder buf = new StringBuilder(methodName.substring(3));
        buf.setCharAt(0, Character.toLowerCase(methodName.charAt(3)));
        return buf.toString();
    }
}
