/*
 * @(#) XmlWriter.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Convenience class for writing an XML stream.
 *
 * @author Chris Wareham
 */
public class XmlWriter implements Closeable, Flushable {
    /**
     * The tab character (ASCII decimal value).
     */
    public static final int TAB_CHARACTER = 9;
    /**
     * The line feed character (ASCII decimal value).
     */
    public static final int LF_CHARACTER = 10;
    /**
     * The carriage return character (ASCII decimal value).
     */
    public static final int CR_CHARACTER = 13;
    /**
     * The space character (ASCII decimal value).
     */
    public static final int SPACE_CHARACTER = 32;
    /**
     * The tilde character (ASCII decimal value).
     */
    public static final int TILDE_CHARACTER = 126;
    /**
     * The default encoding.
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * The writer states.
     */
    private static enum State {
        /**
         * The prologue writer state.
         */
        WRITING_PROLOGUE,
        /**
         * The text writer state.
         */
        WRITING_TEXT,
        /**
         * The element writer state.
         */
        WRITING_ELEMENT
    }

    /**
     * The writer state.
     */
    private State state = State.WRITING_PROLOGUE;
    /**
     * The encoding.
     */
    private Charset charset;
    /**
     * The stream to write to.
     */
    private OutputStreamWriter out;
    /**
     * The stack of element names.
     */
    private final Deque<String> elements = new ArrayDeque<>();
    /**
     * Whether to double quote attributes rather than single quote them.
     */
    private boolean doubleQuote;

    /**
     * Constructs a writer with the default UTF-8 output encoding.
     *
     * @param os the stream to write to
     */
    public XmlWriter(final OutputStream os) {
        this(os, false, DEFAULT_ENCODING);
    }

    /**
     * Constructs a writer with the specified output encoding.
     *
     * @param os the stream to write to
     * @param dq whether to double quote attributes rather than single quote them
     */
    public XmlWriter(final OutputStream os, final boolean dq) {
        this(os, dq, DEFAULT_ENCODING);
    }

    /**
     * Constructs a writer with the specified output encoding.
     *
     * @param os the stream to write to
     * @param e the encoding to use
     */
    public XmlWriter(final OutputStream os, final String e) {
        this(os, false, e);
    }

    /**
     * Constructs a writer with the specified output encoding.
     *
     * @param os the stream to write to
     * @param dq whether to double quote attributes rather than single quote them
     * @param e the encoding to use
     */
    public XmlWriter(final OutputStream os, final boolean dq, final String e) {
        charset = Charset.forName(e);
        out = new OutputStreamWriter(os, charset);
        doubleQuote = dq;
    }

    /**
     * Get the output encoding.
     *
     * @return the output encoding
     */
    public String getEncoding() {
        return charset.name();
    }

    /**
     * Write an XML declaration to the output stream.
     *
     * @throws IOException if an input or output error occurs
     */
    public void writeDeclaration() throws IOException {
        if (state != State.WRITING_PROLOGUE) {
            throw new IllegalStateException("Not writing prologue");
        }
        out.write("<?xml version='1.0' encoding='");
        out.write(getEncoding());
        out.write("'?>\n");
    }

    /**
     * Write a document type declaration.
     *
     * @param name the document name
     * @param publicId the public identifier
     * @param systemId the system identifier
     * @throws IOException if an input or output error occurs
     */
    public void writeDoctype(final String name, final String publicId, final String systemId) throws IOException {
        if (state != State.WRITING_PROLOGUE) {
            throw new IllegalStateException("Not writing prologue");
        }
        out.write("<!DOCTYPE ");
        out.write(name);
        out.write(" PUBLIC \"");
        out.write(publicId);
        out.write("\" \"");
        out.write(systemId);
        out.write("\">\n");
    }

    /**
     * Start an element.
     *
     * @param name the element name
     * @throws IOException if an input or output error occurs
     */
    @SuppressWarnings("fallthrough")
    public void startElement(final String name) throws IOException {
        elements.addFirst(name);
        switch (state) {
        case WRITING_PROLOGUE:
            // fall through
        case WRITING_TEXT:
            state = State.WRITING_ELEMENT;
            out.write('<');
            out.write(name);
            break;
        case WRITING_ELEMENT:
            out.write("><");
            out.write(name);
            break;
        default:
            break;
        }
    }

    /**
     * End the current element.
     *
     * @throws IOException if an input or output error occurs
     */
    public void endElement() throws IOException {
        String name = elements.removeFirst();
        switch (state) {
        case WRITING_ELEMENT:
            state = State.WRITING_TEXT;
            out.write("/>");
            break;
        case WRITING_TEXT:
            out.write("</");
            out.write(name);
            out.write('>');
            break;
        default:
            break;
        }
    }

    /**
     * Write an empty element to the output stream.
     *
     * @param name the element name
     * @throws IOException if an input or output error occurs
     */
    public void writeElement(final String name) throws IOException {
        startElement(name);
        endElement();
    }

    /**
     * Write a start element, boolean and end element to the output stream.
     *
     * @param name the element name
     * @param value the boolean to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void writeElement(final String name, final boolean value) throws IOException {
        writeElement(name, Boolean.toString(value));
    }

    /**
     * Write a start element, integer and end element to the output stream.
     *
     * @param name the element name
     * @param value the integer to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void writeElement(final String name, final int value) throws IOException {
        writeElement(name, Integer.toString(value));
    }

    /**
     * Write a start element, long and end element to the output stream.
     *
     * @param name the element name
     * @param value the long to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void writeElement(final String name, final long value) throws IOException {
        writeElement(name, Long.toString(value));
    }

    /**
     * Write a start element, float and end element to the output stream.
     *
     * @param name the element name
     * @param value the float to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void writeElement(final String name, final float value) throws IOException {
        writeElement(name, Float.toString(value));
    }

    /**
     * Write a start element, double and end element to the output stream.
     *
     * @param name the element name
     * @param value the double to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void writeElement(final String name, final double value) throws IOException {
        writeElement(name, Double.toString(value));
    }

    /**
     * Write a start element, string and end element to the output stream.
     *
     * @param name the element name
     * @param str the string to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void writeElement(final String name, final String str) throws IOException {
        startElement(name);
        if (str != null && !str.isEmpty()) {
            write(str);
        }
        endElement();
    }

    /**
     * Write a string to the output stream.
     *
     * @param buf the string to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void write(final String buf) throws IOException {
        if (buf != null && !buf.isEmpty()) {
            if (state == State.WRITING_ELEMENT) {
                out.write('>');
                state = State.WRITING_TEXT;
            }
            out.write(encode(buf));
        }
    }

    /**
     * Write a string to the output stream without encoding it.
     *
     * @param buf the string to write to the output stream
     * @throws IOException if an input or output error occurs
     */
    public void writeRaw(final String buf) throws IOException {
        if (buf != null && !buf.isEmpty()) {
            if (state == State.WRITING_ELEMENT) {
                out.write('>');
                state = State.WRITING_TEXT;
            }
            out.write(buf);
        }
    }

    /**
     * Write a boolean attribute to the output stream.
     *
     * @param name the attribute name
     * @param value the boolean attribute value
     * @throws IOException if an input or output error occurs
     */
    public void attribute(final String name, final boolean value) throws IOException {
        attribute(name, Boolean.toString(value));
    }

    /**
     * Write an integer attribute to the output stream.
     *
     * @param name the attribute name
     * @param value the integer attribute value
     * @throws IOException if an input or output error occurs
     */
    public void attribute(final String name, final int value) throws IOException {
        attribute(name, Integer.toString(value));
    }

    /**
     * Write a long attribute to the output stream.
     *
     * @param name the attribute name
     * @param value the long attribute value
     * @throws IOException if an input or output error occurs
     */
    public void attribute(final String name, final long value) throws IOException {
        attribute(name, Long.toString(value));
    }

    /**
     * Write a float attribute to the output stream.
     *
     * @param name the attribute name
     * @param value the float attribute value
     * @throws IOException if an input or output error occurs
     */
    public void attribute(final String name, final float value) throws IOException {
        attribute(name, Float.toString(value));
    }

    /**
     * Write a double attribute to the output stream.
     *
     * @param name the attribute name
     * @param value the double attribute value
     * @throws IOException if an input or output error occurs
     */
    public void attribute(final String name, final double value) throws IOException {
        attribute(name, Double.toString(value));
    }

    /**
     * Write a text attribute to the output stream.
     *
     * @param name the attribute name
     * @param value the attribute value
     * @throws IOException if an input or output error occurs
     */
    public void attribute(final String name, final String value) throws IOException {
        if (state != State.WRITING_ELEMENT) {
            throw new IllegalStateException("Not writing element");
        }
        if (value != null && !value.isEmpty()) {
            out.write(' ');
            out.write(name);
            out.write(doubleQuote ? "=\"" : "='");
            out.write(encode(value));
            out.write(doubleQuote ? '"' : '\'');
        }
    }

    /**
     * Write a processing instruction to the output stream.
     *
     * @param target the processing instruction target
     * @param data the processing instruction data
     * @throws IOException if an input or output error occurs
     */
    public void processingInstruction(final String target, final String data) throws IOException {
        switch (state) {
        case WRITING_ELEMENT:
            out.write("><?");
            out.write(target);
            break;
        case WRITING_TEXT:
            out.write("<?");
            out.write(target);
            break;
        default:
            break;
        }
        if (data != null && !data.isEmpty()) {
            out.write(' ');
            out.write(data);
        }
        out.write("?>");
        state = State.WRITING_TEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        out.flush();
        out.close();
    }

    /**
     * Encode text.
     *
     * @param text the text to encode
     * @return the encoded text
     */
    private String encode(final String text) {
        StringBuilder buf = null;
        int len = text.length();
        char[] arr = text.toCharArray();
        for (int i = 0; i < len; ++i) {
            char c = arr[i];
            switch (c) {
            case '&':
                if (buf == null) {
                    buf = new StringBuilder();
                    buf.append(arr, 0, i);
                }
                buf.append("&amp;");
                break;
            case '<':
                if (buf == null) {
                    buf = new StringBuilder();
                    buf.append(arr, 0, i);
                }
                buf.append("&lt;");
                break;
            case '>':
                if (buf == null) {
                    buf = new StringBuilder();
                    buf.append(arr, 0, i);
                }
                buf.append("&gt;");
                break;
            case '\'':
                if (!doubleQuote) {
                    if (buf == null) {
                        buf = new StringBuilder();
                        buf.append(arr, 0, i);
                    }
                    buf.append("&apos;");
                } else if (buf != null) {
                    buf.append(c);
                }
                break;
            case '"':
                if (doubleQuote) {
                    if (buf == null) {
                        buf = new StringBuilder();
                        buf.append(arr, 0, i);
                    }
                    buf.append("&quot;");
                } else if (buf != null) {
                    buf.append(c);
                }
                break;
            default:
                if (c > TILDE_CHARACTER || (c < SPACE_CHARACTER && c != TAB_CHARACTER && c != LF_CHARACTER && c != CR_CHARACTER)) {
                    if (buf == null) {
                        buf = new StringBuilder();
                        buf.append(arr, 0, i);
                    }
                    buf.append("&#x");
                    buf.append(Integer.toHexString(c));
                    buf.append(';');
                } else if (buf != null) {
                    buf.append(c);
                }
            }
        }
        return buf != null ? buf.toString() : text;
    }
}
