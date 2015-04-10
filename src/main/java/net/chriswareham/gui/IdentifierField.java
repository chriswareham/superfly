/*
 * @(#) IdentifierField.java
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
 * This class provides a text field that only allows valid identifiers to be
 * entered.
 *
 * @author Chris Wareham
 */
public class IdentifierField extends JTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The default identifier pattern.
     */
    private static final Pattern DEFAULT_PATTERN = Pattern.compile("\\w+");

    /**
     * Construct a new instance of the identifier field.
     */
    public IdentifierField() {
        this(DEFAULT_PATTERN, null, 0, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param l preferred width of the identifier field in characters
     */
    public IdentifierField(final int l) {
        this(DEFAULT_PATTERN, null, l, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param l preferred width of the identifier field in characters
     * @param ml maximum length of the identifier that can be entered
     */
    public IdentifierField(final int l, final int ml) {
        this(DEFAULT_PATTERN, null, l, ml);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param i initial identifier to be displayed
     */
    public IdentifierField(final String i) {
        this(DEFAULT_PATTERN, i, 0, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param i initial identifier to be displayed
     * @param l preferred width of the identifier field in characters
     */
    public IdentifierField(final String i, final int l) {
        this(DEFAULT_PATTERN, i, l, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param i initial identifier to be displayed
     * @param l preferred width of the identifier field in characters
     * @param ml maximum length of the identifier that can be entered
     */
    public IdentifierField(final String i, final int l, final int ml) {
        this(DEFAULT_PATTERN, i, l, ml);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param p the identifier pattern
     */
    public IdentifierField(final Pattern p) {
        this(p, null, 0, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param p the identifier pattern
     * @param l preferred width of the identifier field in characters
     */
    public IdentifierField(final Pattern p, final int l) {
        this(p, null, l, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param p the identifier pattern
     * @param l preferred width of the identifier field in characters
     * @param ml maximum length of the identifier that can be entered
     */
    public IdentifierField(final Pattern p, final int l, final int ml) {
        this(p, null, l, ml);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param p the identifier pattern
     * @param i initial identifier to be displayed
     */
    public IdentifierField(final Pattern p, final String i) {
        this(p, i, 0, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param p the identifier pattern
     * @param i initial identifier to be displayed
     * @param l preferred width of the identifier field in characters
     */
    public IdentifierField(final Pattern p, final String i, final int l) {
        this(p, i, l, 0);
    }

    /**
     * Construct an instance of the identifier field.
     *
     * @param p the identifier pattern
     * @param i initial identifier to be displayed
     * @param l preferred width of the identifier field in characters
     * @param ml maximum length of the identifier that can be entered
     */
    public IdentifierField(final Pattern p, final String i, final int l, final int ml) {
        super(new IdentifierDocument(p, ml), i, l);
    }

    /**
     * This class provides a document that enforces a maximum length and valid
     * identifier characters.
     */
    private static class IdentifierDocument extends PlainDocument {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The maximum length of the identifier that can be entered.
         */
        private int maximumLength;
        /**
         * The identifier pattern.
         */
        private Pattern pattern;

        /**
         * Construct an instance of the identifier document.
         *
         * @param p the identifier pattern
         * @param ml the maximum length of the identifier that can be entered
         */
        public IdentifierDocument(final Pattern p, final int ml) {
            pattern = p;
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
            } else if (!pattern.matcher(text).matches()) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                super.insertString(offset, str, attributeSet);
            }
        }
    }
}
