/*
 * @(#) DatePickerListener.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.util.Date;
import java.util.EventListener;

/**
 * This interface is implemented by classes that wish to be notified when a
 * date has been picked.
 *
 * @author Chris Wareham
 */
public interface DatePickerListener extends EventListener {
    /**
     * Notify the listener that a date has been picked.
     *
     * @param d the date that has been picked
     */
    void datePicked(Date d);
}
