/*
 * @(#) ComboTableColumn.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import javax.swing.JComboBox;

/**
 * This class provides a table column for a combo box model.
 *
 * @param <T> type of the objects that make up the rows of the combo box model
 * @author Chris Wareham
 */
public class ComboTableColumn<T extends Comparable<? super T>> extends DefaultTableColumn {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct a table column for a combo box model.
     *
     * @param n the name of the column
     * @param m the combo box model
     */
    public ComboTableColumn(final String n, final DefaultComboBoxModel<T> m) {
        this(n, DEFAULT_TABLE_COLUMN_WIDTH, m);
    }

    /**
     * Construct a table column for a combo box model.
     *
     * @param n the name of the column
     * @param w the width of the column
     * @param m the combo box model
     */
    public ComboTableColumn(final String n, final int w, final DefaultComboBoxModel<T> m) {
        setName(n);
        setPreferredWidth(w);

        setEditable(false);
        setSearchable(false);

        setCellRenderer(new ComboCellRenderer());

        setCellEditor(new ComboCellEditor<>(new JComboBox<>(m)));
    }
}
