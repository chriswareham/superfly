/*
 * @(#) MutableActionRequest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * Interface to be implemented by mutable classes that hold parameters and other
 * data for a request.
 *
 * @author Chris Wareham
 */
public interface MutableActionRequest extends ActionRequest {
    /**
     * Set the remote address for the request.
     *
     * @param ra the remote address for the request
     */
    void setRemoteAddr(String ra);

    /**
     * Set the locale for the request.
     *
     * @param l the locale for the request
     */
    void setLocale(Locale l);

    /**
     * Set the path for the request.
     *
     * @param p the path for the request
     */
    void setPath(String p);

    /**
     * Set the path information for the request.
     *
     * @param pi the path information for the request
     */
    void setPathInfo(String pi);

    /**
     * Add a header for the request.
     *
     * @param name the header name
     * @param value the header value
     */
    void addHeader(String name, String value);

    /**
     * Set the headers for the request.
     *
     * @param h the headers for the request
     */
    void addHeaders(Map<String, String[]> h);

    /**
     * Add a parameter for the request.
     *
     * @param name the parameter name
     * @param value the parameter value
     */
    void addParameter(String name, String value);

    /**
     * Set the parameters for the request.
     *
     * @param p the parameters for the request
     */
    void addParameters(Map<String, String[]> p);

    /**
     * Set the cookies for the request.
     *
     * @param c the cookies for the request
     */
    void setCookies(Cookie[] c);
}
