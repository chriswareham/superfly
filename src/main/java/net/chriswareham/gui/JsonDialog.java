/*
 * @(#) JsonDialog.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides a dialog for displaying JSON as a tree.
 *
 * @author Chris Wareham
 */
public class JsonDialog extends JDialog {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The JSON tree.
     */
    private JTree tree;

    /**
     * Construct an instance of the dialog for displaying JSON as a tree.
     *
     * @param parent the parent window
     * @param title the dialog title
     * @param json the JSON to display
     */
    public JsonDialog(final Window parent, final String title, final String json) {
        super(parent, title, DEFAULT_MODALITY_TYPE);
        createInterface();
        populateInterface(parent, json);
    }

    /**
     * Create the interface.
     */
    private void createInterface() {
        setSize(640, 480);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                close();
            }
        });

        tree = new JTree();
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
        getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton button = new JButton("Close");
        button.addActionListener((final ActionEvent event) -> {
            close();
        });
        buttonPanel.add(button);
    }

    /**
     * Populate the interface.
     *
     * @param parent the parent window
     * @param json the JSON to display
     */
    private void populateInterface(final Window parent, final String json) {
        try {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            tree.setModel(new Handler().parse(json));
            for (int i = 0, rowCount = tree.getRowCount(); i < rowCount; ++i) {
                tree.expandRow(i);
            }

            setLocationRelativeTo(parent);

            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (JSONException exception) {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            ErrorDialog.showDialog(parent, "Error", "Error displaying JSON", exception);
            dispose();
        }
    }

    /**
     * Close the dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }

    /**
     * This class provides a handler for parsing JSON into a TreeModel.
     */
    private static class Handler {

        public DefaultTreeModel parse(final String json) throws JSONException {
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode();

            JSONObject obj = new JSONObject(json);
            parse(treeNode, obj);

            return new DefaultTreeModel(treeNode);
        }

        private void parse(final DefaultMutableTreeNode parentNode, final JSONObject json) {
            for (String name : json.keySet()) {
                Object obj = json.get(name);

                if (obj == null) {
                    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(name + ": -");
                    parentNode.add(treeNode);
                } else if (obj instanceof JSONArray) {
                    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(name);
                    parentNode.add(treeNode);
                    parse(treeNode, (JSONArray) obj);
                } else if (obj instanceof JSONObject) {
                    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(name);
                    parentNode.add(treeNode);
                    parse(treeNode, (JSONObject) obj);
                } else  {
                    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(name + ": " + obj.toString());
                    parentNode.add(treeNode);
                }
            }
         }

       private void parse(final DefaultMutableTreeNode parentNode, final JSONArray json) {
            for (int i = 0; i < json.length(); ++i) {
                Object obj = json.get(i);

                if (obj == null) {
                    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("-");
                    parentNode.add(treeNode);
                } else if (obj instanceof JSONArray) {
                    parse(parentNode, (JSONArray) obj);
                } else if (obj instanceof JSONObject) {
                    parse(parentNode, (JSONObject) obj);
                } else  {
                    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(obj.toString());
                    parentNode.add(treeNode);
                }
            }
        }
    }
}
