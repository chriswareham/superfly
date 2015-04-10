/*
 * @(#) Functions.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc.tags;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.chriswareham.util.Dates;

/**
 * This class provides tag library functions.
 *
 * @author Chris Wareham
 */
public final class Functions {
    /**
     * Utility class - no public constructor.
     */
    private Functions() {
        // empty
    }

    /**
     * The pattern for splitting strings.
     */
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[\r\f\n]+");

    /**
     * Get whether a collection of strings contains a specific string.
     *
     * @param haystack the strings to search
     * @param needle the string to match
     * @return whether the collection contains the string
     */
    public static boolean contains(final Collection<String> haystack, final String needle) {
        return haystack != null && needle != null && haystack.contains(needle);
    }

    /**
     * Get whether a collection of strings contains any members of another
     * collection of strings.
     *
     * @param haystack the strings to search
     * @param needles the strings to match
     * @return whether the collection contains any members of the other collection
     */
    public static boolean containsAny(final Collection<String> haystack, final Collection<String> needles) {
        if (haystack != null && needles != null) {
            for (String needle : needles) {
                if (haystack.contains(needle)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get whether a collection of strings contains all members of another
     * collection of strings.
     *
     * @param haystack the strings to search
     * @param needles the strings to match
     * @return whether the collection contains all members of the other collection
     */
    public static boolean containsAll(final Collection<String> haystack, final Collection<String> needles) {
        return haystack != null && needles != null && haystack.containsAll(needles);
    }

    /**
     * Get whether a collection of strings contains any members of a map key set.
     *
     * @param haystack the strings to search
     * @param needles the map to match the key set of
     * @return whether the collection contains any members of the map key set
     */
    public static boolean containsAnyKey(final Collection<String> haystack, final Map<String, ?> needles) {
        if (haystack != null && needles != null) {
            for (String needle : needles.keySet()) {
                if (haystack.contains(needle)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get whether a collection of strings contains all members of a map key set.
     *
     * @param haystack the strings to search
     * @param needles the map to match the key set of
     * @return whether the collection contains all members of the map key set
     */
    public static boolean containsAllKeys(final Collection<String> haystack, final Map<String, ?> needles) {
        return haystack != null && needles != null && haystack.containsAll(needles.keySet());
    }

    /**
     * Split a string on newlines.
     *
     * @param str the string to split
     * @return the split strings
     */
    public static Collection<String> split(final String str) {
        return str != null ? Arrays.asList(SPLIT_PATTERN.split(str)) : Collections.<String>emptyList();
    }

    /**
     * Join a collection of strings.
     *
     * @param strs the strings to join
     * @param sep the separator string
     * @return the joined strings
     */
    public static String join(final Collection<String> strs, final String sep) {
        if (strs == null || strs.isEmpty() || sep == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (String str : strs) {
            if (buf.length() > 0) {
                buf.append(sep);
            }
            buf.append(str);
        }
        return buf.toString();
    }

    /**
     * Calculate the distance in metres between two points, expressed as X and Y
     * Ordnance Survey coordinates.
     *
     * @param fromX the X coordinate of the first point
     * @param fromY the Y coordinate of the first point
     * @param toX the X coordinate of the second point
     * @param toY the Y coordinate of the second point
     * @return the distance in metres between the two points
     */
    public static double distance(final int fromX, final int fromY, final int toX, final int toY) {
        int dx = Math.abs(fromX - toX);
        int dy = Math.abs(fromY - toY);
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    /**
     * Get whether two dates are the same day.
     *
     * @param d1 the first date
     * @param d2 the second date
     * @return whether the two dates are the same day
     */
    public static boolean equalsDay(final Date d1, final Date d2) {
        return d1 != null && d2 != null && Dates.equalsDay(d1, d2);
    }

    /**
     * Get a date representing the start of the current day.
     *
     * @return the date representing the start of the current day
     */
    public static Date startToday() {
        return Dates.startToday();
    }

    /**
     * Get a date representing the start of the current day offset by a number of days.
     *
     * @param days the number of days to offset the current day by
     * @return the date representing the current day offset by the number of days
     */
    public static Date startTodayOffset(final int days) {
        return daysOffset(Dates.startToday(), days);
    }

    /**
     * Get whether a date is the current day.
     *
     * @param date the date
     * @return whether the date is the current day
     */
    public static boolean isToday(final Date date) {
        return date != null && Dates.isToday(date);
    }

    /**
     * Get whether a date is before the current day.
     *
     * @param date the date
     * @return whether the date before the current day
     */
    public static boolean beforeToday(final Date date) {
        return date != null && Dates.beforeToday(date);
    }

    /**
     * Get whether a date is after the current day.
     *
     * @param date the date
     * @return whether the date after the current day
     */
    public static boolean afterToday(final Date date) {
        return date != null && Dates.afterToday(date);
    }

    /**
     * Get the number of days between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of days
     */
    public static long daysBetween(final Date startDate, final Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return 0;
        }
        return Dates.daysBetween(startDate, endDate);
    }

    /**
     * Get the dates between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the dates between two dates
     */
    public static List<Date> datesBetween(final Date startDate, final Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return Collections.emptyList();
        }
        return Dates.datesBetween(startDate, endDate);
    }

    /**
     * Get a date offset by a number of days.
     *
     * @param date the date to offset from
     * @param days the number of days to offset the date by
     * @return the date representing the date offset by the number of days
     */
    public static Date daysOffset(final Date date, final int days) {
        return date != null ? Dates.addDays(days) : null;
    }
}
