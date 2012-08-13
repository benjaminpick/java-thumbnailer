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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import de.uni_siegen.wineme.come_in.thumbnailer.FileDoesNotExistException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.util.ResizeImage;

/**
 * Renders the first page of a PDF file into a thumbnail.
 * 
 * Performance note: This takes about 2-3 seconds per file. 
 * (TODO : Try to override PDPage.convertToImage - this is where the heavy lifting takes place)
 * 
 * Depends on:
 * <li>PDFBox (>= 1.5.0)
 */
public class PDFBoxThumbnailer extends AbstractThumbnailer {

	private static final Color TRANSPARENT_WHITE = new Color(255, 255, 255, 0);
	
	@Override
	public void generateThumbnail(File input, File output) throws IOException,
			ThumbnailerException {
		FileDoesNotExistException.check(input);
		if (input.length() == 0)
			throw new FileDoesNotExistException("File is empty");
		FileUtils.deleteQuietly(output);

		PDDocument document = null;
		try
		{
			try {
				document = PDDocument.load(input);
			} catch (IOException e) {
				throw new ThumbnailerException("Could not load PDF File", e);
			}

			BufferedImage tmpImage = writeImageFirstPage(document, BufferedImage.TYPE_INT_RGB);

			if (tmpImage.getWidth() == thumbWidth)
			{
				ImageIO.write(tmpImage, "PNG", output);
			}
			else
			{
				ResizeImage resizer = new ResizeImage(thumbWidth, thumbHeight);
				resizer.resizeMethod = ResizeImage.NO_RESIZE_ONLY_CROP;
				resizer.setInputImage(tmpImage);
				resizer.writeOutput(output);				
			}
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
	 * @return
	 * @throws IOException
	 */
    private BufferedImage writeImageFirstPage(PDDocument document, int imageType)
    throws IOException
    {
    	List<?> pages = document.getDocumentCatalog().getAllPages();

    	PDPage page = (PDPage)pages.get(0);

    	// resolution: Unfortunately, the resolution is in integer in the call ... so we approximate by taking slightly less (rounding down).
    	/* Before:
    	PDRectangle rect = page.getMediaBox();
    	float resolution = (thumb_width / rect.getWidth() * 72);
    	page.convertToImage(imageType, (int) resolution);
    	*/

    	// Here is the main work:
    	BufferedImage image = convertToImage(page, imageType, thumbWidth, thumbHeight); 
    	
    	return image;
    }
    
    /*     */   private BufferedImage convertToImage(PDPage page, int imageType, int thumbWidth, int thumbHeight)
    /*     */     throws IOException
    /*     */   {
    /* 707 */     PDRectangle mBox = page.findMediaBox();
    /* 708 */     float widthPt = mBox.getWidth();
    /* 709 */     float heightPt = mBox.getHeight();
    /* 711 */     int widthPx = thumbWidth; 			// Math.round(widthPt * scaling);
    /* 712 */     int heightPx = thumbHeight; 			// Math.round(heightPt * scaling);
    /* 710 */     float scaling = thumbWidth / widthPt; // resolution / 72.0F;
    /*     */ 
    /* 714 */     Dimension pageDimension = new Dimension((int) widthPt, (int) heightPt);
    /*     */ 
    /* 716 */     BufferedImage retval = new BufferedImage(widthPx, heightPx, imageType);
    /* 717 */     Graphics2D graphics = (Graphics2D)retval.getGraphics();
    /* 718 */     graphics.setBackground(TRANSPARENT_WHITE);
    /* 719 */     graphics.clearRect(0, 0, retval.getWidth(), retval.getHeight());
    /* 720 */     graphics.scale(scaling, scaling);
    /* 721 */     PageDrawer drawer = new PageDrawer();
    /* 722 */     drawer.drawPage(graphics, page, pageDimension);
    /*     */     try
    /*     */     {
    /* 728 */       int rotation = page.findRotation();
    /* 729 */       if ((rotation == 90) || (rotation == 270))
    /*     */       {
    /* 731 */         int w = retval.getWidth();
    /* 732 */         int h = retval.getHeight();
    /* 733 */         BufferedImage rotatedImg = new BufferedImage(w, h, retval.getType());
    /* 734 */         Graphics2D g = rotatedImg.createGraphics();
    /* 735 */         g.rotate(Math.toRadians(rotation), w / 2, h / 2);
    /* 736 */         g.drawImage(retval, null, 0, 0);
    /*     */       }
    /*     */     }
    /*     */     catch (ImagingOpException e)
    /*     */     {
    /* 741 */       //log.warn("Unable to rotate page image", e);
    /*     */     }
    /*     */ 
    /* 744 */     return retval;
    /*     */   }

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
