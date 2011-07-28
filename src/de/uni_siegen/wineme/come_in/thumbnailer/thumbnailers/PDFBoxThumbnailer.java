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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import de.uni_siegen.wineme.come_in.thumbnailer.FileDoesNotExistException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.util.ResizeImage;

/**
 * Renders the first page of a PDF file into a thumbnail.
 * 
 * Performance note: This usually takes about 3 seconds per file. 
 * (TODO : Try to override PDPage.convertToImage - this is where the heavy lifting takes place)
 * 
 * Depends on:
 * <li>PDFBox (>= 1.5.0)
 */
public class PDFBoxThumbnailer extends AbstractThumbnailer {

	@Override
	public void generateThumbnail(File input, File output) throws IOException,
			ThumbnailerException {
		FileDoesNotExistException.check(input);
		if (input.length() == 0)
			throw new FileDoesNotExistException("File is empty");

		PDDocument document = null;
		try
		{
			try {
				document = PDDocument.load(input);
			} catch (IOException e) {
				throw new ThumbnailerException("Could not load PDF File", e);
			}

			BufferedImage tmpImage = writeImageFirstPage(document, BufferedImage.TYPE_INT_RGB, thumbWidth);

			if (output.exists())
				output.delete();

			ResizeImage resizer = new ResizeImage(thumbWidth, thumbHeight);
			resizer.resizeMethod = ResizeImage.NO_RESIZE_ONLY_CROP;
			resizer.setInputImage(tmpImage);
			resizer.writeOutput(output);
		}
		finally
		{
			if( document != null )
			{
				try {
					document.close();
				} catch (IOException e)  {}
			}
		}
	}
	
	/**
	 * Loosely based on the commandline-Tool PDFImageWriter
	 * @param document
	 * @param imageType
	 * @param thumb_width
	 * @return
	 * @throws IOException
	 */
    private BufferedImage writeImageFirstPage(PDDocument document, int imageType, int thumb_width)
    throws IOException
    {
    	List<?> pages = document.getDocumentCatalog().getAllPages();

    	PDPage page = (PDPage)pages.get( 0 /* i */ );

    	PDRectangle rect = page.getMediaBox();
    	
    	double resolution = (thumb_width / rect.getWidth() * 72);

    	// resolution: Unfortunately, the resolution is in integer in the call ... so we approximate by taking slightly less (rounding down).
    	// Here is the main work:
    	BufferedImage image = page.convertToImage(imageType, (int) resolution);
    	
    	return image;
    }

    /**
     * Get a List of accepted File Types.
     * Only PDF Files are accepted.
     * 
     * @return MIME-Types
     */
	public String[] getAcceptedMIMETypes()
	{
		return new String[]{
				"application/pdf"
		};
	}

    
}
