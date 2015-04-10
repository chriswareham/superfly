/*
 * @(#) MultiPartRequestHandler.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface is implemented by classes that handle multi-part requests.
 *
 * @author Chris Wareham
 */
public interface MultiPartRequestHandler {
    /**
     * Get whether a request is multi-part.
     *
     * @param request the request
     * @return whether the request is multi-part
     */
    boolean isMultiPartRequest(HttpServletRequest request);

    /**
     * Parse a multi-part request.
     *
     * @param request the request
     * @param actionRequest the action request to populate with multi-part data
     * @throws ActionException if an error occurs
     */
    void parseRequest(HttpServletRequest request, MutableActionRequest actionRequest) throws ActionException;
}
