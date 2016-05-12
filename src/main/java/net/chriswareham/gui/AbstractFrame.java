/*
 * @(#) AbstractFrame.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import net.chriswareham.di.ComponentException;
import net.chriswareham.di.ComponentFactory;

/**
 * This class provides an application sub-frame.
 *
 * @author Chris Wareham
 */
public abstract class AbstractFrame extends JFrame {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The component factory.
     */
    private final ComponentFactory componentFactory;

    /**
     * Construct a new instance of the application sub-frame.
     *
     * @param parent the parent window
     * @param cf the component factory
     */
    public AbstractFrame(final AbstractFrame parent, final ComponentFactory cf) {
        this.componentFactory = cf;
        doCreateInterface();
        doPopulateInterface(parent);
    }

    /**
     * Get the component factory.
     *
     * @return the component factory
     */
    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    /**
     * Close the cache tool.
     */
    protected void close() {
        setVisible(false);
        dispose();
    }

    /**
     * Create the interface.
     */
    protected abstract void createInterface();

    /**
     * Populate the interface.
     *
     * @throws ComponentException if an error occurs
     */
    protected abstract void populateInterface() throws ComponentException;

    /**
     * Create the interface.
     */
    private void doCreateInterface() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                close();
            }
        });

        createInterface();
    }

    /**
     * Populate the interface.
     *
     * @param parent the parent window
     */
    private void doPopulateInterface(final AbstractFrame parent) {
        setLocationRelativeTo(parent);

        new AbstractCallback(this) {
            @Override
            public void callback() throws ComponentException {
                populateInterface();
            }
        }.call();
    }
}
