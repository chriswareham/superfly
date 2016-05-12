/*
 * @(#) AuthenticationService.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import net.chriswareham.di.ComponentException;

/**
 * This interface is implemented by classes that provide a user authentication
 * service.
 *
 * @author Chris Wareham
 */
public interface AuthenticationService {
    /**
     * Authenticate a user.
     *
     * @param username the username
     * @param password the password
     * @return the credentials for an authenticated user, null for an unauthenticated user
     * @throws ComponentException if an error occurs
     */
    Credentials authenticate(String username, String password) throws ComponentException;
}
