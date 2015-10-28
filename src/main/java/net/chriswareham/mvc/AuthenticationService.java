/*
 * @(#) AuthenticationService.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import net.chriswareham.di.ComponentException;

public interface AuthenticationService {
    Credentials authenticate(String username, String password) throws ComponentException;
}
