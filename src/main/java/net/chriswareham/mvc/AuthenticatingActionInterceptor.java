/*
 * @(#) AuthenticatingActionInterceptor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import net.chriswareham.di.ComponentException;

/**
 * This class provides a simple interceptor that authenticates requests.
 *
 * @author Chris Wareham
 */
public class AuthenticatingActionInterceptor implements ActionInterceptor {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AuthenticatingActionInterceptor.class);
    /**
     * The pattern that splits cookie values.
     */
    private static final Pattern SPLIT_PATTERN = Pattern.compile("-");

    /**
     * The authentication service.
     */
    private AuthenticationService authenticationService;
    /**
     * The cookie name.
     */
    private String cookieName;
    /**
     * The cookie domain.
     */
    private String cookieDomain;
    /**
     * The cookie path.
     */
    private String cookiePath;
    /**
     * The cookie maximum age in seconds.
     */
    private int cookieMaxAge;

    /**
     * Set the authentication service.
     *
     * @param as the authentication service
     */
    public void setAuthenticationService(final AuthenticationService as) {
        authenticationService = as;
    }

    /**
     * Set the cookie name.
     *
     * @param cn the cookie name
     */
    public void setCookieName(final String cn) {
        cookieName = cn;
    }

    /**
     * Set the cookie domain.
     *
     * @param cd the cookie domain
     */
    public void setCookieDomain(final String cd) {
        cookieDomain = cd;
    }

    /**
     * Set the cookie path.
     *
     * @param cp the cookie path
     */
    public void setCookiePath(final String cp) {
        cookiePath = cp;
    }

    /**
     * Set the cookie maximum age in seconds.
     *
     * @param cma the cookie maximum age in seconds
     */
    public void setCookieMaxAge(final int cma) {
        cookieMaxAge = cma;
    }

    /**
     * Authenticate a request.
     *
     * @param request holds data for the request
     * @param chain the chain of interceptors
     * @return the view name and models for the response
     * @throws ActionException if an error occurs
     */
    @Override
    public ActionResponse intercept(final ActionRequest request, final ActionInterceptorChain chain) throws ActionException {
        LOGGER.debug("Authenticating interceptor called");

        Credentials credentials = getCredentials(request);

        if (credentials != null) {
            request.addAttribute("credentials", credentials);
        }

        ActionResponse response = chain.intercept(request);

        if (request.isAttribute("credentials", Credentials.class)) {
            //
            // The request was either from a logged in user, or the user has
            // been logged in as part of the processing of the request. The
            // cookie is set even if the user was already logged in, in case the
            // processing of the request modified the password (which is used to
            // generate an authentication hash).
            //
            credentials = request.getAttribute("credentials", Credentials.class);
            response.addModel("credentials", credentials);
            addCookie(credentials, response);
        } else if (credentials != null) {
            //
            // The request was from a logged in user who has been logged out as
            // part of the processing of the request.
            //
            removeCookie(response);
        }

        LOGGER.debug("Authenticating interceptor returning");

        return response;
    }

    /**
     * Get user credentials determined from a cookie value of <code><i>username</i>-<i>password_hash</i></code>.
     *
     * @param request the request
     * @return the user credentials, or null if the credentials cannot be determined
     * @throws ActionException if an error occurs
     */
    private Credentials getCredentials(final ActionRequest request) throws ActionException {
        Cookie cookie = request.getCookie(cookieName);

        if (cookie == null) {
            return null;
        }

        String[] s = SPLIT_PATTERN.split(cookie.getValue());

        if (s.length != 2) {
            return null;
        }

        Credentials credentials = null;

        try {
            credentials = authenticationService.authenticate(s[0], s[1]);
        } catch (ComponentException exception) {
            throw new ActionException(exception);
        }

        return credentials;
    }

    /**
     * Set a cookie with a value of <code><i>username</i>-<i>password_hash</i></code>.
     *
     * @param credentials the user credentials
     * @param response the response
     */
    private void addCookie(final Credentials credentials, final ActionResponse response) {
        StringBuilder buf = new StringBuilder();
        buf.append(credentials.getUsername());
        buf.append('-');
        buf.append(credentials.getPassword());

        Cookie cookie = new Cookie(cookieName, buf.toString());
        cookie.setDomain(cookieDomain);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(cookieMaxAge);
        response.addCookie(cookie);
    }

    /**
     * Set a cookie without a value, removing any existing cookie.
     *
     * @param response the response
     */
    private void removeCookie(final ActionResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setDomain(cookieDomain);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
