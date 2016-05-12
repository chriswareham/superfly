/*
 * @(#) Credentials.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

/**
 * This class provides a bean that describes user credentials.
 *
 * @author Chris Wareham
 */
public final class Credentials {
    /**
     * The username.
     */
    private String username;
    /**
     * The password.
     */
    private String password;

    /**
     * Get the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username.
     *
     * @param u the username
     */
    public void setUsername(final String u) {
        username = u;
    }

    /**
     * Get the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     *
     * @param p the password
     */
    public void setPassword(final String p) {
        password = p;
    }
}
