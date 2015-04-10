/*
 * @(#) SearchDialogListener.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.util.EventListener;

/**
 * This interface is implemented by listeners that want to be notified when a
 * table search should be performed.
 *
 * @author Chris Wareham
 */
public interface SearchDialogListener extends EventListener {
    /**
     * Notify the listener that a table search should be be performed.
     *
     * @param event describes the table search to be performed
     */
    void searchPerformed(SearchDialogEvent event);
}
