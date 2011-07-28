/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2011  Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

package de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;

/**
 * This interface is implemented by any method suitable to create a thumbnail of a given File.
 * 
 * @author Benjamin
 *
 */
public interface Thumbnailer extends Closeable {

	/**
	 * Generate a Thumbnail of the input file.
	 * 
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @param mimeType	MIME-Type of input file (null if unknown)
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	public void generateThumbnail(File input, File output, String mimeType) throws IOException, ThumbnailerException;

	/**
	 * Generate a Thumbnail of the input file.
	 * 
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	public void generateThumbnail(File input, File output) throws IOException, ThumbnailerException;
	
	/**
	 * This function will be called after all Thumbnails are generated.
	 * Note: This acts as a Deconstructor. Do not expect this object to work
	 * after calling this method.
	 * 
	 * @throws IOException	If some errors occured during finalising
	 */
	public void close() throws IOException;
	
	
	/**
	 * Set a new Thumbnail size. All following thumbnails will be generated in this size.
	 * 
	 * @param width					Width in Pixel
	 * @param height				Height in Pixel
	 * @param imageResizeOptions	Options for ResizeImage (currently ignored)
	 */
	public void setImageSize(int width, int height, int imageResizeOptions);
	
	/**
	 * Get the currently set Image Width of this Thumbnailer.
	 * @return	image width of created thumbnails.
	 */
	public int getCurrentImageWidth();

	/**
	 * Get the currently set Image Height of this Thumbnailer.
	 * @return	image height of created thumbnails.
	 */
	public int getCurrentImageHeight();
	
	/**
	 * Get a list of all MIME Types that this Thumbnailer is ready to process.
	 * 
	 * @return List of MIME Types. If null, all Files may be passed to this Thumbnailer.
	 */
	public String[] getAcceptedMIMETypes();
}
