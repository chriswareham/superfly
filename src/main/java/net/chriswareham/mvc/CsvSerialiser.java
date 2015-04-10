/*
 * @(#) CsvSerialiser.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.List;

/**
 * This interface provides methods to be implemented by classes that serialise
 * an object to comma separated values.
 *
 * @param <T> the type of objects to serialise
 * @author Chris Wareham
 */
public interface CsvSerialiser<T> {
    /**
     * Get the header for the comma separated values.
     *
     * @return the header for the comma separated values
     */
    List<String> getHeaders();

    /**
     * Serialise an object to comma separated values.
     *
     * @param obj the object to serialise
     * @return comma separated values
     */
    List<String> serialise(T obj);
}
