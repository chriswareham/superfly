/*
 * @(#) Images.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Utility methods for images.
 *
 * @author Chris Wareham
 */
public final class Images {
    /**
     * Utility class - no public constructor.
     */
    private Images() {
        // empty
    }

    /**
     * Crop an image.
     *
     * @param image the image to crop
     * @param x the x origin of the region to crop
     * @param y the y origin of the region to crop
     * @param width the width of the region to crop
     * @param height the height of the region to crop
     * @return the cropped image
     */
    public static BufferedImage cropImage(final BufferedImage image, final int x, final int y, final int width, final int height) {
        BufferedImage croppedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = croppedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(image, 0, 0, width, height, x, y, x + width, y + height, null);
        g.dispose();
        return croppedImage;
    }

    /**
     * Resize an image for previewing.
     *
     * @param image the image to resize
     * @param width the width of the resized image
     * @param height the height of the resized image
     * @return the resized image
     */
    public static BufferedImage resizeImagePreview(final BufferedImage image, final int width, final int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    /**
     * Resize an image.
     *
     * @param image the image to resize
     * @param width the width of the resized image
     * @param height the height of the resized image
     * @return the resized image
     */
    public static BufferedImage resizeImage(final BufferedImage image, final int width, final int height) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage resizedImage = image;

        while (w > width || h > height) {
            if (w > width) {
                w /= 2;
                if (w < width) {
                    w = width;
                }
            }

            if (h > height) {
                h /= 2;
                if (h < height) {
                    h = height;
                }
            }

            resizedImage = resizeImage(resizedImage, w, h);
        }

        return resizedImage;
    }
}
