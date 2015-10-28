/*
 * @(#) Maps.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for maps.
 *
 * @author Chris Wareham
 */
public final class Maps {
    /**
     * Utility class - no public constructor.
     */
    private Maps() {
        // empty
    }

    /**
     * Copy a map.
     *
     * @param <K> the type of the map keys
     * @param <V> the type of the map values
     * @param map the source map to copy
     * @return null if the source map is null, or a copy of the source map
     */
    public static <K, V> Map<K, V> copy(final Map<K, V> map) {
        return map != null ? new HashMap<>(map) : null;
    }

    /**
     * Make a map unmodifiable.
     *
     * @param <K> the type of the map keys
     * @param <V> the type of the map values
     * @param map the source map to make unmodifiable
     * @return an empty map if the source map is null, or an unmodifiable version of the source map
     */
    public static <K, V> Map<K, V> unmodifiable(final Map<K, V> map) {
        return map != null ? Collections.unmodifiableMap(map) : Collections.emptyMap();
    }
}
