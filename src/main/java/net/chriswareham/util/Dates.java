/*
 * @(#) Dates.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Utility methods for dates.
 *
 * @author Chris Wareham
 */
public final class Dates {
    /**
     * An immutable date representing the epoch.
     */
    public static final Date EPOCH = new ImmutableDate(0);
    /**
     * The time zone of the host.
     */
    public static final TimeZone TIME_ZONE = TimeZone.getDefault();

    /**
     * Utility class - no public constructor.
     */
    private Dates() {
        // empty
    }

    /**
     * Get whether a date is in daylight time for the time zone of the host.
     *
     * @param date the date
     * @return whether a date is in daylight time for the time zone of the host
     */
    public static boolean isDaylightTime(final Date date) {
        return TIME_ZONE.inDaylightTime(date);
    }

    /**
     * Get a date representing the start of a day.
     *
     * @param date the day to get the start of
     * @return the date representing the start of the day
     */
    public static Date startDay(final Date date) {
        return startDay(Calendar.getInstance(), date);
    }

    /**
     * Get a date representing the end of a day.
     *
     * @param date the day to get the end of
     * @return the date representing the end of the day
     */
    public static Date endDay(final Date date) {
        return endDay(Calendar.getInstance(), date);
    }

    /**
     * Get a date representing the start of the current day.
     *
     * @return the date representing the start of the current day
     */
    public static Date startToday() {
        return startDay(Calendar.getInstance(), new Date());
    }

    /**
     * Get a date representing the end of the current day.
     *
     * @return the date representing the end of the current day
     */
    public static Date endToday() {
        return endDay(Calendar.getInstance(), new Date());
    }

    /**
     * Get whether two dates are the same day.
     *
     * @param d1 the first date
     * @param d2 the second date
     * @return whether the two dates are the same day
     */
    public static boolean equalsDay(final Date d1, final Date d2) {
        Calendar calendar = Calendar.getInstance();
        return startDay(calendar, d1).getTime() == startDay(calendar, d2).getTime();
    }

    /**
     * Get whether a date is the same as the current day.
     *
     * @param date the date
     * @return whether the date is the same as the current day
     */
    public static boolean isToday(final Date date) {
        Calendar calendar = Calendar.getInstance();
        return startDay(calendar, date).getTime() == startDay(calendar, new Date()).getTime();
    }

    /**
     * Get whether a date is before the current day.
     *
     * @param date the date
     * @return whether the date is before the current day
     */
    public static boolean beforeToday(final Date date) {
        return date.before(startToday());
    }

    /**
     * Get whether a date is after the current day.
     *
     * @param date the date
     * @return whether the date is after the current day
     */
    public static boolean afterToday(final Date date) {
        return date.after(endToday());
    }

    /**
     * Get whether a date is between two dates. The date must be equal to or
     * after the start date, and equal to or before the end date.
     *
     * @param date the date
     * @param startDate the start date
     * @param endDate the end date
     * @return whether the date is between the start and end dates
     */
    public static boolean isBetween(final Date date, final Date startDate, final Date endDate) {
        return !date.before(startDate) && !date.after(endDate);
    }

    /**
     * Get the number of days between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of days
     */
    public static int daysBetween(final Date startDate, final Date endDate) {
        int delta = 0;
        Calendar calendar = Calendar.getInstance();
        Date startDay = startDay(calendar, startDate);
        Date endDay = startDay(calendar, endDate);
        for (calendar.setTime(startDay); calendar.getTime().before(endDay); calendar.add(Calendar.DATE, 1)) {
            ++delta;
        }
        return delta;
    }

    /**
     * Get the dates between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the dates between two dates
     */
    public static List<Date> datesBetween(final Date startDate, final Date endDate) {
        Calendar calendar = Calendar.getInstance();
        List<Date> dates = new ArrayList<>();
        Date startDay = startDay(calendar, startDate);
        Date endDay = endDay(calendar, endDate);
        for (calendar.setTime(startDay); calendar.getTime().before(endDay); calendar.add(Calendar.DATE, 1)) {
            dates.add(calendar.getTime());
        }
        return dates;
    }

    /**
     * Get a date with a number of days added the current date.
     *
     * @param days the number of days added the current date
     * @return the date with the number of days added
     */
    public static Date addDays(final int days) {
        return addDays(new Date(), days);
    }

    /**
     * Get a date with a number of days added.
     *
     * @param date the date to add a number of days to
     * @param days the number of days to add
     * @return the date with the number of days added
     */
    public static Date addDays(final Date date, final int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * Get a date representing the start of a day.
     *
     * @param calendar the calendar to use
     * @param date the day to get the start of
     * @return the date representing the start of the day
     */
    private static Date startDay(final Calendar calendar, final Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get a date representing the end of a day.
     *
     * @param calendar the calendar to use
     * @param date the day to get the end of
     * @return the date representing the end of the day
     */
    private static Date endDay(final Calendar calendar, final Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
