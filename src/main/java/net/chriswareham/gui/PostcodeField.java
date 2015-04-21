/*
 * @(#) PostcodeField.java
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
 * This class provides a text field that only allows valid UK postcode
 * characters to be entered.
 *
 * @author Chris Wareham
 */
public class PostcodeField extends JTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of the postcode field.
     */
    public PostcodeField() {
        this(null, 0, 0);
    }

    /**
     * Construct an instance of the postcode field.
     *
     * @param l preferred width of the postcode field in characters
     */
    public PostcodeField(final int l) {
        this(null, l, 0);
    }

    /**
     * Construct an instance of the postcode field.
     *
     * @param l preferred width of the postcode field in characters
     * @param ml maximum length of the postcode that can be entered
     */
    public PostcodeField(final int l, final int ml) {
        this(null, l, ml);
    }

    /**
     * Construct an instance of the postcode field.
     *
     * @param postcode initial postcode to be displayed
     */
    public PostcodeField(final String postcode) {
        this(postcode, 0, 0);
    }

    /**
     * Construct an instance of the postcode field.
     *
     * @param postcode initial postcode to be displayed
     * @param l preferred width of the postcode field in characters
     */
    public PostcodeField(final String postcode, final int l) {
        this(postcode, l, 0);
    }

    /**
     * Construct an instance of the postcode field.
     *
     * @param postcode initial postcode to be displayed
     * @param l preferred width of the postcode field in characters
     * @param ml maximum length of the postcode that can be entered
     */
    public PostcodeField(final String postcode, final int l, final int ml) {
        super(new PostcodeDocument(ml), postcode, l);
    }

    /**
     * This class provides a document that enforces a maximum length and the
     * validity of postcode characters.
     */
    private static final class PostcodeDocument extends PlainDocument {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * The pattern that matches valid postcode characters.
         */
        private static final Pattern PATTERN = Pattern.compile("[a-zA-Z0-9 ]+");

        /**
         * The maximum length of the postcode that can be entered.
         */
        private final int maximumLength;

        /**
         * Create an instance of the telephone document.
         *
         * @param ml the maximum length of the telephone that can be entered
         */
        private PostcodeDocument(final int ml) {
            maximumLength = ml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insertString(final int offset, final String str, final AttributeSet attributeSet) throws BadLocationException {
            if (maximumLength > 0 && getLength() + str.length() > maximumLength) {
                Toolkit.getDefaultToolkit().beep();
            } else if (!PATTERN.matcher(str).matches()) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                super.insertString(offset, str.toUpperCase(), attributeSet);
            }
        }
    }
}
