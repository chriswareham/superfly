/*
 * @(#) BaseTableModel.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * This class provides an abstract base for table models.
 *
 * @author Chris Wareham
 */
public abstract class BaseTableModel extends AbstractTableModel {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list of columns that make up the model.
     */
    private final List<DefaultTableColumn> columns = new ArrayList<>();

    /**
     * Get the number of columns in the model.
     *
     * @return the number of columns
     */
    @Override
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Get the columns in the model.
     *
     * @return the columns
     */
    public List<DefaultTableColumn> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    /**
     * Get a particular column in the model.
     *
     * @param ci the column index
     * @return the column
     */
    public DefaultTableColumn getColumn(final int ci) {
        return columns.get(ci);
    }

    /**
     * Get the name of a particular column in the model.
     *
     * @param ci the column index
     * @return the name of the column
     */
    @Override
    public String getColumnName(final int ci) {
        return columns.get(ci).getName();
    }

    /**
     * Get whether a column can be searched.
     *
     * @param ci the column index
     * @return true if the column can be searched, false otherwise
     */
    public boolean isColumnSearchable(final int ci) {
        return columns.get(ci).isSearchable();
    }

    /**
     * Set whether a column can be searched.
     *
     * @param ci the column index
     * @param searchable whether the column can be searched
     */
    public void setColumnSearchable(final int ci, final boolean searchable) {
        columns.get(ci).setSearchable(searchable);
    }

    /**
     * Get whether a column can be edited.
     *
     * @param ci the column index
     * @return whether the column can be edited
     */
    public boolean isColumnEditable(final int ci) {
        return columns.get(ci).isEditable();
    }

    /**
     * Set whether a column can be edited.
     *
     * @param ci the column index
     * @param editable whether the column can be edited
     */
    public void setColumnEditable(final int ci, final boolean editable) {
        columns.get(ci).setEditable(editable);
    }

    /**
     * Add a column to the model.
     *
     * @param column the column to add
     */
    public void addColumn(final DefaultTableColumn column) {
        column.setModelIndex(columns.size());
        columns.add(column);
    }

    /**
     * Get whether the model has no rows.
     *
     * @return whether the model has no rows
     */
    public abstract boolean isRowsEmpty();

    /**
     * Get whether a row is an alternate one.
     *
     * @param ri the row index
     * @return whether the row is an alternate one
     */
    public boolean isAlternateRow(final int ri) {
        return ri % 2 == 1;
    }

    /**
     * Get whether a cell can be edited.
     *
     * @param ri the row index of the cell
     * @param ci the column index of the cell
     * @return whether the cell can be edited
     */
    @Override
    public boolean isCellEditable(final int ri, final int ci) {
        return columns.get(ci).isEditable();
    }
}
