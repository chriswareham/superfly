/*
 * @(#) BooleanTableColumn.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

/**
 * This class provides a table column for booleans.
 *
 * @author Chris Wareham
 */
public class BooleanTableColumn extends DefaultTableColumn {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the table column for booleans.
     *
     * @param n the name of the column
     */
    public BooleanTableColumn(final String n) {
        this(n, DEFAULT_TABLE_COLUMN_WIDTH, false, SwingConstants.LEFT);
    }

    /**
     * Construct an instance of the table column for booleans.
     *
     * @param n the name of the column
     * @param w the width of the column
     */
    public BooleanTableColumn(final String n, final int w) {
        this(n, w, false, SwingConstants.LEFT);
    }

    /**
     * Construct an instance of the table column for booleans.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param fw whether the column is fixed width
     */
    public BooleanTableColumn(final String n, final int w, final boolean fw) {
        this(n, w, fw, SwingConstants.LEFT);
    }

    /**
     * Construct an instance of the table column for booleans.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param fw whether the column is fixed width
     * @param a the alignment of the column
     */
    public BooleanTableColumn(final String n, final int w, final boolean fw, final int a) {
        setName(n);
        setPreferredWidth(w);
        if (fw) {
            setMaxWidth(w);
        }

        setEditable(false);
        setSearchable(false);

        BooleanCellRenderer renderer = new BooleanCellRenderer();
        renderer.setHorizontalAlignment(a);
        setCellRenderer(renderer);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(true);
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        setCellEditor(new DefaultCellEditor(checkBox));
    }
}
