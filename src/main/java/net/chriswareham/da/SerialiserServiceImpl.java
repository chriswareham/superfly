/*
 * @(#) SerialiserServiceImpl.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.da;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidParameterException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * This class implements methods that serialise and deserialise objects to and
 * from strings.
 *
 * @author Chris Wareham
 */
public class SerialiserServiceImpl implements SerialiserService {
    /**
     * The default algorithm.
     */
    private static final String DEFAULT_ALGORITHM = "Blowfish";
    /**
     * The default key size.
     */
    private static final int DEFAULT_KEY_SIZE = 128;
    /**
     * The character set of strings representing serialised objects.
     */
    private static final Charset CHARSET = Charset.forName("US-ASCII");

    /**
     * The algorithm.
     */
    private String algorithm = DEFAULT_ALGORITHM;
    /**
     * The key size.
     */
    private int keySize = DEFAULT_KEY_SIZE;
    /**
     * The key for encrypting and decrypting serialised objects.
     */
    private Key key;

    /**
     * Set the algorithm.
     *
     * @param a the algorithm
     */
    public void setAlgorithm(final String a) {
        algorithm = a;
    }

    /**
     * Set the key size.
     *
     * @param ks the key size
     */
    public void setKeySize(final int ks) {
        keySize = ks;
    }

    /**
     * Set the key to use for encrypting and decrypting.
     *
     * @param k the key to use for encrypting and decrypting
     * @throws SerialiserServiceException if an error occurs
     */
    public void setKey(final String k) throws SerialiserServiceException {
        try {
            key = new SecretKeySpec(Base64.decodeBase64(k), algorithm);
        } catch (IllegalArgumentException exception) {
            throw new SerialiserServiceException("Failed to set key", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateKey() throws SerialiserServiceException {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(algorithm);
            generator.init(keySize);

            Key generatedKey = generator.generateKey();

            byte[] encodedKey = generatedKey.getEncoded();

            if (encodedKey == null) {
                throw new InvalidParameterException("Algorithm does not support encoding");
            }

            return new String(Base64.encodeBase64(encodedKey), CHARSET);
        } catch (GeneralSecurityException | InvalidParameterException exception) {
            throw new SerialiserServiceException("Unable to generate key", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String serialise(final Serializable obj) throws SerialiserServiceException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            ByteArrayOutputStream sink = new ByteArrayOutputStream();

            try (ObjectOutputStream out = new ObjectOutputStream(new CipherOutputStream(sink, cipher))) {
                out.writeObject(obj);
            }

            byte[] bytes = sink.toByteArray();

            return new String(Base64.encodeBase64(bytes), CHARSET);
        } catch (GeneralSecurityException | IOException exception) {
            throw new SerialiserServiceException("Unable to serialise object", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T deserialise(final String str, final Class<T> type) throws SerialiserServiceException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] bytes = str.getBytes(CHARSET);

            ByteArrayInputStream source = new ByteArrayInputStream(Base64.decodeBase64(bytes));

            try (ObjectInputStream in = new ObjectInputStream(new CipherInputStream(source, cipher))) {
                Object obj = in.readObject();
                return type.cast(obj);
            }
        } catch (GeneralSecurityException | IOException | ClassNotFoundException | ClassCastException exception) {
            throw new SerialiserServiceException("Unable to deserialise object", exception);
        }
    }
}
