/*
 * @(#) DefaultActionRequest.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

/**
 * Class that holds parameters and other data for a request.
 *
 * @author Chris Wareham
 */
public class DefaultActionRequest implements MutableActionRequest {
    /**
     * The date format.
     */
    private static final String DATE_FORMAT = "d-M-yyyy";
    /**
     * The time format.
     */
    private static final String TIME_FORMAT = "HH:mm";

    /**
     * The remote address for the request.
     */
    private String remoteAddr;
    /**
     * The locale for the request.
     */
    private Locale locale;
    /**
     * The path of the request.
     */
    private String path;
    /**
     * The path information for the request.
     */
    private String pathInfo;
    /**
     * The attributes.
     */
    private Map<String, Object> attributes;
    /**
     * The headers for the request.
     */
    private Map<String, List<String>> headers;
    /**
     * The parameters for the request.
     */
    private Map<String, List<String>> parameters;
    /**
     * The cookies for the request.
     */
    private Map<String, Cookie> cookies;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRemoteAddr(final String ra) {
        remoteAddr = ra;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocale(final Locale l) {
        locale = l;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPath(final String p) {
        path = p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPathInfo() {
        return !pathInfo.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPathInfo(final String pi) {
        pathInfo = pi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean isAttribute(final String name, final Class<T> type) {
        return attributes != null && attributes.containsKey(name) && type.isInstance(attributes.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAttributeNames() {
        return attributes != null ? Collections.unmodifiableSet(attributes.keySet()) : Collections.<String>emptySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getAttribute(final String name, final Class<T> type) {
        T attribute = null;
        if (attributes != null && attributes.containsKey(name)) {
            attribute = type.cast(attributes.get(name));
        }
        return attribute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAttribute(final String name, final Object attribute) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(name, attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAttribute(final String name) {
        if (attributes != null) {
            attributes.remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHeader(final String name) {
        return headers != null && headers.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getHeaderNames() {
        return headers != null ? Collections.unmodifiableSet(headers.keySet()) : Collections.<String>emptySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeader(final String name) {
        String value = null;
        if (headers != null && headers.containsKey(name)) {
            value = headers.get(name).get(0);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getHeaders(final String name) {
        List<String> values = Collections.emptyList();
        if (headers != null && headers.containsKey(name)) {
            values = Collections.unmodifiableList(headers.get(name));
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHeader(final String name, final String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        List<String> values = headers.get(name);
        if (values == null) {
            values = new ArrayList<>();
            headers.put(name, values);
        }
        values.add(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHeaders(final Map<String, String[]> h) {
        for (String name : h.keySet()) {
            String[] values = h.get(name);
            for (String value : values) {
                addHeader(name, value);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isParameter(final String name) {
        return parameters != null && parameters.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getParameterNames() {
        return parameters != null ? Collections.unmodifiableSet(parameters.keySet()) : Collections.<String>emptySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanParameter(final String name) {
        return Boolean.parseBoolean(getStringParameter(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntegerParameter(final String name) {
        return getIntegerParameter(name, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntegerParameter(final String name, final int def) {
        try {
            return Integer.parseInt(getStringParameter(name));
        } catch (final NumberFormatException exception) {
            return def;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleParameter(final String name) {
        return getDoubleParameter(name, 0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleParameter(final String name, final double def) {
        try {
            return Double.parseDouble(getStringParameter(name));
        } catch (final NumberFormatException exception) {
            return def;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimeParameter(final String name) {
        return getTimeParameter(name, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimeParameter(final String name, final Date def) {
        Date value = def;
        try {
            String str = getStringParameter(name, null);
            String hstr = getStringParameter(name + ".hour", null);
            String mstr = getStringParameter(name + ".min", null);
            if (str != null) {
                DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
                value = timeFormat.parse(str);
            } else if (hstr != null && mstr != null) {
                DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
                value = timeFormat.parse(hstr + ':' + mstr);
            }
        } catch (final ParseException exception) {
            value = def;
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateParameter(final String name) {
        return getDateParameter(name, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateParameter(final String name, final Date def) {
        Date value = def;
        try {
            String str = getStringParameter(name, null);
            String dstr = getStringParameter(name + ".day", null);
            String mstr = getStringParameter(name + ".month", null);
            String ystr = getStringParameter(name + ".year", null);
            if (str != null) {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                value = dateFormat.parse(str);
            } else if (dstr != null && mstr != null && ystr != null) {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                value = dateFormat.parse(dstr + '-' + mstr + '-' + ystr);
            }
        } catch (final ParseException exception) {
            value = def;
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringParameter(final String name) {
        return getStringParameter(name, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringParameter(final String name, final String def) {
        if (parameters != null && parameters.containsKey(name)) {
            List<String> values = parameters.get(name);
            if (!values.isEmpty()) {
                return values.get(0);
            }
        }
        return def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getStringParameters(final String name) {
        List<String> values = Collections.emptyList();
        if (parameters != null && parameters.containsKey(name)) {
            values = Collections.unmodifiableList(parameters.get(name));
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParameter(final String name, final String value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        List<String> values = parameters.get(name);
        if (values == null) {
            values = new ArrayList<>();
            parameters.put(name, values);
        }
        values.add(value.trim());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParameters(final Map<String, String[]> p) {
        for (String name : p.keySet()) {
            String[] values = p.get(name);
            for (String value : values) {
                addParameter(name, value);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCookie(final String name) {
        return cookies != null && cookies.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getCookieNames() {
        return cookies != null ? Collections.unmodifiableSet(cookies.keySet()) : Collections.<String>emptySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cookie getCookie(final String name) {
        return cookies != null ? cookies.get(name) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Cookie> getCookies() {
        return cookies != null ? Collections.unmodifiableMap(cookies) : Collections.<String, Cookie>emptyMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCookies(final Cookie[] c) {
        if (c != null && c.length > 0) {
            cookies = new HashMap<>(c.length);
            for (Cookie cookie : c) {
                cookies.put(cookie.getName(), cookie);
            }
        }
    }
}
