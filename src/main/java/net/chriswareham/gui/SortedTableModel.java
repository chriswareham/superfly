/*
 * @(#) SortedTableModel.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class provides a table model that maintains the natural ordering of
 * rows.
 *
 * @param <T> type of the objects that make up the rows of the model
 * @author Chris Wareham
 */
public abstract class SortedTableModel<T extends Comparable<? super T>> extends BaseTableModel {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list of rows that make up the model.
     */
    private final List<T> rows = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRowsEmpty() {
        return rows.isEmpty();
    }

    /**
     * Get the number of rows in the model.
     *
     * @return the number of rows
     */
    @Override
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Get whether the model contains a row.
     *
     * @param row the row
     * @return whether the model contains the row
     */
    public boolean isRow(final T row) {
        return rows.contains(row);
    }

    /**
     * Get the rows from the model.
     *
     * @return the rows
     */
    public List<T> getRows() {
        return Collections.unmodifiableList(rows);
    }

    /**
     * Get a row from the model.
     *
     * @param ri the row index
     * @return the row
     */
    public T getRow(final int ri) {
        return rows.get(ri);
    }

    /**
     * Get the index of a row in the model.
     *
     * @param r the row
     * @return the index, or -1 if the model does not contain the row
     */
    public int getRowIndex(final T r) {
        return rows.indexOf(r);
    }

    /**
     * Add rows to the model.
     *
     * @param r the rows to add
     */
    public void addRows(final Collection<T> r) {
        for (T row : r) {
            addRow(row);
        }
    }

    /**
     * Add a row to the model.
     *
     * @param r the row to add
     */
    public void addRow(final T r) {
        int ri = Collections.binarySearch(rows, r);
        if (ri < 0) {
            ri = ~ri;
        }
        rows.add(ri, r);
        fireTableRowsInserted(ri, ri);
    }

    /**
     * Remove a particular row from the model.
     *
     * @param ri the row index
     */
    public void removeRow(final int ri) {
        rows.remove(ri);
        fireTableRowsDeleted(ri, ri);
    }

    /**
     * Remove a particular row from the model.
     *
     * @param r the row to remove
     */
    public void removeRow(final T r) {
        int ri = rows.indexOf(r);
        rows.remove(ri);
        fireTableRowsDeleted(ri, ri);
    }

    /**
     * Removes all rows from the model.
     */
    public void removeRows() {
        if (!rows.isEmpty()) {
            int ri = rows.size();
            rows.clear();
            fireTableRowsDeleted(0, ri - 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fireTableDataChanged() {
        Collections.sort(rows);
        super.fireTableDataChanged();
    }
}
