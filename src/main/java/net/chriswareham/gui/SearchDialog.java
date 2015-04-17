/*
 * @(#) LoginDialog.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This class provides a dialog to generate search criteria for a table model.
 *
 * @author Chris Wareham
 */
public class SearchDialog extends JDialog {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The event listeners.
     */
    private final List<SearchDialogListener> listeners = new CopyOnWriteArrayList<>();
    /**
     * The search string text field.
     */
    private JTextField textField;
    /**
     * The table model to list the searchable columns.
     */
    private SearchTableModel searchTableModel;
    /**
     * The exact match check box.
     */
    private JCheckBox checkBox;

    /**
     * Construct an instance of dialog to generate search criteria for a table
     * model.
     *
     * @param parent the parent window
     * @param tableModel the table model to generate the search criteria for
     */
    public SearchDialog(final Window parent, final SortedTableModel<?> tableModel) {
        super(parent, "Search", DEFAULT_MODALITY_TYPE);

        createInterface(parent, tableModel);
    }

    /**
     * Add a listener.
     *
     * @param listener the listener to add
     */
    public void addSearchDialogListener(final SearchDialogListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener.
     *
     * @param listener the listener to remove
     */
    public void removeSearchDialogListener(final SearchDialogListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fire an event notifying listeners that a search should be performed.
     *
     * @param event describes the event
     */
    private void fireSearchPerformed(final SearchDialogEvent event) {
        for (SearchDialogListener listener : listeners) {
            listener.searchPerformed(event);
        }
    }

    /**
     * Create the interface.
     *
     * @param parent the parent window
     * @param tableModel the table model to list the searchable table columns of
     */
    private void createInterface(final Window parent, final SortedTableModel<?> tableModel) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                close();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Search for:", JLabel.RIGHT), c);

        c.weightx = 1.0;
        c.gridx++;
        textField = new JTextField(24);
        panel.add(textField, c);

        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Exact match:", JLabel.RIGHT), c);

        c.weightx = 1.0;
        c.gridx++;
        c.fill = GridBagConstraints.NONE;
        checkBox = new JCheckBox();
        panel.add(checkBox, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy++;

        searchTableModel = new SearchTableModel(tableModel);
        JTable table = new DefaultTable(searchTableModel);
        Dimension d = table.getPreferredScrollableViewportSize();
        d.height /= 2;

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(table.getBackground());
        panel.add(scrollPane, c);

        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton button = new JButton("Search");
        button.addActionListener((final ActionEvent event) -> {
            search();
        });
        buttonPanel.add(button);

        button = new JButton("Close");
        button.addActionListener((final ActionEvent event) -> {
            close();
        });
        buttonPanel.add(button);

        pack();

        setLocationRelativeTo(parent);
    }

    /**
     * Close the dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }

    /**
     * Search.
     */
    private void search() {
        String str = textField.getText().trim();
        if (!str.isEmpty()) {
            List<Integer> columns = new ArrayList<>(searchTableModel.getRowCount());
            for (SearchTableRow row : searchTableModel.getRows()) {
                if (row.isSelected()) {
                    columns.add(row.getIndex());
                }
            }
            if (!columns.isEmpty()) {
                fireSearchPerformed(new SearchDialogEvent(this, str, columns, checkBox.isSelected()));
            }
        }
    }

    /**
     * Table model row.
     */
    private static class SearchTableRow {
        /** The table column name. */
        private final String name;
        /** The table column index. */
        private final int index;
        /** Whether the table column is selected. */
        private boolean selected;

        /**
         * Construct an instance of the table model row.
         *
         * @param n the table column name
         * @param s whether the table column is selected
         * @param i the table column index
         */
        private SearchTableRow(final String n, final boolean s, final int i) {
            name = n;
            index = i;
            selected = s;
        }

        /**
         * Get the table column name.
         *
         * @return the table column name
         */
        public String getName() {
            return name;
        }

        /**
         * Get whether the table column is selected.
         *
         * @return whether the table column is selected
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Set whether the table column is selected.
         *
         * @param s whether the table column is selected
         */
        public void setSelected(final boolean s) {
            selected = s;
        }

        /**
         * Get the table column index.
         *
         * @return the table column index
         */
        public int getIndex() {
            return index;
        }
    }

    /**
     * Search table model.
     */
    private static class SearchTableModel extends DefaultTableModel<SearchTableRow> {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructs a new instance of the search table model.
         *
         * @param tableModel the table model to list the searchable table columns of
         */
        private SearchTableModel(final SortedTableModel<?> tableModel) {
            addColumn(new StringTableColumn("Column", 225));
            addColumn(new BooleanTableColumn("Search", 75, true, SwingConstants.CENTER));

            setColumnEditable(1, true);

            for (int ci = 0, nc = tableModel.getColumnCount(); ci < nc; ++ci) {
                if (tableModel.isColumnSearchable(ci)) {
                    addRow(new SearchTableRow(tableModel.getColumnName(ci), false, ci));
                }
            }
        }

        /**
         * Gets the value in a cell in the table model.
         *
         * @param ri the index of the row the cell is in
         * @param ci the index of the column the cell is in
         * @return the value of the cell
         */
        @Override
        public Object getValueAt(final int ri, final int ci) {
            SearchTableRow row = getRow(ri);
            switch (ci) {
            case 0:
                return row.getName();
            case 1:
                return row.isSelected() ? Boolean.TRUE : Boolean.FALSE;
            default:
                throw new IllegalArgumentException("Invalid column " + ci);
            }
        }

        /**
         * Sets the value in a cell in the table model.
         *
         * @param ri the index of the row the cell is in
         * @param ci the index of the column the cell is in
         * @param value the value of the cell
         */
        @Override
        public void setValueAt(final Object value, final int ri, final int ci) {
            SearchTableRow row = getRow(ri);
            switch (ci) {
            case 0:
                break;
            case 1:
                Boolean selected = (Boolean) value;
                row.setSelected(selected);
                fireTableCellUpdated(ri, ci);
                break;
            default:
                throw new IllegalArgumentException("Invalid column " + ci);
            }
        }
    }
}
