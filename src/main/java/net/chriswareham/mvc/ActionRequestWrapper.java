/*
 * @(#) ActionRequestWrapper.java
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
 * This class provides a wrapper for action requests.
 *
 * @author Chris Wareham
 */
public class ActionRequestWrapper implements ActionRequest {
    /**
     * The action request to wrap.
     */
    private final ActionRequest request;

    /**
     * Constructs a new instance of the ActionRequestWrapper.
     *
     * @param r the request to wrap
     */
    public ActionRequestWrapper(final ActionRequest r) {
        request = r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Locale getLocale() {
        return request.getLocale();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return request.getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPathInfo() {
        return request.isPathInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPathInfo() {
        return request.getPathInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean isAttribute(final String name, final Class<T> type) {
        return request.isAttribute(name, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAttributeNames() {
        return request.getAttributeNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getAttribute(final String name, final Class<T> type) {
        return request.getAttribute(name, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAttribute(final String name, final Object attribute) {
        request.addAttribute(name, attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAttribute(final String name) {
        request.removeAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHeader(final String name) {
        return request.isHeader(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getHeaderNames() {
        return request.getHeaderNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeader(final String name) {
        return request.getHeader(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getHeaders(final String name) {
        return request.getHeaders(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isParameter(final String name) {
        return request.isParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getParameterNames() {
        return request.getParameterNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanParameter(final String name) {
        return request.getBooleanParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntegerParameter(final String name) {
        return request.getIntegerParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntegerParameter(final String name, final int def) {
        return request.getIntegerParameter(name, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleParameter(final String name) {
        return request.getDoubleParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleParameter(final String name, final double def) {
        return request.getDoubleParameter(name, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimeParameter(final String name) {
        return request.getTimeParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimeParameter(final String name, final Date def) {
        return request.getTimeParameter(name, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateParameter(final String name) {
        return request.getDateParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateParameter(final String name, final Date def) {
        return request.getDateParameter(name, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringParameter(final String name) {
        return request.getStringParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringParameter(final String name, final String def) {
        return request.getStringParameter(name, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getStringParameters(final String name) {
        return request.getStringParameters(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCookie(final String name) {
        return request.isCookie(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getCookieNames() {
        return request.getCookieNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cookie getCookie(final String name) {
        return request.getCookie(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Cookie> getCookies() {
        return request.getCookies();
    }
}
