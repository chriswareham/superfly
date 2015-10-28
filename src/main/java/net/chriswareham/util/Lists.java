/*
 * @(#) Lists.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods for lists.
 *
 * @author Chris Wareham
 */
public final class Lists {
    /**
     * Utility class - no public constructor.
     */
    private Lists() {
        // empty
    }

    /**
     * Copy a list.
     *
     * @param <T> the type of the list elements
     * @param list the source list to copy
     * @return null if the source list is null, or a copy of the source list
     */
    public static <T> List<T> copy(final List<T> list) {
        return list != null ? new ArrayList<>(list) : null;
    }

    /**
     * Make a list unmodifiable.
     *
     * @param <T> the type of the list elements
     * @param list the source list to make unmodifiable
     * @return an empty list if the source list is null, or an unmodifiable
     * version of the source list
     */
    public static <T> List<T> unmodifiable(final List<T> list) {
        return list != null ? Collections.unmodifiableList(list) : Collections.emptyList();
    }
}
