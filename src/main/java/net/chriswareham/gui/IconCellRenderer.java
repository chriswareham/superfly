/*
 * @(#) IconCellRenderer.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class provides a renderer for table cells containing icons.
 *
 * @author Chris Wareham
 */
public class IconCellRenderer extends DefaultTableCellRenderer {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Set the icon for the cell.
     *
     * @param obj the icon for the cell
     */
    @Override
    public void setValue(final Object obj) {
        ImageIcon icon = (ImageIcon) obj;
        setIcon(icon);
    }
}
