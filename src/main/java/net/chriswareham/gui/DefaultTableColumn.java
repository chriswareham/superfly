/*
 * @(#) DefaultTableColumn.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import javax.swing.table.TableColumn;

/**
 * This class provides a default table column.
 *
 * @author Chris Wareham
 */
public abstract class DefaultTableColumn extends TableColumn {
    /**
     * The default column width.
     */
    public static final int DEFAULT_TABLE_COLUMN_WIDTH = 75;
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the column.
     */
    private String name;
    /**
     * Whether the column is editable.
     */
    private boolean editable;
    /**
     * Whether the column is searchable.
     */
    private boolean searchable;

    /**
     * Get the name of the column.
     *
     * @return the name of the column
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the column.
     *
     * @param n the name of the column
     */
    public void setName(final String n) {
        name = n;
    }

    /**
     * Get whether the column is editable.
     *
     * @return whether the column is editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Set whether the column is editable.
     *
     * @param e whether the column is editable
     */
    public void setEditable(final boolean e) {
        editable = e;
    }

    /**
     * Get whether the column is searchable.
     *
     * @return whether the column is searchable
     */
    public boolean isSearchable() {
        return searchable;
    }

    /**
     * Set whether the column is searchable.
     *
     * @param s whether the column is searchable
     */
    public void setSearchable(final boolean s) {
        searchable = s;
    }
}
