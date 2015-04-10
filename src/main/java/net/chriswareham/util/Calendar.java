/*
 * @(#) Calendar.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.GregorianCalendar;

/**
 * This class provides a calendar that implements the bean pattern.
 *
 * @author Chris Wareham
 */
public class Calendar extends GregorianCalendar {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get the day of the month.
     *
     * @return the day of the month
     */
    public int getDayOfMonth() {
        return get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Set the day of the month.
     *
     * @param d the day of the month
     */
    public void setDayOfMonth(final int d) {
        set(GregorianCalendar.DAY_OF_MONTH, d);
    }

    /**
     * Get the month.
     *
     * @return the month
     */
    public int getMonth() {
        return get(GregorianCalendar.MONTH);
    }

    /**
     * Set the month.
     *
     * @param m the month
     */
    public void setMonth(final int m) {
        set(GregorianCalendar.MONTH, m);
    }

    /**
     * Get the year.
     *
     * @return the year
     */
    public int getYear() {
        return get(GregorianCalendar.YEAR);
    }

    /**
     * Set the year.
     *
     * @param y the year
     */
    public void setYear(final int y) {
        set(GregorianCalendar.YEAR, y);
    }

    /**
     * Get the hour (12 hour clock).
     *
     * @return the hour (12 hour clock)
     */
    public int getHour() {
        return get(GregorianCalendar.HOUR);
    }

    /**
     * Set the hour (12 hour clock).
     *
     * @param h the hour (12 hour clock)
     */
    public void setHour(final int h) {
        set(GregorianCalendar.HOUR, h);
    }

    /**
     * Get the hour of day (24 hour clock).
     *
     * @return the hour of day (24 hour clock)
     */
    public int getHourOfDay() {
        return get(GregorianCalendar.HOUR_OF_DAY);
    }

    /**
     * Set the hour of day (24 hour clock).
     *
     * @param h the hour of day (24 hour clock)
     */
    public void setHourOfDay(final int h) {
        set(GregorianCalendar.HOUR_OF_DAY, h);
    }

    /**
     * Get the minute.
     *
     * @return the minute
     */
    public int getMinute() {
        return get(GregorianCalendar.MINUTE);
    }

    /**
     * Set the minute.
     *
     * @param m the minute
     */
    public void setMinute(final int m) {
        set(GregorianCalendar.MINUTE, m);
    }

    /**
     * Get the second.
     *
     * @return the second
     */
    public int getSecond() {
        return get(GregorianCalendar.SECOND);
    }

    /**
     * Set the second.
     *
     * @param s the second
     */
    public void setSecond(final int s) {
        set(GregorianCalendar.SECOND, s);
    }
}
