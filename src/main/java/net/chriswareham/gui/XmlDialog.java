/*
 * @(#) XmlDialog.java
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
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.xml.sax.SAXException;

/**
 * This class provides a dialog for displaying XML as a tree of element nodes.
 *
 * @author Chris Wareham
 */
public class XmlDialog extends JDialog {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The XML tree.
     */
    private XmlTree tree;

    /**
     * Construct an instance of the dialog for displaying XML as a tree of
     * element nodes.
     *
     * @param parent the parent window
     * @param title the dialog title
     * @param xml the XML to display
     */
    public XmlDialog(final Window parent, final String title, final String xml) {
        super(parent, title, DEFAULT_MODALITY_TYPE);
        createInterface();
        populateInterface(parent, xml);
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

        tree = new XmlTree();
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
     * @param xml the XML to display
     */
    private void populateInterface(final Window parent, final String xml) {
        try {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            tree.setXml(xml);

            setLocationRelativeTo(parent);

            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (IOException | SAXException exception) {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            ErrorDialog.showDialog(parent, "Error", "Error displaying XML", exception);
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
}
