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

import javax.imageio.ImageIO;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.UnsupportedInputFileFormatException;
import de.uni_siegen.wineme.come_in.thumbnailer.util.ResizeImage;

/**
 * This class uses Java Image I/O (Java's internal Image Processing library) in order to resize images.
 * JAI can be extended with extra Readers, this Thumbnailer will use all available image readers.
 * 
 * Depends:
 * <li>JAI Image I/O Tools (optional, for TIFF support) (@see http://java.net/projects/imageio-ext/ - licence not gpl compatible I suspect ...)
 */
public class NativeImageThumbnailer extends AbstractThumbnailer {

	public void generateThumbnail(File input, File output) throws IOException, ThumbnailerException {
		ResizeImage resizer = new ResizeImage(thumbWidth, thumbHeight);
		
		try {
			resizer.setInputImage(input);
		} catch (UnsupportedInputFileFormatException e) {
			throw new ThumbnailerException("File format could not be interpreted as image", e);
		}
		resizer.writeOutput(output);		
	}

    /**
     * Get a List of accepted File Types.
     * Normally, these are: bmp, jpg, wbmp, jpeg, png, gif
     * The exact list may depend on the Java installation.
     * 
     * @return MIME-Types
     */
	public String[] getAcceptedMIMETypes()
	{
		return ImageIO.getReaderMIMETypes();
	}
}