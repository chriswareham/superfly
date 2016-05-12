/*
 * @(#) AbstractTopLevelFrame.java
 *
 * Copyright (C) 2016, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import net.chriswareham.di.ComponentFactory;

/**
 * This class provides an application top level frame.
 *
 * @author Chris Wareham
 */
public abstract class AbstractTopLevelFrame extends AbstractFrame {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map of currently open windows.
     */
    private final Map<Class<? extends AbstractFrame>, AbstractFrame> windows = new HashMap<>();

    /**
     * Construct an instance of the application top level frame.
     *
     * @param componentFactory the component factory
     */
    public AbstractTopLevelFrame(final ComponentFactory componentFactory) {
        super(null, componentFactory);
    }

    /**
     * Open a window.
     *
     * @param <T> the type of the window to open
     * @param type the class of the window to open
     */
    private <T extends AbstractFrame> void openWindow(final Class<T> type) {
        AbstractFrame window = windows.get(type);
        if (window != null) {
            window.toFront();
        } else {
            try {
                Constructor<T> constructor = type.getConstructor(AbstractFrame.class, ComponentFactory.class);
                window = constructor.newInstance(this, getComponentFactory());
                window.addWindowListener(new ClosedWindowListener(type));
                window.setVisible(true);
                windows.put(type, window);
            } catch (ReflectiveOperationException | IllegalArgumentException exception) {
                ErrorDialog.showDialog(this, "Error", exception);
            }
        }
    }

    /**
     * Called when a window has been closed.
     *
     * @param <T> the type of the window that has been closed
     * @param type the class of the window that has been closed
     */
    private <T extends AbstractFrame> void closedWindow(final Class<T> type) {
        windows.remove(type);
    }

    /**
     * Called when the user selects a menu item to open a window.
     */
    protected class OpenWindowListener implements ActionListener {
        /**
         * The class of the window to open.
         */
        private final Class<? extends AbstractFrame> type;

        /**
         * Constructs a new instance of the open window action listener.
         *
         * @param t the class of the window to open
         */
        public OpenWindowListener(final Class<? extends AbstractFrame> t) {
            type = t;
        }

        /**
         * Called when the user selects a menu item to open a window.
         *
         * @param event describes the action event
         */
        @Override
        public void actionPerformed(final ActionEvent event) {
            openWindow(type);
        }
    }

    /**
     * Called when the user has closed a window.
     */
    protected class ClosedWindowListener extends WindowAdapter {
        /**
         * The class of the window that has been closed.
         */
        private final Class<? extends AbstractFrame> type;

        /**
         * Constructs a new instance of the close window action listener.
         *
         * @param t the class of the window that has been closed
         */
        public ClosedWindowListener(final Class<? extends AbstractFrame> t) {
            type = t;
        }

        /**
         * Called when the user has closed a window.
         *
         * @param event describes the window event
         */
        @Override
        public void windowClosed(final WindowEvent event) {
            closedWindow(type);
        }
    }
}
