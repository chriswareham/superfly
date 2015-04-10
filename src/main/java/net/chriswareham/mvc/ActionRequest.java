/*
 * @(#) ActionRequest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

/**
 * Interface to be implemented by classes that hold parameters and other data
 * for a request.
 *
 * @author Chris Wareham
 */
public interface ActionRequest {
    /**
     * Gets the remote address for the request.
     *
     * @return the remote address for the request
     */
    String getRemoteAddr();

    /**
     * Gets the locale for the request.
     *
     * @return the locale for the request
     */
    Locale getLocale();

    /**
     * Get the path for the request.
     *
     * @return the path for the request
     */
    String getPath();

    /**
     * Gets whether there is path information for the request.
     *
     * @return whether there is path information for the request
     */
    boolean isPathInfo();

    /**
     * Gets the path information for the request.
     *
     * @return the path information, or null if there is none
     */
    String getPathInfo();

    /**
     * Gets whether an attribute exists.
     *
     * @param <T> the type of the attribute
     * @param name the name of the attribute
     * @param type the type of the attribute
     * @return whether the attribute exists
     */
    <T> boolean isAttribute(String name, Class<T> type);

    /**
     * Gets all attribute names.
     *
     * @return the attribute names
     */
    Set<String> getAttributeNames();

    /**
     * Gets an attribute.
     *
     * @param <T> the type of the attribute
     * @param name the name of the attribute
     * @param type the type of the attribute
     * @return the attribute or null if it cannot be found or is of the wrong type
     */
    <T> T getAttribute(String name, Class<T> type);

    /**
     * Adds an attribute.
     *
     * @param name the name of the attribute
     * @param attribute the attribute
     */
    void addAttribute(String name, Object attribute);

    /**
     * Removes an attribute.
     *
     * @param name the name of the attribute
     */
    void removeAttribute(String name);

    /**
     * Gets whether a header exists.
     *
     * @param name the name of the header
     * @return whether the header exists
     */
    boolean isHeader(String name);

    /**
     * Gets all header names.
     *
     * @return the header names
     */
    Set<String> getHeaderNames();

    /**
     * Gets a header value.
     *
     * @param name the name of the header
     * @return the header value or null if the header is not set
     */
    String getHeader(String name);

    /**
     * Gets header values.
     *
     * @param name the name of the header
     * @return the header values
     */
    List<String> getHeaders(String name);

    /**
     * Gets whether a parameter exists.
     *
     * @param name the name of the parameter
     * @return whether the parameter exists
     */
    boolean isParameter(String name);

    /**
     * Gets all parameter names.
     *
     * @return the parameter names
     */
    Set<String> getParameterNames();

    /**
     * Gets a boolean value from a parameter. Returns false if a parameter of
     * the requested name does not exist, or if a boolean cannot be parsed from
     * the parameter string.
     *
     * @param name the name of the parameter
     * @return the boolean value of the parameter
     */
    boolean getBooleanParameter(String name);

    /**
     * Gets an integer value from a parameter. Returns 0 if a parameter of the
     * requested name does not exist, or if an integer cannot be parsed from the
     * parameter string.
     *
     * @param name the name of the parameter
     * @return the integer value of the parameter
     */
    int getIntegerParameter(String name);

    /**
     * Gets an integer value from a parameter. Returns the specified default
     * value if a parameter of the requested name does not exist, or if an
     * integer cannot be parsed from the parameter string.
     *
     * @param name the name of the parameter
     * @param def the default value
     * @return the integer value of the parameter
     */
    int getIntegerParameter(String name, int def);

    /**
     * Gets a double value from a parameter. Returns 0.0 if a parameter of the
     * requested name does not exist, or if a double cannot be parsed from the
     * parameter string.
     *
     * @param name the name of the parameter
     * @return the double value of the parameter
     */
    double getDoubleParameter(String name);

    /**
     * Gets a double value from a parameter. Returns the specified default
     * value if a parameter of the requested name does not exist, or if a
     * double cannot be parsed from the parameter string.
     *
     * @param name the name of the parameter
     * @param def the default value
     * @return the double value of the parameter
     */
    double getDoubleParameter(String name, double def);

    /**
     * Gets a time value from a parameter. Returns null if a parameter of the
     * requested name does not exist, or if a time cannot be parsed from the
     * parameter string.
     *
     * @param name the name of the parameter
     * @return the time value of the parameter
     */
    Date getTimeParameter(String name);

    /**
     * Gets a time value from a parameter. Returns the specified default value
     * if a parameter of the requested name does not exist, or if a time cannot
     * be parsed from the parameter string.
     *
     * @param name the name of the parameter
     * @param def the default value
     * @return the time value of the parameter
     */
    Date getTimeParameter(String name, Date def);

    /**
     * Gets a date value from a parameter. Returns null if a parameter of the
     * requested name does not exist, or if a date cannot be parsed from the
     * parameter string.
     *
     * @param name the name of the parameter
     * @return the date value of the parameter
     */
    Date getDateParameter(String name);

    /**
     * Gets a date value from a parameter. Returns the specified default value
     * if a parameter of the requested name does not exist, or if a date cannot
     * be parsed from the parameter string.
     *
     * @param name the name of the parameter
     * @param def the default value
     * @return the date value of the parameter
     */
    Date getDateParameter(String name, Date def);

    /**
     * Gets a string value from a parameter. Returns an empty string if a
     * parameter of the requested name does not exist.
     *
     * @param name the name of the parameter
     * @return the string value of the parameter
     */
    String getStringParameter(String name);

    /**
     * Gets a string value from a parameter. Returns the specified default
     * value if a parameter of the requested name does not exist.
     *
     * @param name the name of the parameter
     * @param def the default value
     * @return the string value of the parameter
     */
    String getStringParameter(String name, String def);

    /**
     * Gets a list of string values from a parameter. Returns an empty list if a
     * parameter of the requested name does not exist.
     *
     * @param name the name of the parameter
     * @return the list of string values of the parameter
     */
    List<String> getStringParameters(String name);

    /**
     * Gets whether a cookie exists.
     *
     * @param name the name of the cookie
     * @return whether the cookie exists
     */
    boolean isCookie(String name);

    /**
     * Gets all cookie names.
     *
     * @return the cookie names
     */
    Set<String> getCookieNames();

    /**
     * Gets a cookie for the request.
     *
     * @param name the name of the cookie
     * @return the cookie, or null if there isn't one
     */
    Cookie getCookie(String name);

    /**
     * Gets the cookies for the request.
     *
     * @return the cookies for the request
     */
    Map<String, Cookie> getCookies();
}
