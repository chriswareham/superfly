/*
 * @(#) DatePicker.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class provides a date picker dialog.
 *
 * TODO: Internationalise the strings and day of week ordering (the first day of
 * the week can be Sunday or Monday depending on locale).
 *
 * @author Chris Wareham
 */
public class DatePicker extends JDialog {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The month names.
     */
    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };
    /**
     * The days in each month (leap years are handled in the update method).
     */
    private static final int[] DAYS_OF_MONTH = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };
    /**
     * The days of the week.
     */
    private static final String[] DAYS_OF_WEEK = {
        "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"
    };
    /**
     * The select button label.
     */
    private static final String SELECT = "Select";
    /**
     * The cancel button label.
     */
    private static final String CANCEL = "Cancel";

    /**
     * The event listeners.
     */
    private List<DatePickerListener> listeners;
    /**
     * The calendar used for calculations.
     */
    private Calendar calendar;
    /**
     * The currently selected day of month.
     */
    private int dd;
    /**
     * The currently selected month.
     */
    private int mm;
    /**
     * The currently selected year.
     */
    private int yy;
    /**
     * The number of buttons to blank out at the start of this month.
     */
    private int blanks;
    /**
     * The buttons for the days of the month.
     */
    private JButton[][] buttons;
    /**
     * The month combo box.
     */
    private JComboBox<String> monthComboBox;
    /**
     * The year combo box.
     */
    private JComboBox<Integer> yearComboBox;

    /**
     * Construct an instance of the date picker dialog.
     */
    public DatePicker() {
        this(null, null);
    }

    /**
     * Construct an instance of the date picker dialog.
     *
     * @param date the initial date
     */
    public DatePicker(final Date date) {
        this(null, date);
    }

    /**
     * Construct an instance of the date picker dialog.
     *
     * @param window the parent window
     */
    public DatePicker(final Window window) {
        this(window, null);
    }

    /**
     * Construct an instance of the date picker dialog.
     *
     * @param window the parent window
     * @param date the initial date
     */
    public DatePicker(final Window window, final Date date) {
        super(window, Dialog.ModalityType.APPLICATION_MODAL);
        calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        createInterface();
        update();
        pack();
    }

    /**
     * Add a listener.
     *
     * @param listener the listener to add
     */
    public void addDatePickerListener(final DatePickerListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener.
     *
     * @param listener the listener to remove
     */
    public void removeDatePickerListener(final DatePickerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fire an event notifying listeners a date has been picked.
     *
     * @param date the date picked
     */
    private void fireDatePicked(final Date date) {
        for (DatePickerListener listener : listeners) {
            listener.datePicked(date);
        }
    }

    /**
     * Creates the interface.
     */
    private void createInterface() {
        listeners = new CopyOnWriteArrayList<>();

        // combo box panel

        JPanel panel = new JPanel(new GridLayout(1, 2));
        getContentPane().add(panel, BorderLayout.NORTH);

        // month combo box

        monthComboBox = new JComboBox<>(MONTHS);
        monthComboBox.setSelectedIndex(mm);
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                int i = monthComboBox.getSelectedIndex();
                if (i > -1) {
                    mm = i;
                    update();
                }
            }
        });
        panel.add(monthComboBox);

        // year combo box

        yearComboBox = new JComboBox<>();
        for (int i = -5; i < 5; ++i) {
            yearComboBox.addItem(yy + i);
        }
        yearComboBox.setSelectedItem(yy);
        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                int i = yearComboBox.getSelectedIndex();
                if (i > -1) {
                    yy = yearComboBox.getItemAt(i);
                    update();
                }
            }
        });
        panel.add(yearComboBox);

        // calandar button panel

        panel = new JPanel(new GridLayout(7, 7));
        getContentPane().add(BorderLayout.CENTER, panel);

        // add days of week buttons to first row of panel

        for (String dayOfWeek : DAYS_OF_WEEK) {
            panel.add(new JLabel(dayOfWeek));
        }

        // construct the days of month buttons with a listener

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                String n = event.getActionCommand();
                if (!n.isEmpty()) {
                    clearActiveDay();
                    dd = Integer.parseInt(n);
                    setActiveDay();
                }
            }
        };

        buttons = new JButton[6][7];

        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 7; ++j) {
                buttons[i][j] = new JButton("");
                buttons[i][j].addActionListener(buttonListener);
                panel.add(buttons[i][j]);
            }
        }

        // action button panel

        panel = new JPanel(new GridLayout(1, 2));
        getContentPane().add(panel, BorderLayout.SOUTH);

        JButton button = new JButton(SELECT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                fireDatePicked(calendar.getTime());
                setVisible(false);
                dispose();
            }
        });
        panel.add(button);

        button = new JButton(CANCEL);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                setVisible(false);
                dispose();
            }
        });
        panel.add(button);
    }

    /**
     * Update the buttons to reflect the currently selected month.
     */
    private void update() {
        clearActiveDay();

        calendar.set(Calendar.YEAR, yy);
        calendar.set(Calendar.MONTH, mm);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        blanks = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        int dom = DAYS_OF_MONTH[mm];
        if (mm == 1 && (yy % 4 == 0 && yy % 100 != 0 || yy % 400 == 0)) {
            ++dom;
        }

        if (dd > dom) {
            dd = dom;
        }

        for (int i = 0; i < blanks; ++i) {
            buttons[0][i].setText("");
            buttons[0][i].setEnabled(false);
        }

        for (int i = blanks; i < dom + blanks; ++i) {
            JButton button = buttons[i / 7][i % 7];
            button.setText(Integer.toString(i - blanks + 1));
            button.setEnabled(true);
        }

        for (int i = blanks + dom; i < 42; ++i) {
            buttons[i / 7][i % 7].setText("");
            buttons[i / 7][i % 7].setEnabled(false);
        }

        setActiveDay();

        repaint();
    }

    /**
     * Add the highlight to the button for the currently selected day.
     */
    private void setActiveDay() {
        calendar.set(Calendar.DAY_OF_MONTH, dd);

        JButton button = buttons[(blanks + dd - 1) / 7][(blanks + dd - 1) % 7];
        button.setBackground(Color.red);
        button.repaint();
    }

    /**
     * Remove the highlight from the button for the currently selected day.
     */
    private void clearActiveDay() {
        JButton button = buttons[(blanks + dd - 1) / 7][(blanks + dd - 1) % 7];
        button.setBackground(getBackground());
        button.repaint();
    }
}
