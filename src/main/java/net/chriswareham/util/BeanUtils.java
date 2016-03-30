/*
 * @(#) BeanUtils.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility methods for bean setters and getters.
 *
 * @author Chris Wareham
 */
public final class BeanUtils {
    /**
     * Utility class - no public constructor.
     */
    private BeanUtils() {
        // empty
    }

    public static <T> List<T> get(final List<? extends T> member) {
        return member != null ? Collections.unmodifiableList(member) : Collections.<T>emptyList();
    }

    public static <T> void set(final List<T> member, final List<T> parameter) {
        member.clear();
        if (parameter != null && !parameter.isEmpty()) {
            member.addAll(parameter);
        }
    }

    public static <T> Set<T> get(final Set<? extends T> member) {
        return member != null ? Collections.unmodifiableSet(member) : Collections.<T>emptySet();
    }

    public static <T> void set(final Set<T> member, final Set<T> parameter) {
        member.clear();
        if (parameter != null && !parameter.isEmpty()) {
            member.addAll(parameter);
        }
    }

    public static <K, V> Map<K, V> get(final Map<? extends K, ? extends V> member) {
        return member != null ? Collections.unmodifiableMap(member) : Collections.<K, V>emptyMap();
    }

    public static <K, V> void set(final Map<K, V> member, final Map<K, V> parameter) {
        member.clear();
        if (parameter != null && !parameter.isEmpty()) {
            member.putAll(parameter);
        }
    }

    public static Date get(final Date member) {
        return member != null ? new Date(member.getTime()) : null;
    }

    public static Date set(final Date member, final Date parameter) {
        if (parameter == null) {
            return null;
        }
        if (member == null) {
            return new Date(parameter.getTime());
        }
        member.setTime(parameter.getTime());
        return member;
    }
}
