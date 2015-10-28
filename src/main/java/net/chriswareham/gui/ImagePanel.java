/*
 * @(#) ImageComponent.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import net.chriswareham.util.Images;

/**
 * This class provides a panel for resizing an image.
 *
 * @author Chris Wareham
 */
public class ImagePanel extends JPanel {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The dashes for a dashed outline.
     */
    private static final float[] DASHES = {5.0f};
    /**
     * The dashed outline for selections.
     */
    private static final BasicStroke DASHED_LINE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, DASHES, 0.0f);
    /**
     * The solid outline for selections.
     */
    private static final BasicStroke SOLID_LINE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

    /**
     * The scale slider.
     */
    private JSlider scaleSlider;
    /**
     * The resize button.
     */
    private JButton resizeButton;
    /**
     * The image component.
     */
    private ImageComponent imageComponent;

    /**
     * Construct an instance of the panel for resizing an image.
     *
     * @param d the dimension to resize the image to
     */
    public ImagePanel(final Dimension d) {
        super(new GridBagLayout());
        createInterface(d);
    }

    /**
     * Create the interface.
     *
     * @param d the dimension to resize the image to
     */
    private void createInterface(final Dimension d) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Scale:"), c);

        c.weightx = 1.0;
        c.gridx++;
        scaleSlider = new JSlider(1, 100, 100);
        scaleSlider.addChangeListener((final ChangeEvent event) -> {
                imageComponent.setScale(scaleSlider.getValue() / 100.0);
            });
        scaleSlider.setEnabled(false);
        add(scaleSlider, c);

        c.weightx = 0.0;
        c.gridx++;
        resizeButton = new JButton("Resize");
        resizeButton.addActionListener((final ActionEvent event) -> {
                imageComponent.cropImage();
                scaleSlider.setValue(100);
            });
        resizeButton.setEnabled(false);
        add(resizeButton, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 3;
        imageComponent = new ImageComponent(resizeButton, d);
        JScrollPane scrollPane = new JScrollPane(imageComponent);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, c);
    }

    /**
     * Get the image.
     *
     * @return the image
     */
    public BufferedImage getImage() {
        return imageComponent.getImage();
    }

    /**
     * Set the image.
     *
     * @param i the image
     */
    public void setImage(final BufferedImage i) {
        imageComponent.setImage(i);
        scaleSlider.setEnabled(true);
    }

    /**
     * This class provides a component for resizing an image.
     */
    private static class ImageComponent extends JComponent {
        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The component that triggers crop actions.
         */
        private final JComponent cropComponent;
        /**
         * The resize template.
         */
        private Rectangle crop;
        /**
         * The image.
         */
        private BufferedImage image;
        /**
         * The scaled image.
         */
        private BufferedImage scaledImage;
        /**
         * The scale.
         */
        private double scale = 1.0;
        /**
         * The drag origin.
         */
        private Point drag;

        /**
         * Constructs an instance of the image display component.
         *
         * @param cc the component that triggers crop actions
         * @param c the dimension to resize the image to
         */
        ImageComponent(final JComponent cc, final Dimension c) {
            cropComponent = cc;

            int x = Math.max(0, (getWidth() - c.width) / 2);
            int y = Math.max(0, (getHeight() - c.height) / 2);

            crop = new Rectangle(x, y, c.width, c.height);

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(final ComponentEvent event) {
                    // XXX
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent event) {
                    if (crop.contains(event.getX(), event.getY())) {
                        drag = event.getPoint();
                    }
                }

                @Override
                public void mouseReleased(final MouseEvent event) {
                    if (drag != null) {
                        drag = null;
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(final MouseEvent event) {
                    if (drag != null) {
                        int maxX = getWidth() - 1;
                        int maxY = getHeight() - 1;

                        crop.x = Math.max(0, crop.x + event.getX() - drag.x);
                        if (crop.x + crop.width > maxX) {
                            crop.x = maxX - crop.width;
                        }

                        crop.y = Math.max(0, crop.y + event.getY() - drag.y);
                        if (crop.y + crop.height > maxY) {
                            crop.y = maxY - crop.height;
                        }

                        drag = event.getPoint();

                        repaint();
                    }
                }
            });
        }

        /**
         * Get the image.
         *
         * @return the image
         */
        public BufferedImage getImage() {
            return scaledImage != null ? scaledImage : image;
        }

        /**
         * Set the image.
         *
         * @param i the image
         */
        public void setImage(final BufferedImage i) {
            image = i;
            scaledImage = null;

            revalidate();
            repaint();
        }

        /**
         * Set the scale.
         *
         * @param s the scale
         */
        public void setScale(final double s) {
            if (scale != s) {
                scale = s;
                scaledImage = null;

                repaint();
            }
        }

        /**
         * Crop the image.
         */
        public void cropImage() {
            if (image != null) {
                BufferedImage img = scaledImage != null ? scaledImage : image;

                int w = getWidth();
                int h = getHeight();

                Rectangle r = new Rectangle();
                r.width = img.getWidth();
                r.height = img.getHeight();
                r.x = (w - r.width) / 2;
                r.y = (h - r.height) / 2;

                Rectangle intersect = crop.intersection(r);

                if (!intersect.isEmpty()) {
                    image = Images.cropImage(img, Math.max(0, crop.x - r.x), Math.max(0, crop.y - r.y), intersect.width, intersect.height);
                    crop.x = (w - crop.width) / 2;
                    crop.y = (h - crop.height) / 2;
                    scaledImage = null;
                    scale = 1.0;
                    repaint();
                }
            }
        }

        /**
         * Get the minimum size of the component.
         *
         * @return the minimum size of the component
         */
        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        /**
         * Get the preferred size of the component.
         *
         * @return the preferred size of the component
         */
        @Override
        public Dimension getPreferredSize() {
            int w = 0;
            int h = 0;

            if (image != null) {
                w = image.getWidth();
                h = image.getHeight();
            }

            w = Math.max(w, crop.width + crop.x + 1);
            h = Math.max(h, crop.height + crop.y + 1);

            return new Dimension(w, h);
        }

        /**
         * Paint the component.
         *
         * @param g the graphics context
         */
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            Rectangle clip = g2.getClipBounds();

            if (image != null) {
                BufferedImage img = image;
                if (scale < 1.0) {
                    if (scaledImage == null) {
                        int w = Math.max(1, (int) (image.getWidth() * scale));
                        int h = Math.max(1, (int) (image.getHeight() * scale));
                        scaledImage = Images.resizeImage(image, w, h);
                    }
                    img = scaledImage;
                }

                Rectangle r = new Rectangle();
                r.width = img.getWidth();
                r.height = img.getHeight();
                r.x = (getWidth() - r.width) / 2;
                r.y = (getHeight() - r.height) / 2;

                Rectangle intersect = r.intersection(clip);

                if (!intersect.isEmpty()) {
                    int sourceX = intersect.x - r.x;
                    int sourceY = intersect.y - r.y;

                    g2.drawImage(img, intersect.x, intersect.y, intersect.x + intersect.width, intersect.y + intersect.height,
                        sourceX, sourceY, sourceX + intersect.width, sourceY + intersect.height, null);
                }

                cropComponent.setEnabled(r.contains(crop));
            }

            if (crop.intersects(clip)) {
                g2.setColor(Color.BLACK);
                g2.setStroke(SOLID_LINE);
                g2.drawRect(crop.x, crop.y, crop.width, crop.height);

                g2.setColor(Color.WHITE);
                g2.setStroke(DASHED_LINE);
                g2.drawRect(crop.x, crop.y, crop.width, crop.height);
            }
        }
    }
}
