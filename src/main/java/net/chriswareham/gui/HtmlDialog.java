/*
 * @(#) HtmlDialog.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * This class provides a dialog for displaying HTML.
 *
 * @author Chris Wareham
 */
public class HtmlDialog extends JDialog {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The style sheet.
     */
    private static final StyleSheet STYLE_SHEET;

    static {
        STYLE_SHEET = new StyleSheet();
        STYLE_SHEET.addRule("body { background-color: #FEFEFE; color: black; }");
        STYLE_SHEET.addRule("h1 { font-size: 1.5em; margin: 0; padding: 8px 0 4px 0; }");
        STYLE_SHEET.addRule("h2 { font-size: 1.3em; margin: 0; padding: 4px 0 4px 0; }");
        STYLE_SHEET.addRule("h3 { font-size: 1.1em; margin: 0; padding: 4px 0 4px 0; }");
        STYLE_SHEET.addRule("p { font-size: 1.0em; margin: 0; padding: 2px 4px 2px 4px; }");
        STYLE_SHEET.addRule("li { font-size: 1.0em; margin: 0; padding: 2px 4px 2px 8px; }");
        STYLE_SHEET.addRule("strong { font-weight: bold; }");
        STYLE_SHEET.addRule("em { font-style: italic; }");
    }

    /**
     * The HTML editor pane.
     */
    private JEditorPane editorPane;

    /**
     * Construct an instance of the dialog for displaying HTML.
     *
     * @param parent the parent window
     * @param title the dialog title
     * @param html the HTML to display
     */
    public HtmlDialog(final Window parent, final String title, final String html) {
        super(parent, title, DEFAULT_MODALITY_TYPE);
        createInterface();
        populateInterface(parent, html);
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

        HTMLEditorKit editorKit = new HTMLEditorKit();
        editorKit.setStyleSheet(STYLE_SHEET);

        editorPane = new JEditorPane();
        editorPane.setEditorKit(editorKit);
        editorPane.setEditable(false);
        getContentPane().add(new JScrollPane(editorPane), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton button = new JButton("Close");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                close();
            }
        });
        buttonPanel.add(button);
    }

    /**
     * Populate the interface.
     *
     * @param parent the parent window
     * @param html the HTML to display
     */
    private void populateInterface(final Window parent, final String html) {
        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        editorPane.setText(html);
        editorPane.setCaretPosition(0);

        setLocationRelativeTo(parent);

        parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Close the dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }
}
