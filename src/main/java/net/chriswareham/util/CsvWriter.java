/*
 * @(#) CsvWriter.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * This class provides a writer for CSV files.
 *
 * @author Chris Wareham
 */
public class CsvWriter implements Closeable, Flushable {
    /**
     * The default separator character.
     */
    public static final char DEFAULT_SEPARATOR_CHARACTER = ',';
    /**
     * The default quote character.
     */
    public static final char DEFAULT_QUOTE_CHARACTER = '"';
    /**
     * The default quote escape character.
     */
    public static final char DEFAULT_QUOTE_ESCAPE_CHARACTER = '"';
    /**
     * The default line end characters.
     */
    public static final String DEFAULT_LINE_END_CHARACTERS = "\n";
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
     * The writer to output the data to.
     */
    private Writer writer;
    /**
     * The separator character.
     */
    private char separator;
    /**
     * The quote character.
     */
    private char quote;
    /**
     * The quote escape character.
     */
    private char quoteEscape;
    /**
     * The line end characters.
     */
    private String lineEnd;

    /**
     * Construct an instance of a writer for CSV files.
     *
     * @param w the underlying writer.
     */
    public CsvWriter(final Writer w) {
        this(w, DEFAULT_SEPARATOR_CHARACTER, DEFAULT_QUOTE_CHARACTER, DEFAULT_QUOTE_ESCAPE_CHARACTER, DEFAULT_LINE_END_CHARACTERS);
    }

    /**
     * Construct an instance of a writer for CSV files.
     *
     * @param w the underlying writer.
     * @param s the separator character
     * @param q the quote character
     * @param qe the quote escape character
     * @param le the line end characters
     */
    public CsvWriter(final Writer w, final char s, final char q, final char qe, final String le) {
        writer = w;
        separator = s;
        quote = q;
        quoteEscape = qe;
        lineEnd = le;
    }

    /**
     * Writes a list of fields formatted as columns in CSV format followed by
     * the line end characters.
     *
     * @param fields the fields to be written
     * @throws IOException if an error occurs
     */
    public void writeLine(final List<String> fields) throws IOException {
        if (fields != null && !fields.isEmpty()) {
            StringBuilder buf = new StringBuilder();

            boolean firstField = true;
            for (String field : fields) {
                if (!firstField) {
                    buf.append(separator);
                } else {
                    firstField = false;
                }

                if (field != null && !field.isEmpty()) {
                    buf.append(quote);
                    int fieldlen = field.length();
                    for (int j = 0; j < fieldlen; ++j) {
                        char c = field.charAt(j);
                        if (c == quote || c == quoteEscape) {
                            buf.append(quoteEscape);
                        }
                        if (c == TAB_CHARACTER || c == LF_CHARACTER || c == CR_CHARACTER) {
                            buf.append(' ');
                        } else {
                            buf.append(c);
                        }
                    }
                    buf.append(quote);
                }
            }

            writer.write(buf.toString());
        }

        writer.write(lineEnd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}
