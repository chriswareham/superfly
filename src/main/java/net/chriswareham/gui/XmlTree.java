/*
 * @(#) XmlTree.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.io.IOException;
import java.io.StringReader;

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
 * This class provides a tree for displaying XML as a tree of element nodes.
 *
 * @author Chris Wareham
 */
public class XmlTree extends JTree {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the tree for displaying XML as a tree of
     * element nodes.
     */
    public XmlTree() {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) getCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
    }

    public void setXml(final String xml) throws IOException, SAXException {
        Handler handler = new Handler();

        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(new StringReader(xml)));

        setModel(handler.getTreeModel());
        for (int i = 0, rowCount = getRowCount(); i < rowCount; ++i) {
            expandRow(i);
        }
    }

    /**
     * This class provides a handler for parsing XML into a TreeModel.
     */
    private static final class Handler extends DefaultHandler {
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
