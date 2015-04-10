/*
 * @(#) Objects.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Utility methods for objects.
 *
 * @author Chris Wareham
 */
public final class Objects {
    /**
     * Utility class - no public constructor.
     */
    private Objects() {
        // empty
    }

    /**
     * Get a copy of an object.
     *
     * @param <T> the type of object to copy
     * @param obj the object to copy
     * @return a copy of the object
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T copy(final T obj) {
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(buf)) {
                outputStream.writeObject(obj);
            }

            return (T) new ObjectInputStream(buf.getInputStream()).readObject();
        } catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException("Failed to copy object", exception);
        }
    }

    /**
     * Implementation of an output stream backed by a byte array. For internal
     * use this is more efficient than the JDK implementation.
     */
    private static class ByteArrayOutputStream extends OutputStream {
        /**
         * The byte array.
         */
        private byte[] buf;
        /**
         * The count of bytes used in the byte array.
         */
        private int count;

        /**
         * Construct an instance of the output stream.
         */
        ByteArrayOutputStream() {
            buf = new byte[5 * 1024];
        }

        /**
         * Get an input stream backed by the byte array.
         *
         * @return an input stream backed by the byte array
         */
        public InputStream getInputStream() {
            return new ByteArrayInputStream(buf, count);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final int b) {
            capacity(count + 1);
            buf[count++] = (byte) b;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final byte[] b) {
            capacity(count + b.length);
            System.arraycopy(b, 0, buf, count, b.length);
            count += b.length;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final byte[] b, final int off, final int len) {
            capacity(count + len);
            System.arraycopy(b, off, buf, count, len);
            count += len;
        }

        /**
         * Ensure the capacity of the byte array.
         *
         * @param n the required capacity of the byte array
         */
        private void capacity(final int n) {
            if (n > buf.length) {
                byte[] dst = new byte[Math.max(n, 2 * buf.length)];
                System.arraycopy(buf, 0, dst, 0, buf.length);
                buf = dst;
            }
        }
    }

    /**
     * Implementation of an input stream backed by a byte array. For internal
     * use this is more efficient than the JDK implementation.
     */
    private static class ByteArrayInputStream extends InputStream {
        /**
         * The byte array.
         */
        private final byte[] buf;
        /**
         * The count of bytes used in the byte array.
         */
        private final int count;
        /**
         * The current read position in the byte array.
         */
        private int pos;

        /**
         * Construct an instance of the input stream.
         *
         * @param b the byte array to back the input stream
         * @param c the count of bytes used in the byte array
         */
        ByteArrayInputStream(final byte[] b, final int c) {
            buf = b;
            count = c;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read() {
            return pos < count ? buf[pos++] & 0xff : -1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read(final byte[] b) {
            return read(b, 0, b.length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read(final byte[] b, final int off, final int len) {
            int n = -1;
            if (pos < count) {
                n = count < pos + len ? count - pos : len;
                System.arraycopy(buf, pos, b, off, n);
                pos += n;
            }
            return n;
        }
    }
}
