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
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

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
    private JTree tree;

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
     * @param xml the XML to display
     */
    private void populateInterface(final Window parent, final String xml) {
        try {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            Handler handler = new Handler();

            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));

            tree.setModel(handler.getTreeModel());
            for (int i = 0, rowCount = tree.getRowCount(); i < rowCount; ++i) {
                tree.expandRow(i);
            }

            setLocationRelativeTo(parent);

            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (IOException | SAXException exception) {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            ErrorDialog.showDialog(parent, "Error", exception);
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
     * This class provides a handler for parsing XML into a TreeModel.
     */
    private static class Handler extends DefaultHandler {
        /**
         * The buffer for capturing data.
         */
        private final StringBuilder buf;
        /**
         * The current node of the tree model.
         */
        private DefaultMutableTreeNode currentNode;
        /**
         * The tree model to populate.
         */
        private final DefaultTreeModel treeModel;

        /**
         * Constructs a new instance of the Handler class.
         */
        private Handler() {
            buf = new StringBuilder();
            currentNode = new DefaultMutableTreeNode("<?xml version='1.0'?>");
            treeModel = new DefaultTreeModel(currentNode);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes atts) throws SAXException {
            if (buf.length() > 0) {
                DefaultMutableTreeNode data = new DefaultMutableTreeNode(buf.toString());
                currentNode.add(data);
                buf.delete(0, buf.length());
            }

            buf.append('<');
            buf.append(localName);
            int len = atts.getLength();
            for (int i = 0; i < len; ++i) {
                buf.append(' ');
                buf.append(atts.getLocalName(i));
                buf.append("='");
                buf.append(atts.getValue(i));
                buf.append('\'');
            }
            buf.append('>');
            DefaultMutableTreeNode element = new DefaultMutableTreeNode(buf.toString());
            buf.delete(0, buf.length());
            currentNode.add(element);
            currentNode = element;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if (buf.length() > 0) {
                DefaultMutableTreeNode data = new DefaultMutableTreeNode(buf.toString());
                currentNode.add(data);
                buf.delete(0, buf.length());
            }

            currentNode = (DefaultMutableTreeNode) currentNode.getParent();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void characters(final char[] ch, final int start, final int length) throws SAXException {
            buf.append(ch, start, length);
        }

        /**
         * Get the tree model.
         *
         * @return the tree model
         */
        public DefaultTreeModel getTreeModel() {
            return treeModel;
        }
    }
}
