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
package de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers;

import java.io.File;
import java.io.IOException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;

// For example code
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Example class for new Thumbnailers.
 * @see https://github.com/benjaminpick/java-thumbnailer/wiki/How-To-Write-A-New-Thumbnailer
 */
public class DummyThumbnailer extends AbstractThumbnailer {

	/**
	 * Generate a Thumbnail of the input file.
	 * 
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	@Override
	public void generateThumbnail(File input, File output) throws IOException, ThumbnailerException {
		if (input.getName() != "hello-world.txt")
			throw new ThumbnailerException("This is not a suitable file format!");
		
		// For testing purpose, just create an empty image.
		BufferedImage image = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
		ImageIO.write(image, "PNG", output);
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
		return new String[] {
			"text/hello-world",
			"text/hello-world-2"
		};
	}

}
