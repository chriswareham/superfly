/*
 * @(#) BooleanCellRenderer.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class provides a renderer for table cells containing booleans.
 *
 * @author Chris Wareham
 */
public class BooleanCellRenderer extends DefaultTableCellRenderer {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The border when the cell is focused.
     */
    private static final Border FOCUSED_BORDER;
    /**
     * The border when the cell is unfocused.
     */
    private static final Border UNFOCUSED_BORDER;

    static {
        FOCUSED_BORDER = UIManager.getBorder("Table.focusCellHighlightBorder");
        UNFOCUSED_BORDER = new EmptyBorder(1, 1, 1, 1);
    }

    /**
     * The boolean renderer component.
     */
    private final JCheckBox checkBox;

    /**
     * Construct an instance of the renderer for table cells containing booleans.
     */
    public BooleanCellRenderer() {
        checkBox = new JCheckBox();
        checkBox.setOpaque(true);
        checkBox.setBorderPainted(true);
        checkBox.setBorder(UNFOCUSED_BORDER);
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
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
        if (value instanceof Boolean) {
            checkBox.setSelected((Boolean) value);
        }

        if (focused) {
            checkBox.setBorder(FOCUSED_BORDER);
            checkBox.setForeground(table.getForeground());
            checkBox.setBackground(table.getBackground());
        } else if (selected) {
            checkBox.setBorder(UNFOCUSED_BORDER);
            checkBox.setForeground(table.getSelectionForeground());
            checkBox.setBackground(table.getSelectionBackground());
        } else {
            checkBox.setBorder(UNFOCUSED_BORDER);
            checkBox.setForeground(table.getForeground());
            checkBox.setBackground(table.getBackground());
        }

        return checkBox;
    }
}
