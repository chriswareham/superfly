/*
 * @(#) DateSelector.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class provides date selector widget.
 *
 * @author Chris Wareham
 */
public class DateSelector extends JPanel {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The select date icon.
     */
    private static final ImageIcon SELECT_DATE_ICON;
    /**
     * The clear date icon.
     */
    private static final ImageIcon CLEAR_DATE_ICON;

    static {
        SELECT_DATE_ICON = new ImageIcon(DateSelector.class.getResource("/images/date.png"));
        CLEAR_DATE_ICON = new ImageIcon(DateSelector.class.getResource("/images/clear.png"));
    }

    /**
     * The date format.
     */
    private final DateFormat dateFormat;
    /**
     * The date.
     */
    private Date date;
    /**
     * The text field.
     */
    private JTextField textField;
    /**
     * The select button.
     */
    private JButton selectButton;
    /**
     * The clear button.
     */
    private JButton clearButton;

    /**
     * Construct an instance of the date selector widget.
     *
     * @param df the date format
     */
    public DateSelector(final DateFormat df) {
        super(new GridBagLayout());
        dateFormat = df;
        createInterface();
    }

    /**
     * Get the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date != null ? new Date(date.getTime()) : null;
    }

    /**
     * Set the date.
     *
     * @param d the date
     */
    public void setDate(final Date d) {
        if (d != null) {
            date = date != null ? date : new Date(0L);
            date.setTime(d.getTime());
            textField.setText(dateFormat.format(date));
            clearButton.setEnabled(true);
        } else {
            date = null;
            textField.setText(null);
            clearButton.setEnabled(false);
        }
    }

    /**
     * Create the interface.
     */
    private void createInterface() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        textField = new JTextField(16);
        textField.setEditable(false);
        add(textField, c);

        c.insets.left = 8;
        c.weightx = 0.0;
        c.gridx++;
        selectButton = new JButton(SELECT_DATE_ICON);
        selectButton.addActionListener((final ActionEvent event) -> {
                pickDate();
            });
        add(selectButton, c);

        c.gridx++;
        clearButton = new JButton(CLEAR_DATE_ICON);
        clearButton.addActionListener((final ActionEvent event) -> {
                setDate(null);
            });
        clearButton.setEnabled(false);
        add(clearButton, c);
    }

    /**
     * Pick a date.
     */
    private void pickDate() {
        DatePicker datePicker = new DatePicker(date);
        datePicker.addDatePickerListener((final Date d) -> {
                setDate(d);
            });
        datePicker.setLocationRelativeTo(selectButton);
        datePicker.setVisible(true);
    }
}
