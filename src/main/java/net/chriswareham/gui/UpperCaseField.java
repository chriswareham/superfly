/*
 * @(#) UpperCaseField.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class provides a text field that only allows upper case characters to
 * be entered.
 *
 * @author Chris Wareham
 */
public class UpperCaseField extends JTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the upper case field.
     */
    public UpperCaseField() {
        this(null, 0, 0);
    }

    /**
     * Construct an instance of the upper case field.
     *
     * @param l preferred width of the upper case field in characters
     */
    public UpperCaseField(final int l) {
        this(null, l, 0);
    }

    /**
     * Construct an instance of the upper case field.
     *
     * @param l preferred width of the upper case field in characters
     * @param ml maximum length of the upper case characters that can be entered
     */
    public UpperCaseField(final int l, final int ml) {
        this(null, l, ml);
    }

    /**
     * Construct an instance of the upper case field.
     *
     * @param value initial value to be displayed
     */
    public UpperCaseField(final String value) {
        this(value, 0, 0);
    }

    /**
     * Construct an instance of the upper case field.
     *
     * @param value initial value to be displayed
     * @param l preferred width of the upper case field in characters
     */
    public UpperCaseField(final String value, final int l) {
        this(value, l, 0);
    }

    /**
     * Construct an instance of the upper case field.
     *
     * @param value initial value to be displayed
     * @param l preferred width of the upper case field in characters
     * @param ml maximum length of the upper case characters that can be entered
     */
    public UpperCaseField(final String value, final int l, final int ml) {
        super(new UpperCaseDocument(ml), value, l);
    }

    /**
     * This class provides a document that enforces a maximum length and upper
     * case characters.
     */
    private static class UpperCaseDocument extends PlainDocument {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * The maximum length of the characters that can be entered.
         */
        private final int maximumLength;

        /**
         * Create an instance of the upper case document.
         *
         * @param ml the maximum length of the characters that can be entered
         */
        UpperCaseDocument(final int ml) {
            maximumLength = ml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insertString(final int offset, final String str, final AttributeSet attributeSet) throws BadLocationException {
            if (maximumLength > 0 && getLength() + str.length() > maximumLength) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                super.insertString(offset, str.toUpperCase(), attributeSet);
            }
        }
    }
}
