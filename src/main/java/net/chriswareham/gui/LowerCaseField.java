/*
 * @(#) LowerCaseField.java
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
 * This class provides a text field that only allows lower case characters to
 * be entered.
 *
 * @author Chris Wareham
 */
public class LowerCaseField extends JTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the lower case field.
     */
    public LowerCaseField() {
        this(null, 0, 0);
    }

    /**
     * Construct an instance of the lower case field.
     *
     * @param l preferred width of the lower case field in characters
     */
    public LowerCaseField(final int l) {
        this(null, l, 0);
    }

    /**
     * Construct an instance of the lower case field.
     *
     * @param l preferred width of the lower case field in characters
     * @param ml maximum length of the lower case characters that can be entered
     */
    public LowerCaseField(final int l, final int ml) {
        this(null, l, ml);
    }

    /**
     * Construct an instance of the lower case field.
     *
     * @param value initial value to be displayed
     */
    public LowerCaseField(final String value) {
        this(value, 0, 0);
    }

    /**
     * Construct an instance of the lower case field.
     *
     * @param value initial value to be displayed
     * @param l preferred width of the lower case field in characters
     */
    public LowerCaseField(final String value, final int l) {
        this(value, l, 0);
    }

    /**
     * Construct an instance of the lower case field.
     *
     * @param value initial value to be displayed
     * @param l preferred width of the lower case field in characters
     * @param ml maximum length of the lower case characters that can be entered
     */
    public LowerCaseField(final String value, final int l, final int ml) {
        super(new LowerCaseDocument(ml), value, l);
    }

    /**
     * This class provides a document that enforces a maximum length and lower
     * case characters.
     */
    private static class LowerCaseDocument extends PlainDocument {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * The maximum length of the characters that can be entered.
         */
        private final int maximumLength;

        /**
         * Create an instance of the lower case document.
         *
         * @param ml the maximum length of the characters that can be entered
         */
        LowerCaseDocument(final int ml) {
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
                super.insertString(offset, str.toLowerCase(), attributeSet);
            }
        }
    }
}
