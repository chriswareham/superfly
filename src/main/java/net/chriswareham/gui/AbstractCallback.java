/*
 * @(#) AbstractCallback.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.Cursor;

import net.chriswareham.di.ComponentException;

/**
 * This class provides a wrapper that handles errors for a callback.
 *
 * @author Chris Wareham
 */
public abstract class AbstractCallback {
    /**
     * The window the callback is for.
     */
    private final AbstractFrame window;

    /**
     * Construct a wrapper that handles errors for a callback.
     *
     * @param w the window the callback is for
     */
    public AbstractCallback(final AbstractFrame w) {
        this.window = w;
    }

    /**
     * The callback to handle errors for.
     *
     * @throws ComponentException if an error occurs
     */
    public abstract void callback() throws ComponentException;

    /**
     * Call and handles errors for a callback.
     */
    public void call() {
        try {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            callback();

            window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (ComponentException exception) {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            ErrorDialog.showDialog(window, "Error", exception);
        }
    }
}
