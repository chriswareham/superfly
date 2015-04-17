/*
 * @(#) ComboCellRenderer.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class provides a renderer for table cells containing combo boxes.
 *
 * @author Chris Wareham
 */
public class ComboCellRenderer extends DefaultTableCellRenderer {
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
     * The combo box renderer component.
     */
    private final JComboBox<Object> comboBox;

    /**
     * Construct an instance of the renderer for table cells containing combo
     * boxes.
     */
    public ComboCellRenderer() {
        comboBox = new JComboBox<>();
        comboBox.setOpaque(true);
        comboBox.setBorder(UNFOCUSED_BORDER);
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
        comboBox.removeAllItems();

        if (value != null) {
            comboBox.addItem(value);
        }

        comboBox.setFont(table.getFont());

        if (focused) {
            comboBox.setBorder(FOCUSED_BORDER);
            comboBox.setForeground(table.getForeground());
            comboBox.setBackground(table.getBackground());
        } else if (selected) {
            comboBox.setBorder(UNFOCUSED_BORDER);
            comboBox.setForeground(table.getSelectionForeground());
            comboBox.setBackground(table.getSelectionBackground());
        } else {
            comboBox.setBorder(UNFOCUSED_BORDER);
            comboBox.setForeground(table.getForeground());
            comboBox.setBackground(table.getBackground());
        }

        return comboBox;
    }
}
