/*
 * @(#) Sets.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Collection;
import java.util.Set;

/**
 * Utility methods for sets.
 *
 * @author Chris Wareham
 */
public final class Sets {
    /**
     * Utility class - no public constructor.
     */
    private Sets() {
        // empty
    }

    /**
     * Return whether a set contains any of the items in a collection.
     *
     * @param <T> the type of items
     * @param set the set of items
     * @param coll the collection of items
     * @return whether the set contains any of the items in the collection
     */
    public static <T> boolean containsAny(final Set<T> set, final Collection<? extends T> coll) {
        for (T item : coll) {
            if (set.contains(item)) {
                return true;
            }
        }
        return false;
    }
}
