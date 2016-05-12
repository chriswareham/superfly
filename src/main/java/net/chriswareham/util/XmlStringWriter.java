/*
 * @(#) XmlStringWriter.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Convenience class for writing an XML string. Instances of this class are not
 * thread safe and should not be reused.
 *
 * @author Chris Wareham
 */
public class XmlStringWriter extends XmlWriter {
    /**
     * The stream to write to.
     */
    private final ByteArrayOutputStream outputStream;

    /**
     * Constructs a writer with the default UTF-8 output encoding.
     */
    public XmlStringWriter() {
        this(false, DEFAULT_ENCODING);
    }

    /**
     * Constructs a writer with the specified output encoding.
     *
     * @param dq whether to double quote attributes rather than single quote them
     */
    public XmlStringWriter(final boolean dq) {
        this(dq, DEFAULT_ENCODING);
    }

    /**
     * Constructs a writer with the specified output encoding.
     *
     * @param e the encoding to use
     */
    public XmlStringWriter(final String e) {
        this(false, e);
    }

    /**
     * Constructs a writer with the specified output encoding.
     *
     * @param dq whether to double quote attributes rather than single quote them
     * @param e the encoding to use
     */
    public XmlStringWriter(final boolean dq, final String e) {
        this(new ByteArrayOutputStream(), dq, e);
    }

    /**
     * Constructs a writer with the specified output encoding.
     *
     * @param os the stream to write to
     * @param dq whether to double quote attributes rather than single quote them
     * @param e the encoding to use
     */
    private XmlStringWriter(final ByteArrayOutputStream os, final boolean dq, final String e) {
        super(os, dq, e);
        this.outputStream = os;
    }

    /**
     * Get the XML written to the writer.
     *
     * @return the XML written to the writer
     * @throws IOException if an input or output error occurs
     */
    public String toXml() throws IOException {
        return outputStream.toString(getEncoding());
    }
}
