/*
 * @(#) DefaultTextArea.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import javax.swing.JTextArea;

/**
 * This class provides a default text area.
 *
 * @author Chris Wareham
 */
public class DefaultTextArea extends JTextArea {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the default text area.
     */
    public DefaultTextArea() {
        super(12, 24);
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(final String t) {
        super.setText(t);
        setCaretPosition(0);
    }
}
