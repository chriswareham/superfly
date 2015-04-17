/*
 * @(#) IntegerField.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Toolkit;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class provides a text field that only allows integers to be entered.
 *
 * @author Chris Wareham
 */
public class IntegerField extends JTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The integer pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("[-+]?[0-9]+");

    /**
     * Construct an instance of the integer field.
     */
    public IntegerField() {
        this(null, 0, 0);
    }

    /**
     * Construct an instance of the integer field.
     *
     * @param l preferred width of the integer field in characters
     */
    public IntegerField(final int l) {
        this(null, l, 0);
    }

    /**
     * Construct an instance of the integer field.
     *
     * @param l preferred width of the integer field in characters
     * @param ml maximum number of digits that can be entered
     */
    public IntegerField(final int l, final int ml) {
        this(null, l, ml);
    }

    /**
     * Construct an instance of the integer field.
     *
     * @param i initial integer to be displayed
     */
    public IntegerField(final Integer i) {
        this(i, 0, 0);
    }

    /**
     * Construct an instance of the integer field.
     *
     * @param i initial integer to be displayed
     * @param l preferred width of the integer field in characters
     */
    public IntegerField(final Integer i, final int l) {
        this(i, l, 0);
    }

    /**
     * Construct an instance of the integer field.
     *
     * @param i initial integer to be displayed
     * @param l preferred width of the integer field in characters
     * @param ml maximum number of digits that can be entered
     */
    public IntegerField(final Integer i, final int l, final int ml) {
        super(new IntegerDocument(ml), i != null ? i.toString() :  null, l);
    }

    /**
     * Get the integer currently entered.
     *
     * @return the integer currently entered
     */
    public Integer getInteger() {
        String text = getText();
        return PATTERN.matcher(text).matches() ? Integer.valueOf(text) : 0;
    }

    /**
     * Set the integer currently entered.
     *
     * @param i the integer currently entered
     */
    public void setInteger(final Integer i) {
        setText(i.toString());
    }

    /**
     * This class provides a document that enforces a maximum length and a valid
     * integer.
     */
    private static class IntegerDocument extends PlainDocument {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * The insert pattern.
         */
        private static final Pattern PATTERN = Pattern.compile("[-+]?[0-9]*");

        /**
         * The maximum number of the double that can be entered.
         */
        private final int maximumLength;

        /**
         * Create an instance of the double document.
         *
         * @param ml the maximum length of the double that can be entered
         */
        private IntegerDocument(final int ml) {
            maximumLength = ml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insertString(final int offset, final String str, final AttributeSet attributeSet) throws BadLocationException {
            String text = str;

            if (getLength() > 0) {
                StringBuilder buf = new StringBuilder();
                if (offset > 0) {
                    buf.append(getText(0, offset));
                }
                buf.append(str);
                if (offset < getLength()) {
                    buf.append(getText(offset, getLength() - offset));
                }
                text = buf.toString();
            }

            if (maximumLength > 0 && text.length() > maximumLength) {
                Toolkit.getDefaultToolkit().beep();
            } else if (!PATTERN.matcher(text).matches()) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                super.insertString(offset, str, attributeSet);
            }
        }
    }
}
