/*
 * MaulssLib
 * 
 * Created on 04 January 2015 at 1:35 AM.
 */

package com.maulss.enhancedlib.util;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public final class ImageUtil {

	/* Disable initialization */
	private ImageUtil() {}

	private static final FilenameFilter IMAGE_FILTER =
			(dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".bmp");

	public BufferedImage[] loadFromDirectory(File directory) {
		File[] files = directory.listFiles(IMAGE_FILTER);
		BufferedImage[] images = new BufferedImage[files.length];

		if (!directory.isDirectory()) {
			return images;
		}

		for (int i = 0; i < files.length; i++) {
			try {
				BufferedImage image = ImageIO.read(files[i]);
				if (image != null) images[i] = image;
			} catch (IOException ignored) {
				// Invalid image file
			}
		}

		return images;
	}

	public static BufferedImage[] getFrames(File gif) throws IOException {
		ImageReader ir = new GIFImageReader(new GIFImageReaderSpi());
		ir.setInput(ImageIO.createImageInputStream(gif));

		BufferedImage[] frames = new BufferedImage[ir.getNumImages(true)];
		for (int i = 0; i < ir.getNumImages(true); ++i) {
			frames[i] = ir.read(i);
		}

		return frames;
	}
}