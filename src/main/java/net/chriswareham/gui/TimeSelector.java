/*
 * @(#) TimeSelector.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * This class provides a widget for selecting a time.
 *
 * @author Chris Wareham
 */
public class TimeSelector extends JPanel {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The calendar.
     */
    private Calendar calendar;
    /**
     * The hour spinner number model.
     */
    private SpinnerNumberModel hourModel;
    /**
     * The minute spinner number model.
     */
    private SpinnerNumberModel minuteModel;

    /**
     * Construct an instance of the widget for selecting a time.
     */
    public TimeSelector() {
        super(new GridBagLayout());
        createInterface();
    }

    /**
     * Get the time.
     *
     * @return the time
     */
    public Date getTime() {
        calendar.set(Calendar.HOUR_OF_DAY, hourModel.getNumber().intValue());
        calendar.set(Calendar.MINUTE, minuteModel.getNumber().intValue());
        return calendar.getTime();
    }

    /**
     * Set the time.
     *
     * @param time the time
     */
    public void setTime(final Date time) {
        calendar.setTime(time);
        hourModel.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minuteModel.setValue(calendar.get(Calendar.MINUTE));
    }

    /**
     * Create the interface.
     */
    private void createInterface() {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0L);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        hourModel = new SpinnerNumberModel(0, 0, 23, 1);
        add(new JSpinner(hourModel), c);

        c.insets.left = 8;
        c.weightx = 0.0;
        c.gridx++;
        add(new JLabel(":"), c);

        c.weightx = 1.0;
        c.gridx++;
        minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
        add(new JSpinner(minuteModel), c);
    }
}
