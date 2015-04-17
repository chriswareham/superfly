/*
 * @(#) DateCellEditor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Component;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

/**
 * This class provides an editor for table cells containing dates.
 *
 * @author Chris Wareham
 */
public class DateCellEditor extends DefaultCellEditor {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The date field editor component.
     */
    private final DateField dateField;

    /**
     * Construct an instance of the editor for table cells containing dates.
     *
     * @param df the date field editor component
     */
    public DateCellEditor(final DateField df) {
        super(df);
        dateField = df;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
        dateField.setValue(value);
        return dateField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCellEditorValue() {
        return dateField.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopCellEditing() {
        if (dateField.isEditValid()) {
            try {
                dateField.commitEdit();
            } catch (ParseException exception) {
                return super.stopCellEditing();
            }
        }
        return super.stopCellEditing();
    }
}
