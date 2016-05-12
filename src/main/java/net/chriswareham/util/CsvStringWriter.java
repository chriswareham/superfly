/*
 * @(#) CsvStringWriter.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Convenience class for writing a CSV string. Instances of this class are not
 * thread safe and should not be reused.
 *
 * @author Chris Wareham
 */
public class CsvStringWriter extends CsvWriter {
    /**
     * The stream to write to.
     */
    private final ByteArrayOutputStream outputStream;

    /**
     * Construct an instance of a writer for CSV files.
     */
    public CsvStringWriter() {
        this(DEFAULT_SEPARATOR_CHARACTER, DEFAULT_QUOTE_CHARACTER, DEFAULT_QUOTE_ESCAPE_CHARACTER, DEFAULT_LINE_END_CHARACTERS);
    }

    /**
     * Construct an instance of a writer for CSV files.
     *
     * @param s the separator character
     * @param q the quote character
     * @param qe the quote escape character
     * @param le the line end characters
     */
    public CsvStringWriter(final char s, final char q, final char qe, final String le) {
        this(new ByteArrayOutputStream(), s, q, qe, le);
    }

    /**
     * Construct an instance of a writer for CSV files.
     *
     * @param os the stream to write to
     * @param s the separator character
     * @param q the quote character
     * @param qe the quote escape character
     * @param le the line end characters
     */
    private CsvStringWriter(final ByteArrayOutputStream os, final char s, final char q, final char qe, final String le) {
        super(os, s, q, qe, le);
        this.outputStream = os;
    }

    /**
     * Get the CSV written to the writer.
     *
     * @return the CSV written to the writer
     * @throws IOException if an input or output error occurs
     */
    public String toCsv() throws IOException {
        return outputStream.toString();
    }
}
