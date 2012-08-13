/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2011-2012  Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */

package de.uni_siegen.wineme.come_in.thumbnailer.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import de.uni_siegen.wineme.come_in.thumbnailer.UnsupportedInputFileFormatException;

/**
 * Resize an image.
 * 
 * @author Benjamin
 */
public class ResizeImage {

	/** The logger for this class */
	private static final Logger mLog = Logger.getLogger(ResizeImage.class);

	
	BufferedImage inputImage;
	private boolean isProcessed = false;
	BufferedImage outputImage;
	
	private int imageWidth;
	private int imageHeight;
	private int thumbWidth;
	private int thumbHeight;
	private double resizeRatio = 1.0;
	
	/**
	 * Scale input image so that width and height is equal (or smaller) to the output size. 
	 * The other dimension will be smaller or equal than the output size.
	 */
	public static final int RESIZE_FIT_BOTH_DIMENSIONS = 2;
	
	/**
	 * Scale input image so that width or height is equal to the output size. 
	 * The other dimension will be bigger or equal than the output size.
	 */
	public static final int RESIZE_FIT_ONE_DIMENSION = 3;
	
	/**
	 * Do not resize the image. Instead, crop the image (if smaller) or center it (if bigger)
	 */
	public static final int NO_RESIZE_ONLY_CROP = 4;
	
	/**
	 * Do not try to scale the image up, only down. If bigger, center it.
	 */
	public static final int DO_NOT_SCALE_UP = 16;

	/**
	 * If output image is bigger than input image, allow the output to be smaller than expected (the size of the input image)
	 */
	public static final int ALLOW_SMALLER = 32;
	
	public int resizeMethod = RESIZE_FIT_ONE_DIMENSION;
	public int extraOptions = DO_NOT_SCALE_UP;
	
	private int scaledWidth;
	private int scaledHeight;
	private int offsetX;
	private int offsetY;


	
	public ResizeImage(int thumbWidth, int thumbHeight)
	{
		this.thumbWidth = thumbWidth;
		this.thumbHeight = thumbHeight;
	}
	
	/**
	 * Set the input image
	 * @param input	File that should be resized
	 * @return A reference to this (to allow chaining of function calls)
	 * @throws IOException	If file cannot be read
	 */
	public ResizeImage setInputImage(File input) throws IOException
	{
		BufferedImage image = ImageIO.read(input);
		setInputImage(image);
		
		return this;
	}
	
	/**
	 * Set the input image
	 * @param input	File that should be resized
	 * @return A reference to this (to allow chaining of function calls)
	 * @throws IOException	If file cannot be read
	 */
	public ResizeImage setInputImage(InputStream input) throws IOException
	{
		BufferedImage image = ImageIO.read(input);
		setInputImage(image);
		
		return this;
	}
	
	/**
	 * Set the input image
	 * @param input	Image that should be resized
	 * @return A reference to this (to allow chaining of function calls)
	 * @throws IOException	If file cannot be read
	 */
	public ResizeImage setInputImage(BufferedImage input) throws UnsupportedInputFileFormatException
	{
		if (input == null)
			throw new UnsupportedInputFileFormatException("The image reader could not open the file.");
		
		this.inputImage = input;
		isProcessed = false;
		imageWidth    = inputImage.getWidth(null);
		imageHeight   = inputImage.getHeight(null);
		
		return this;
	}
	
	public void writeOutput(File output) throws IOException
	{
		writeOutput(output, "PNG");				
	}

	public void writeOutput(File output, String format) throws IOException
	{
		if (!isProcessed)
			process();
		
		ImageIO.write(outputImage, format, output);				
	}
	
	private void process()
	{		
		if (imageWidth == thumbWidth && imageHeight == thumbHeight)
			outputImage = inputImage;
		else
		{
			calcDimensions(resizeMethod);
			paint();
		}
		
		isProcessed = true;	
	}
	
	private void calcDimensions(int resizeMethod)
	{
		switch (resizeMethod)
		{
			case RESIZE_FIT_BOTH_DIMENSIONS:
				resizeRatio = Math.min(((double) thumbWidth) / imageWidth, ((double) thumbHeight) / imageHeight);
				break;
				
			case RESIZE_FIT_ONE_DIMENSION:
				resizeRatio = Math.max(((double) thumbWidth) / imageWidth, ((double) thumbHeight) / imageHeight);
				break;

			case NO_RESIZE_ONLY_CROP:
				resizeRatio = 1.0;
				break;
		}
		if ((extraOptions & DO_NOT_SCALE_UP) > 0)
			if (resizeRatio > 1.0)
				resizeRatio = 1.0;


		scaledWidth = (int) Math.round(imageWidth * resizeRatio);
		scaledHeight = (int) Math.round(imageHeight * resizeRatio);

		if ((extraOptions & ALLOW_SMALLER) > 0) {
			if (scaledWidth < thumbWidth && scaledHeight < thumbHeight)
			{
				thumbWidth = scaledWidth;
				thumbHeight = scaledHeight;
			}
		}
		
		// Center if smaller.
		if (scaledWidth  < thumbWidth)
			offsetX = (thumbWidth  - scaledWidth)  / 2;
		else
			offsetX = 0;

		if (scaledHeight < thumbHeight)
			offsetY = (thumbHeight - scaledHeight) / 2;
		else
			offsetY = 0;
	}
	
	private void paint()
	{
		outputImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = outputImage.createGraphics();
		
		// Fill background with white color
		graphics2D.setBackground(Color.WHITE);
		graphics2D.setPaint(Color.WHITE); 
		graphics2D.fillRect(0, 0, thumbWidth, thumbHeight);

		// Enable smooth, high-quality resampling
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		ThumbnailReadyObserver observer = new ThumbnailReadyObserver(Thread.currentThread());
		boolean scalingComplete = graphics2D.drawImage(inputImage, offsetX, offsetY, scaledWidth, scaledHeight, observer);
		
		if (!scalingComplete && observer != null)
		{
			// ImageObserver must wait for ready
			if (mLog.isDebugEnabled()) 
				throw new RuntimeException("Scaling is not yet complete!");
			else
			{
				mLog.warn("ResizeImage: Scaling is not yet complete!");
			
				while(!observer.ready)
				{
					System.err.println("Waiting .4 sec...");
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
					}
				}
			}
		}
		
		graphics2D.dispose();
	}
}
