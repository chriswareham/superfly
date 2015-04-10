/*
 * @(#) DateTableColumn.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.SwingConstants;

/**
 * This class provides a table column for dates.
 *
 * @author Chris Wareham
 */
public class DateTableColumn extends DefaultTableColumn {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the table column for dates.
     *
     * @param n the name of the column
     */
    public DateTableColumn(final String n) {
        this(n, DEFAULT_TABLE_COLUMN_WIDTH, false, SwingConstants.LEFT, new SimpleDateFormat());
    }

    /**
     * Construct an instance of the table column for dates.
     *
     * @param n the name of the column
     * @param w the width of the column
     */
    public DateTableColumn(final String n, final int w) {
        this(n, w, false, SwingConstants.LEFT, new SimpleDateFormat());
    }

    /**
     * Construct an instance of the table column for dates.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param fw whether the column is fixed width
     */
    public DateTableColumn(final String n, final int w, final boolean fw) {
        this(n, w, fw, SwingConstants.LEFT, new SimpleDateFormat());
    }

    /**
     * Construct an instance of the table column for dates.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param fw whether the column is fixed width
     * @param a the alignment of the column
     */
    public DateTableColumn(final String n, final int w, final boolean fw, final int a) {
        this(n, w, fw, a, new SimpleDateFormat());
    }

    /**
     * Construct an instance of the table column for dates.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param fw whether the column is fixed width
     * @param a the alignment of the column
     * @param df the date format
     */
    public DateTableColumn(final String n, final int w, final boolean fw, final int a, final DateFormat df) {
        setName(n);
        setPreferredWidth(w);
        if (fw) {
            setMaxWidth(w);
        }

        setEditable(false);
        setSearchable(false);

        DateCellRenderer renderer = new DateCellRenderer(df);
        renderer.setHorizontalAlignment(a);
        setCellRenderer(renderer);

        setCellEditor(new DateCellEditor(new DateField(df)));
    }
}
