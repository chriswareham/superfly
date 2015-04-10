/*
 * @(#) MultiPartUploader.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class provides an uploader for multi-part HTTP requests.
 *
 * @author Chris Wareham
 */
public class MultiPartUploader implements Closeable {
    /**
     * The boundary.
     */
    private final String boundary;
    /**
     * The connection.
     */
    private HttpURLConnection connection;
    /**
     * The output stream.
     */
    private DataOutputStream outputStream;

    /**
     * Create an instance of the uploader for multi-part HTTP requests.
     *
     * @param b the boundary
     */
    public MultiPartUploader(final String b) {
        boundary = b;
    }

    /**
     * Open the connection.
     *
     * @param u the URL
     * @throws IOException if an error occurs
     */
    public void open(final String u) throws IOException {
        if (connection != null) {
            throw new IllegalStateException("Already connected");
        }

        connection = (HttpURLConnection) new URL(u).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        connection.connect();

        outputStream = new DataOutputStream(connection.getOutputStream());
    }

    /**
     * Commit the request.
     *
     * @throws IOException if an error occurs
     */
    public void commit() throws IOException {
        if (outputStream == null) {
            throw new IllegalStateException("Not connected");
        }

        outputStream.writeBytes("--");
        outputStream.writeBytes(boundary);
        outputStream.writeBytes("--\r\n");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Upload server response " + connection.getResponseMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException exception) {
                outputStream = null;
            }
        }
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    /**
     * Write a form field.
     *
     * @param name the field name
     * @param value the field value
     * @throws IOException if an error occurs
     */
    public void writeFormField(final String name, final String value) throws IOException {
        if (outputStream == null) {
            throw new IllegalStateException("Not connected");
        }

        outputStream.writeBytes("--");
        outputStream.writeBytes(boundary);
        outputStream.writeBytes("\r\nContent-Disposition: form-data; name=\"");
        outputStream.writeBytes(name);
        outputStream.writeBytes("\"\r\n\r\n");
        outputStream.writeBytes(value);
        outputStream.writeBytes("\r\n");
    }

    /**
     * Write a file field.
     *
     * @param name the field name
     * @param value the field value (the filename)
     * @param type the file type
     * @param data the file data
     * @throws IOException if an error occurs
     */
    public void writeFileField(final String name, final String value, final String type, final byte[] data) throws IOException {
        if (outputStream == null) {
            throw new IllegalStateException("Not connected");
        }

        outputStream.writeBytes("--");
        outputStream.writeBytes(boundary);
        outputStream.writeBytes("\r\n");

        outputStream.writeBytes("Content-Disposition: form-data; name=\"");
        outputStream.writeBytes(name);
        outputStream.writeBytes("\";filename=\"");
        outputStream.writeBytes(value);
        outputStream.writeBytes("\"\r\n");

        outputStream.writeBytes("Content-Type: ");
        outputStream.writeBytes(type);
        outputStream.writeBytes("\r\n\r\n");
        outputStream.write(data, 0, data.length);
        outputStream.writeBytes("\r\n");
    }
}
