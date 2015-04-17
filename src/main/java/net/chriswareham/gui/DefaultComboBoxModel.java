/*
 * @(#) DefaultComboBoxModel.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * This class provides a default combo box model.
 *
 * @param <T> type of the objects that make up the rows of the model
 * @author Chris Wareham
 */
public class DefaultComboBoxModel<T> extends AbstractListModel<T> implements ComboBoxModel<T> {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list of rows that make up the model.
     */
    private final List<T> rows = new ArrayList<>();
    /**
     * The selected row index.
     */
    private T selectedRow;

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
     * @return the row
     */
    @Override
    public T getElementAt(final int ri) {
        return rows.get(ri);
    }

    /**
     * Get the selected row.
     *
     * @return the selected row, or null if no row is selected
     */
    @Override
    public Object getSelectedItem() {
        return selectedRow;
    }

    /**
     * Set the selected row.
     *
     * @param o the selected row
     */
    @Override
    public void setSelectedItem(final Object o) {
        if ((selectedRow != null && !selectedRow.equals(o)) || (selectedRow == null && o != null)) {
            selectedRow = getRow(rows.indexOf(o));
            fireContentsChanged(this, -1, -1);
        }
    }

    /**
     * Set the index of the selected row from the model.
     *
     * @param ri the index of the selected row
     */
    public void setSelectedRowIndex(final int ri) {
        setSelectedItem(getRow(ri));
    }

    /**
     * Get the index of the selected row from the model.
     *
     * @return the index of the selected row, or -1 if no row is selected
     */
    public int getSelectedRowIndex() {
        return selectedRow != null ? rows.indexOf(selectedRow) : -1;
    }

    /**
     * Get the selected row from the model.
     *
     * @return the selected row, or null if no row is selected
     */
    public T getSelectedRow() {
        return selectedRow;
    }

    /**
     * Get a row from the model.
     *
     * @param ri the row index
     * @return the row, or null if the row index is out of range
     */
    public T getRow(final int ri) {
        return ri > -1 && ri < rows.size() ? rows.get(ri) : null;
    }

    /**
     * Add a row to the model.
     *
     * @param r the row to add
     */
    public void addRow(final T r) {
        addRow(rows.size(), r);
    }

    /**
     * Add a row to the model at a specific index.
     *
     * @param ri the row index
     * @param r the row to add
     */
    public void addRow(final int ri, final T r) {
        rows.add(ri, r);
        fireIntervalAdded(this, ri, ri);
        if (ri == 0 && selectedRow == null) {
            setSelectedItem(r);
        }
    }

    /**
     * Add multiple rows to the model.
     *
     * @param r the rows to add
     */
    public void addRows(final T[] r) {
        addRows(Arrays.asList(r));
    }

    /**
     * Add multiple rows to the model.
     *
     * @param r the rows to add
     */
    public void addRows(final Collection<T> r) {
        if (!r.isEmpty()) {
            int ri = rows.size();
            rows.addAll(r);
            fireIntervalAdded(this, ri, ri + r.size() - 1);
            if (ri == 0 && selectedRow == null) {
                setSelectedItem(r.iterator().next());
            }
        }
    }

    /**
     * Remove a particular row from the model.
     *
     * @param r the row to remove
     */
    public void removeRow(final T r) {
        int ri = rows.indexOf(r);
        if (ri != -1) {
            removeRow(ri);
        }
    }

    /**
     * Remove a particular row from the model.
     *
     * @param ri the row index
     */
    public void removeRow(final int ri) {
        if (rows.get(ri) == selectedRow) {
            if (ri == 0) {
                setSelectedItem(rows.size() == 1 ? null : getRow(ri + 1));
            } else {
                setSelectedItem(getRow(ri - 1));
            }
        }
        rows.remove(ri);
        fireIntervalRemoved(this, ri, ri);
    }

    /**
     * Removes all rows from the model.
     */
    public void removeRows() {
        if (!rows.isEmpty()) {
            int ri = rows.size() - 1;
            rows.clear();
            selectedRow = null;
            fireIntervalRemoved(this, 0, ri);
        }
    }
}
