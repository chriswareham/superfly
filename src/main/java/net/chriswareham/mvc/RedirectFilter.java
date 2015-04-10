/*
 * @(#) RedirectFilter.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import org.apache.log4j.Logger;

/**
 * This class provides a filter for performing domain and page redirects.
 *
 * @author Chris Wareham
 */
public class RedirectFilter implements Filter {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RedirectFilter.class);

    /**
     * The map of domain names to redirect, and the domains they redirect to.
     */
    private Map<String, DomainRedirect> domainRedirects;
    /**
     * The map of page names to redirect, and the pages they redirect to.
     */
    private Map<PageRedirect, String> pageRedirects;

    /**
     * Reads the redirects parameter from the filter configuration. The
     * redirects parameter is the name of the file containing mappings of domain
     * and page redirects. The file is parsed and the mappings stored.
     *
     * @param config the filter configuration
     * @throws ServletException if an error occurs
     */
    @Override
    public void init(final FilterConfig config) throws ServletException {
        ServletContext context = config.getServletContext();

        String redirects = config.getInitParameter("redirects");

        if (redirects == null) {
            throw new ServletException("A 'redirects' parameter is required");
        }

        try {
            InputSource source = new InputSource(context.getResourceAsStream(redirects));
            source.setSystemId(context.getRealPath(redirects));

            RedirectHandler handler = new RedirectHandler();

            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(source);

            domainRedirects = handler.domainRedirects;
            pageRedirects = handler.pageRedirects;
        } catch (SAXException | IOException exception) {
            throw new ServletException(exception);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("init(): domain redirects:[" + domainRedirects.size() + "] page redirects:[" + pageRedirects.size() + "]");
        }
    }

    /**
     * Clears the redirect mappings.
     */
    @Override
    public void destroy() {
        domainRedirects = null;
        pageRedirects = null;
    }

    /**
     * Process a request.
     *
     * The server name that the client made the request to is checked against
     * the list of domain redirects. If there is a match, then a 301 "Moved
     * Permanently" response is sent.
     *
     * Requests that do no match a domain redirect have their path checked
     * against the list of page redirects. If there is a match, then a 301
     * "Moved Permanently" response is sent.
     *
     * Requests that do not match either a domain or page redirect result in a
     * call to the next filter in the invocation chain.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the invocation chain
     * @throws IOException if an input or output error occurs
     * @throws ServletException if an error occurs processing the request
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String serverName = req.getServerName();

        String path = req.getServletPath();
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            path += pathInfo;
        }

        DomainRedirect domainRedirect = domainRedirects.get(serverName);
        if (domainRedirect != null) {
            StringBuilder buf = new StringBuilder(domainRedirect.domain);

            if (!domainRedirect.ignorePath) {
                // add on the path
                buf.append(path);

                // add on any query string
                String queryString = req.getQueryString();
                if (queryString != null) {
                    buf.append('?');
                    buf.append(queryString);
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("doFilter(): redirecting from:[" + serverName + path + "] to:[" + buf + "]");
            }

            res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            res.addHeader("Location", buf.toString());
        }

        String country = request.getLocale().getCountry();

        for (PageRedirect pageRedirect : pageRedirects.keySet()) {
            // see if this is a country specific redirect

            if (pageRedirect.country != null && !pageRedirect.country.equals(country)) {
                continue;
            }

            // see if the redirect pattern matches the request

            Matcher matcher = pageRedirect.pattern.matcher(path);
            if (matcher.matches()) {
                StringBuilder buf = new StringBuilder(pageRedirects.get(pageRedirect));

                // add on any captured text from the matching
                int groupCount = matcher.groupCount() + 1;
                for (int i = 1; i < groupCount; ++i) {
                    buf.append(matcher.group(i));
                }

                // add on any query string
                String queryString = req.getQueryString();
                if (queryString != null) {
                    if (buf.indexOf("?") != -1) {
                        buf.append('&');
                    } else {
                        buf.append('?');
                    }
                    buf.append(queryString);
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("doFilter(): redirecting from:[" + path + "] to:[" + buf + "]");
                }

                res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                res.addHeader("Location", buf.toString());
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * This class provides a handler to populate domain and page redirects.
     */
    private static class RedirectHandler extends DefaultHandler {
        /**
         * The map of domain names to redirect, and the domains they redirect to.
         */
        private Map<String, DomainRedirect> domainRedirects = new HashMap<>();
        /**
         * The map of page names to redirect, and the pages they redirect to.
         */
        private Map<PageRedirect, String> pageRedirects = new LinkedHashMap<>();

        /**
         * {@inheritDoc}
         */
        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
            if ("redirect".equals(localName)) {
                String to = attributes.getValue("to");
                if (to != null) {
                    String domainName = attributes.getValue("domainname");
                    String ignorePath = attributes.getValue("ignorepath");
                    String from = attributes.getValue("from");
                    String country = attributes.getValue("country");
                    if (domainName != null) {
                        domainRedirects.put(domainName.trim(), new DomainRedirect(to.trim(), Boolean.parseBoolean(ignorePath)));
                    } else if (from != null) {
                        pageRedirects.put(new PageRedirect(Pattern.compile(from.trim()), country != null ? country.trim() : null), to.trim());
                    }
                }
            }
        }
    }

    /**
     * This class provides a bean to store a domain redirect.
     */
    private static class DomainRedirect {
        /**
         * The domain to redirect to.
         */
        private String domain;
        /**
         * Whether to ignore the path in the original request.
         */
        private boolean ignorePath;

        /**
         * Constructs a new instance of the domain redirect class.
         *
         * @param d the domain to redirect to
         * @param ip whether to ignore the path in the original request
         */
        DomainRedirect(final String d, final boolean ip) {
            domain = d;
            ignorePath = ip;
        }
    }

    /**
     * This class provides a bean to store a page redirect.
     */
    private static class PageRedirect {
        /**
         * The pattern to match the path to.
         */
        private Pattern pattern;
        /**
         * The country the request must match.
         */
        private String country;

        /**
         * Constructs a new instance of the page redirect class.
         *
         * @param p the pattern to match the path to
         * @param c the country the request must match
         */
        PageRedirect(final Pattern p, final String c) {
            pattern = p;
            country = c;
        }
    }
}
