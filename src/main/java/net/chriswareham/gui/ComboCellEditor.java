/*
 * @(#) ComboCellEditor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 * This class provides an editor for table cells containing combo boxes.
 *
 * @author Chris Wareham
 * @param <T> type of the objects that make up the rows of the combo box model
 */
public class ComboCellEditor<T extends Comparable<? super T>> extends DefaultCellEditor {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The combo box editor component.
     */
    private final JComboBox<T> comboBox;

    /**
     * Construct an instance of the editor for table cells containing combo
     * boxes.
     *
     * @param cb the combo box editor component
     */
    public ComboCellEditor(final JComboBox<T> cb) {
        super(cb);
        comboBox = cb;
        comboBox.setRequestFocusEnabled(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
        comboBox.setSelectedItem(value);
        comboBox.setFont(table.getFont());
        return comboBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
}
