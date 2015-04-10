/*
 * @(#) StringTableColumn.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class provides a table column for strings.
 *
 * @author Chris Wareham
 */
public class StringTableColumn extends DefaultTableColumn {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the table column for strings.
     *
     * @param n the name of the column
     */
    public StringTableColumn(final String n) {
        this(n, DEFAULT_TABLE_COLUMN_WIDTH, false, SwingConstants.LEFT);
    }

    /**
     * Construct an instance of the table column for strings.
     *
     * @param n the name of the column
     * @param w the width of the column
     */
    public StringTableColumn(final String n, final int w) {
        this(n, w, false, SwingConstants.LEFT);
    }

    /**
     * Construct an instance of the table column for strings.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param fw whether the column is fixed width
     */
    public StringTableColumn(final String n, final int w, final boolean fw) {
        this(n, w, fw, SwingConstants.LEFT);
    }

    /**
     * Construct an instance of the table column for strings.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param fw whether the column is fixed width
     * @param a the alignment of the column
     */
    public StringTableColumn(final String n, final int w, final boolean fw, final int a) {
        setName(n);
        setPreferredWidth(w);
        if (fw) {
            setMaxWidth(w);
        }

        setEditable(false);
        setSearchable(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(a);
        setCellRenderer(renderer);
    }
}
