/*
 * @(#) ImagePreviewPanel.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class provides a panel for displaying an image preview.
 *
 * @author Chris Wareham
 */
public class ImagePreviewPanel extends JPanel implements PropertyChangeListener {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The image preview dimension.
     */
    private static final int DIMENSION = 200;
    /**
     * The valid image extensions.
     */
    private static final Set<String> EXTENSIONS;

    static {
        Set<String> extensions = new HashSet<>();
        extensions.add("gif");
        extensions.add("jpg");
        extensions.add("png");
        EXTENSIONS = Collections.unmodifiableSet(extensions);
    }

    /**
     * The image label.
     */
    private final JLabel label;

    /**
     * Construct an instance of the panel for displaying an image preview.
     */
    public ImagePreviewPanel() {
        setLayout(new BorderLayout(5, 5));

        add(new JLabel("Preview:"), BorderLayout.NORTH);

        label = new JLabel();
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(DIMENSION + 5, -1));
        label.setBorder(BorderFactory.createEtchedBorder());
        add(label, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            ImageIcon icon = null;

            File file = (File) evt.getNewValue();

            if (file != null && validFile(file)) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        float width = img.getWidth();
                        float height = img.getHeight();
                        float scale = height / width;

                        int w = Math.max(1, DIMENSION);
                        int h = Math.max(1, (int) (DIMENSION * scale));

                        icon = new ImageIcon(img.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH));
                    }
                } catch (IOException exception) {
                    icon = null;
                }
            }

            label.setIcon(icon);
            repaint();
        }
    }

    /**
     * Check a file has a valid image extension.
     *
     * @param file the file to check
     * @return whether the file has a valid image extension
     */
    private static boolean validFile(final File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i < 1) {
            // either no extension or nothing before the period
            return false;
        }
        return EXTENSIONS.contains(name.substring(i + 1));
    }
}
