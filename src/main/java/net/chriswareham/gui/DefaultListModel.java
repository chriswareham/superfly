/*
 * @(#) DefaultListModel.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * This class provides a default list model.
 *
 * @author Chris Wareham
 * @param <T> type of the objects that make up the rows of the model
 */
public class DefaultListModel<T extends Comparable<? super T>> extends AbstractListModel<T> {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list of rows that make up the model.
     */
    private final List<T> rows = new ArrayList<>();

    /**
     * Get the number of rows in the model.
     *
     * @return the number of rows
     */
    @Override
    public int getSize() {
        return rows.size();
    }

    /**
     * Get a row from the model.
     *
     * @param ri the row index
     * @return the object
     */
    @Override
    public T getElementAt(final int ri) {
        return rows.get(ri);
    }

    /**
     * Get whether a row is in the model.
     *
     * @param row the row
     * @return whether the row is in the model
     */
    public boolean isRow(final T row) {
        return rows.contains(row);
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
     * Get all the rows from the model.
     *
     * @return all the rows from the model
     */
    public List<T> getRows() {
        return Collections.unmodifiableList(rows);
    }

    /**
     * Add a row to the model.
     *
     * @param ri the row index
     * @param o the row to add
     */
    public void addRow(final int ri, final T o) {
        rows.add(ri, o);
        fireIntervalAdded(this, ri, ri);
    }

    /**
     * Add a row to the model.
     *
     * @param o the row to add
     */
    public void addRow(final T o) {
        int ri = rows.size();
        rows.add(o);
        fireIntervalAdded(this, ri, ri);
    }

    /**
     * Add rows to the model.
     *
     * @param o the rows to add
     */
    public void addRows(final List<T> o) {
        int ri = rows.size();
        int sz = o.size();
        if (sz > 0) {
            rows.addAll(o);
            fireIntervalAdded(this, ri, ri + sz);
        }
    }

    /**
     * Update a row in the model.
     *
     * @param ri the row index
     */
    public void updateRow(final int ri) {
        fireContentsChanged(this, ri, ri);
    }

    /**
     * Remove a row from the model.
     *
     * @param ri the row index
     */
    public void removeRow(final int ri) {
        rows.remove(ri);
        fireIntervalRemoved(this, ri, ri);
    }

    /**
     * Remove a row from the model.
     *
     * @param o the row to remove
     */
    public void removeRow(final T o) {
        int ri = rows.indexOf(o);
        if (ri > -1) {
            rows.remove(ri);
            fireIntervalRemoved(this, ri, ri);
        }
    }

    /**
     * Removes all rows from the model.
     */
    public void removeRows() {
        int ri = rows.size() - 1;
        if (ri > -1) {
            rows.clear();
            fireIntervalRemoved(this, 0, ri);
        }
    }
}
