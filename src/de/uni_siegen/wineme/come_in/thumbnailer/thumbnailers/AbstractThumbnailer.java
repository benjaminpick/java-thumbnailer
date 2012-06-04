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

import java.io.File;
import java.io.IOException;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerConstants;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;

/**
 * This AbstractThumbnailer may be used in order to implement only essential methods.
 * It
 * <li>stores the current thumbnail height/width 
 * <li>implements an empty close method
 * <li>specifies an wildcard MIME Type as appropriate Filetype
 * 
 * @author Benjamin
 */
public abstract class AbstractThumbnailer implements Thumbnailer, ThumbnailerConstants {

	/**
	 * @var Height of thumbnail picture to create (in Pixel)
	 */
	protected int thumbHeight;

	/**
	 * @var Width of thumbnail picture to create (in Pixel)
	 */
	protected int thumbWidth;
	
	/**
	 * @var Options for image resizer (currently unused)
	 */
	protected int imageResizeOptions = 0;
	
	/**
	 * @var Keep memory if this thumbnailer was closed before.
	 */
	protected boolean closed = false;
	
	/**
	 * Initialize the thumbnail size from default constants.
	 */
	public AbstractThumbnailer()
	{
		thumbHeight = THUMBNAIL_DEFAULT_HEIGHT; 
		thumbWidth  = THUMBNAIL_DEFAULT_WIDTH; 
	}
	
	/**
	 * Set a new Thumbnail size. All following thumbnails will be generated in this size.
	 * 
	 * @param width					Width in Pixel
	 * @param height				Height in Pixel
	 * @param imageResizeOptions	Options for ResizeImage (currently ignored)
	 */
	@Override
	public void setImageSize(int thumbWidth, int thumbHeight, int imageResizeOptions) {
		this.thumbHeight = thumbHeight;
		this.thumbWidth = thumbWidth;
		this.imageResizeOptions = imageResizeOptions;
	}
	
	/**
	 * Get the currently set Image Width of this Thumbnailer.
	 * @return	image width of created thumbnails.
	 */
	public int getCurrentImageWidth()
	{
		return thumbWidth;
	}

	/**
	 * Get the currently set Image Height of this Thumbnailer.
	 * @return	image height of created thumbnails.
	 */
	public int getCurrentImageHeight()
	{
		return thumbHeight;
	}
	
	/**
	 * This function will be called after all Thumbnails are generated.
	 * Note: This acts as a Deconstructor. Do not expect this object to work
	 * after calling this method.
	 * 
	 * @throws IOException	If some errors occured during finalising
	 */
	@Override
	public void close() throws IOException {
		// Do nothing for now - other Thumbnailer may need cleanup code here.
		closed = true;
	}

	/**
	 * Call close() just in case the caller forgot.
	 */
	protected void finalize() throws Throwable 
	{
		try {
			super.finalize();
		} finally {
			if (!closed)
				close();
		}
	}
	
	/**
	 * Get a list of all MIME Types that this Thumbnailer is ready to process.
	 * You should override this method in order to give hints when which Thumbnailer is most appropriate.
	 * If you do not override this method, the Thumbnailer will be called in any case - awaiting a ThumbnailException if
	 * this thumbnailer cannot treat such a file.
	 * 
	 * @return List of MIME Types. If null, all Files may be passed to this Thumbnailer.
	 */	
	public String[] getAcceptedMIMETypes()
	{
		return null;
	}
	
	/**
	 * Generate a Thumbnail of the input file.
	 * (You can override this method if you want to handle the different MIME-Types).
	 * 
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @param mimeType	MIME-Type of input file (null if unknown)
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	public void generateThumbnail(File input, File output, String mimeType) throws IOException, ThumbnailerException {
		// Ignore MIME-Type-Hint
		generateThumbnail(input, output);
	}
}
