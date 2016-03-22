/*
 * @(#) DateField.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.event.FocusEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JFormattedTextField;

/**
 * This class provides a text field that only allows a valid date to be entered.
 *
 * @author Chris Wareham
 */
public class DateField extends JFormattedTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The date format.
     */
    private final DateFormat format;

    /**
     * Construct an instance of the date field.
     *
     * @param format the date format
     */
    public DateField(final DateFormat format) {
        super(format);
        this.format = format;
    }

    /**
     * Construct an instance of the date field.
     *
     * @param format the date format
     * @param l preferred width of the date field in characters
     */
    public DateField(final DateFormat format, final int l) {
        super(format);
        this.format = format;
        setColumns(l);
    }

    /**
     * Get the date currently entered.
     *
     * @return the date currently entered
     */
    public Date getDate() {
        Date date = null;
        String text = getText();
        if (!text.isEmpty()) {
            try {
                date = format.parse(text);
            } catch (ParseException e) {
                // shouldn't happen
            }
        }
        return date;
    }

    /**
     * Set the date currently entered.
     *
     * @param d the date currently entered
     */
    public void setDate(final Date d) {
        setText(format.format(d));
    }

    /**
     * Processes a focus event.
     *
     * @param event the focus event to process
     */
    @Override
    protected void processFocusEvent(final FocusEvent event) {
        if (event.getID() == FocusEvent.FOCUS_LOST && getText().isEmpty()) {
            setValue(null);
        }
        super.processFocusEvent(event);
    }

    /**
     * Checks text for validity.
     *
     * @return true if the text is valid
     */
    @Override
    public boolean isEditValid() {
        if (getText().isEmpty()) {
            setValue(null);
            return true;
        }
        return super.isEditValid();
    }

    /**
     * Gets the current date value.
     *
     * @return the current date value
     */
    public Date getDateValue() {
        return (Date) getValue();
    }
}
