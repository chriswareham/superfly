/*
 * @(#) CsvReader.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a reader for CSV files.
 *
 * @author Chris Wareham
 */
public class CsvReader implements Closeable {
    /**
     * The default separator character.
     */
    public static final char DEFAULT_SEPARATOR_CHARACTER = ',';
    /**
     * The default quote character.
     */
    public static final char DEFAULT_QUOTE_CHARACTER = '"';

    /**
     * The buffer to read from.
     */
    private BufferedReader reader;
    /**
     * The separator character.
     */
    private char separator;
    /**
     * The quote character.
     */
    private char quote;

    /**
     * Construct an instance of a reader for CSV files that uses the default
     * separator and quote characters.
     *
     * @param r the underlying reader
     */
    public CsvReader(final Reader r) {
        this(r, DEFAULT_SEPARATOR_CHARACTER, DEFAULT_QUOTE_CHARACTER);
    }

    /**
     * Construct an instance of a reader for CSV files that uses the default
     * quote character.
     *
     * @param r the underlying reader
     * @param s the field separator character
     */
    public CsvReader(final Reader r, final char s) {
        this(r, s, DEFAULT_QUOTE_CHARACTER);
    }

    /**
     * Construct an instance of a reader for CSV files.
     *
     * @param r the underlying reader
     * @param s the field separator character
     * @param q the field quote character
     */
    public CsvReader(final Reader r, final char s, final char q) {
        reader = new BufferedReader(r);
        separator = s;
        quote = q;
    }

    /**
     * Skip the next line from the underlying reader.
     *
     * @throws IOException if an error occurs
     */
    public void skipLine() throws IOException {
        reader.readLine();
    }

    /**
     * Parse the next line from the underlying reader.
     *
     * @return the parsed fields, or null if the end of file has been reached
     * @throws IOException if an error occurs
     */
    public List<String> parseLine() throws IOException {
        return parseLine(new ArrayList<>());
    }

    /**
     * Parse the next line from the underlying reader.
     *
     * @param fields the list to store parsed fields in
     * @return the parsed fields, or null if the end of file has been reached
     * @throws IOException if an error occurs
     */
    public List<String> parseLine(final List<String> fields) throws IOException {
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        fields.clear();

        int len = line.length();
        StringBuilder buf = new StringBuilder();

        boolean inQuotes = false;

        for (int i = 0; i < len; ++i) {
            char c = line.charAt(i);

            if (c == quote) {
                if (inQuotes && i + 1 < len && line.charAt(i + 1) == quote) {
                    // a quoted quote
                    buf.append(quote);
                    ++i;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == separator && !inQuotes) {
                fields.add(buf.toString().trim());
                buf.setLength(0);
            } else {
                buf.append(c);
            }
        }

        fields.add(buf.toString().trim());

        return fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
