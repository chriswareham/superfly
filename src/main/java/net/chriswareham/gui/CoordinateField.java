/*
 * @(#) CoordinateField.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class provides a widget for selecting coordinates.
 *
 * @author Chris Wareham
 */
public class CoordinateField extends JPanel {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The x coordinate field.
     */
    private DoubleField xField;
    /**
     * The y coordinate field.
     */
    private DoubleField yField;

    /**
     * Construct an instance of the widget for selecting coordinates.
     */
    public CoordinateField() {
        super(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        xField = new DoubleField(8);
        add(xField, c);

        c.insets.left = 8;
        c.weightx = 0.0;
        c.gridx++;
        add(new JLabel("x"), c);

        c.weightx = 1.0;
        c.gridx++;
        yField = new DoubleField(8);
        add(yField, c);
    }

    /**
     * Get the x coordinate.
     *
     * @return the x coordinate
     */
    public double getXCoordinate() {
        return xField.getDouble();
    }

    /**
     * Set the x coordinate.
     *
     * @param x the x coordinate
     */
    public void setXCoordinate(final double x) {
        xField.setDouble(x);
    }

    /**
     * Get the y coordinate.
     *
     * @return the y coordinate
     */
    public double getYCoordinate() {
        return yField.getDouble();
    }

    /**
     * Set the y coordinate.
     *
     * @param y the y coordinate
     */
    public void setYCoordinate(final double y) {
        yField.setDouble(y);
    }
}
