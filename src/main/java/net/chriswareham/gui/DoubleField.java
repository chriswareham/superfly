/*
 * @(#) DoubleField.java
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
 * This class provides a text field that only allows doubles to be entered.
 *
 * @author Chris Wareham
 */
public class DoubleField extends JTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The double pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");

    /**
     * Construct an instance of the double field.
     */
    public DoubleField() {
        this(null, 0, 0);
    }

    /**
     * Construct an instance of the double field.
     *
     * @param l preferred width of the double field in characters
     */
    public DoubleField(final int l) {
        this(null, l, 0);
    }

    /**
     * Construct an instance of the double field.
     *
     * @param l preferred width of the double field in characters
     * @param ml maximum number of digits that can be entered
     */
    public DoubleField(final int l, final int ml) {
        this(null, l, ml);
    }

    /**
     * Construct an instance of the double field.
     *
     * @param d initial double to be displayed
     */
    public DoubleField(final Double d) {
        this(d, 0, 0);
    }

    /**
     * Construct an instance of the double field.
     *
     * @param d initial double to be displayed
     * @param l preferred width of the double field in characters
     */
    public DoubleField(final Double d, final int l) {
        this(d, l, 0);
    }

    /**
     * Construct an instance of the double field.
     *
     * @param d initial double to be displayed
     * @param l preferred width of the double field in characters
     * @param ml maximum number of digits that can be entered
     */
    public DoubleField(final Double d, final int l, final int ml) {
        super(new DoubleDocument(ml), d != null ? d.toString() :  null, l);
    }

    /**
     * Get the double currently entered.
     *
     * @return the double currently entered
     */
    public Double getDouble() {
        String text = getText();
        return PATTERN.matcher(text).matches() ? Double.valueOf(text) : 0.0;
    }

    /**
     * Set the double currently entered.
     *
     * @param d the double currently entered
     */
    public void setDouble(final Double d) {
        setText(d.toString());
    }

    /**
     * This class provides a document that enforces a maximum length and a valid
     * double.
     */
    private static class DoubleDocument extends PlainDocument {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * The insert pattern.
         */
        private static final Pattern PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]*");

        /**
         * The maximum number of the double that can be entered.
         */
        private final int maximumLength;

        /**
         * Create an instance of the double document.
         *
         * @param ml the maximum length of the double that can be entered
         */
        private DoubleDocument(final int ml) {
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
