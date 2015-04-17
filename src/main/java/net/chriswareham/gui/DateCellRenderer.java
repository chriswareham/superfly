/*
 * @(#) DateCellRenderer.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Component;
import java.text.DateFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class provides a renderer for table cells containing dates.
 *
 * @author Chris Wareham
 */
public class DateCellRenderer extends DefaultTableCellRenderer {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The date format.
     */
    private final DateFormat dateFormat;

    /**
     * Construct an instance of the renderer for table cells containing dates.
     *
     * @param df the date format.
     */
    public DateCellRenderer(final DateFormat df) {
        setBorder(null);
        dateFormat = df;
    }

    /**
     * Get the renderer component.
     *
     * @param table the table holding this cell
     * @param value the value held by the cell
     * @param selected whether the row holding the cell is selected
     * @param focused whether the cell is focused
     * @param row the table row holding the cell
     * @param column the table column holding the cell
     * @return the renderer component
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean selected, final boolean focused, final int row, final int column) {
        return super.getTableCellRendererComponent(table, value == null ? null : dateFormat.format(value), selected, focused, row, column);
    }
}
