/*
 * @(#) DefaultTable.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * This class provides a default table.
 *
 * @author Chris Wareham
 */
public class DefaultTable extends JTable {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The foreground colour of rows in the table.
     */
    private static final Color FOREGROUND_COLOR = UIManager.getColor("Table.foreground");
    /**
     * The background colour of rows in the table.
     */
    private static final Color BACKGROUND_COLOR = Color.white;
    /**
     * The background colour of alternate rows in the table.
     */
    private static final Color ALTERNATE_BACKGROUND_COLOR = new Color(0xEBF2FC);
    /**
     * The selection foreground colour of rows in the table.
     */
    private static final Color SELECTION_FOREGROUND_COLOR = UIManager.getColor("Table.selectionForeground");
    /**
     * The selection background colour of rows in the table.
     */
    private static final Color SELECTION_BACKGROUND_COLOR = UIManager.getColor("Table.selectionBackground");
    /**
     * The colour of lines between columns in the table.
     */
    private static final Color VERTICAL_LINE_COLOR = new Color(0xD9D9D9);

    /**
     * The table model.
     */
    private final BaseTableModel tableModel;

    /**
     * Construct an instance of the default table.
     *
     * @param model the table model
     */
    public DefaultTable(final BaseTableModel model) {
        setShowGrid(false);
        setIntercellSpacing(new Dimension());
        setBackground(BACKGROUND_COLOR);
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateColumnsFromModel(false);
        getTableHeader().setReorderingAllowed(false);
        setModel(model);

        tableModel = model;

        for (int col = 0; col < tableModel.getColumnCount(); ++col) {
            addColumn(tableModel.getColumn(col));
        }
    }

    /**
     * Get whether any rows in the table are selected.
     *
     * @return whether any rows in the table are selected
     */
    public boolean isSelectedRows() {
        return getSelectedRowCount() > 0;
    }

    /**
     * Search the table.
     *
     * @param string the string to search for
     * @param cols the table columns to search
     * @param exact whether to search for an exact match
     * @return whether a match was found
     */
    public boolean search(final String string, final List<Integer> cols, final boolean exact) {
        int rc = getRowCount();
        int sr = getSelectedRow();

        for (int row = sr + 1; row < rc; ++row) {
            if (search(row, string, cols, exact)) {
                return true;
            }
        }

        if (sr > 0) {
            for (int row = 0; row < sr; ++row) {
                if (search(row, string, cols, exact)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Paint the table.
     *
     * @param g the graphics context to paint in
     */
    @Override
    public void paint(final Graphics g) {
        super.paint(g);

        // paint empty rows if necessary

        int rowCount = getRowCount();
        Rectangle clip = g.getClipBounds();
        int height = clip.y + clip.height;
        if (rowCount * rowHeight < height) {
            int visibleRowCount = height / rowHeight;
            for (int row = rowCount; row <= visibleRowCount; ++row) {
                g.setColor(isAlternateRow(row) ? ALTERNATE_BACKGROUND_COLOR : BACKGROUND_COLOR);
                g.fillRect(clip.x, row * rowHeight, clip.width, rowHeight);
            }
        }

        // paint vertical lines

        g.setColor(VERTICAL_LINE_COLOR);

        int columnCount = columnModel.getColumnCount();
        int x = 0;
        for (int col = 0; col < columnCount; ++col) {
            TableColumn column = columnModel.getColumn(col);
            x += column.getWidth();
            g.drawLine(x - 1, 0, x - 1, height);
        }
    }

    /**
     * Prepare a table cell renderer.
     *
     * @param renderer the table cell renderer to prepare
     * @param row the row of the cell to render, where 0 is the first row
     * @param col the column of the cell to render, where 0 is the first column
     * @return the component to render
     */
    @Override
    public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int col) {
        Component component = super.prepareRenderer(renderer, row, col);

        boolean selected = isCellSelected(row, col);

        if (selected) {
            component.setBackground(SELECTION_BACKGROUND_COLOR);
            component.setForeground(SELECTION_FOREGROUND_COLOR);
        } else {
            component.setBackground(isAlternateRow(row) ? ALTERNATE_BACKGROUND_COLOR : BACKGROUND_COLOR);
            component.setForeground(FOREGROUND_COLOR);
        }

        if (component instanceof JComponent) {
            JComponent jcomponent = (JComponent) component;

            if (jcomponent instanceof JCheckBox) {
                jcomponent.setOpaque(true);
            }

            if (!getCellSelectionEnabled() && !isEditing()) {
                jcomponent.setBorder(null);
            }
        }

        return component;
    }

    /**
     * Search a table row.
     *
     * @param row the table row to search
     * @param string the string to search for
     * @param cols the table columns to search
     * @param exact whether to search for an exact match
     * @return whether a match was found
     */
    private boolean search(final int row, final String string, final List<Integer> cols, final boolean exact) {
        for (int col : cols) {
            String s = tableModel.getValueAt(row, col).toString();
            if ((exact && s.equals(string)) || (!exact && s.contains(string))) {
                selectRow(row);
                return true;
            }
        }
        return false;
    }

    /**
     * Select a table row, moving the viewport if necessary.
     *
     * @param row the table row to select
     */
    private void selectRow(final int row) {
        selectionModel.addSelectionInterval(row, row);
        Container parent = getParent();
        if (parent instanceof JViewport) {
            JViewport viewport = (JViewport) parent;
            Rectangle rectangle = getCellRect(row, 0, true);
            Point point = viewport.getViewPosition();
            rectangle.setLocation(rectangle.x - point.x, rectangle.y - point.y);
            viewport.scrollRectToVisible(rectangle);
        }
    }

    /**
     * Get whether a row is an alternate one.
     *
     * @param row the index of the row
     * @return whether the row is an alternate one
     */
    private boolean isAlternateRow(final int row) {
        return tableModel.isAlternateRow(row);
    }
}
