/*
 * @(#) SerialiserService.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.io.Serializable;

/**
 * This interface is implemented by classes that serialise and deserialise
 * objects to and from strings.
 *
 * @author Chris Wareham
 */
public interface SerialiserService {
    /**
     * Generate a private key.
     *
     * @return a private key
     * @throws SerialiserServiceException if an error occurs
     */
    String generateKey() throws SerialiserServiceException;

    /**
     * Serialises an object to an encrypted and encoded string.
     *
     * @param obj the object to serialise
     * @return the serialised object as a string
     * @throws SerialiserServiceException if an error occurs
     */
    String serialise(Serializable obj) throws SerialiserServiceException;

    /**
     * Deserialises an object from an encrypted and encoded string.
     *
     * @param <T> the type of the object
     * @param str the serialised object as a string
     * @param type the type of the object
     * @return the deserialised object
     * @throws SerialiserServiceException if an error occurs
     */
    <T> T deserialise(String str, Class<T> type) throws SerialiserServiceException;
}
