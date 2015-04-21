/*
 * @(#) TelephoneField.java
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
 * This class provides a text field that only allows valid UK telephone
 * characters to be entered.
 *
 * @author Chris Wareham
 */
public class TelephoneField extends JTextField {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new instance of the telephone field.
     */
    public TelephoneField() {
        this(null, 0, 0);
    }

    /**
     * Construct an instance of the telephone field.
     *
     * @param l preferred width of the telephone field in characters
     */
    public TelephoneField(final int l) {
        this(null, l, 0);
    }

    /**
     * Construct an instance of the telephone field.
     *
     * @param l preferred width of the telephone field in characters
     * @param ml maximum length of the telephone that can be entered
     */
    public TelephoneField(final int l, final int ml) {
        this(null, l, ml);
    }

    /**
     * Construct an instance of the telephone field.
     *
     * @param telephone initial telephone to be displayed
     */
    public TelephoneField(final String telephone) {
        this(telephone, 0, 0);
    }

    /**
     * Construct an instance of the telephone field.
     *
     * @param telephone initial telephone to be displayed
     * @param l preferred width of the telephone field in characters
     */
    public TelephoneField(final String telephone, final int l) {
        this(telephone, l, 0);
    }

    /**
     * Construct an instance of the telephone field.
     *
     * @param telephone initial telephone to be displayed
     * @param l preferred width of the telephone field in characters
     * @param ml maximum length of the telephone that can be entered
     */
    public TelephoneField(final String telephone, final int l, final int ml) {
        super(new TelephoneDocument(ml), telephone, l);
    }

    /**
     * This class provides a document that enforces a maximum length and the
     * validity of telephone characters.
     */
    private static final class TelephoneDocument extends PlainDocument {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * The pattern that matches valid telephone characters.
         */
        private static final Pattern PATTERN = Pattern.compile("[0-9()+ ]+");

        /**
         * The maximum length of the telephone that can be entered.
         */
        private final int maximumLength;

        /**
         * Create an instance of the telephone document.
         *
         * @param ml the maximum length of the telephone that can be entered
         */
        private TelephoneDocument(final int ml) {
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
