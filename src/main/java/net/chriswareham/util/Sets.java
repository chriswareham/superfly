/*
 * @(#) Sets.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
     * Copy a set.
     *
     * @param <T> the type of the set elements
     * @param set the source set to copy
     * @return null if the source set is null, or a copy of the source set
     */
    public static <T> Set<T> copy(final Set<T> set) {
        return set != null ? new HashSet<>(set) : null;
    }

    /**
     * Make a set unmodifiable.
     *
     * @param <T> the type of the set elements
     * @param set the source set to make unmodifiable
     * @return an empty set if the source set is null, or an unmodifiable
     * version of the source set
     */
    public static <T> Set<T> unmodifiable(final Set<T> set) {
        return set != null ? Collections.unmodifiableSet(set) : Collections.emptySet();
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
