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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.util.IOUtil;
import de.uni_siegen.wineme.come_in.thumbnailer.util.ResizeImage;
import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.ScratchFileIdentifier;

import edu.mit.scratch.*;

/**
 * This Thumbnailer extracts the Thumbnail from Scratch files.
 * This Thumbnail is generated from the start screen of the project.
 * 
 * (Scratch is a visual programming language for children.)
 * @author Benjamin
 * @TODO invent MIME type for scratch projects?
 * 
 * Depends on:
 * <li>ScratchApplet (needs modification: Move all classes to package "edu.mit.scratch" and make ObjReader public).
 */
public class ScratchThumbnailer extends AbstractThumbnailer {
	
	/**
	 * Generate a thumbnail from a Scratch file.
	 * 
	 * Adapted from GetThumbnail version 1.1, Jan Rochat 2009
	 * @see http://scratch.mit.edu/forums/viewtopic.php?id=13463
	 * 
	 * @param input		Scratch file to process
	 * @param output 	Where to save the thumbnail
	 * @throws ThumbnailerException If input is not a scratch file
	 * @throws IOException			If output cannot be written.
	 */
	public void generateThumbnail(File input, File output) throws ThumbnailerException, IOException
	{
		FileInputStream in = null;
		try {
			in = new FileInputStream(input);
			ObjReader reader = new ObjReader(in);
	
			Hashtable<?,?> parsedScratchFile;
			try {
				 parsedScratchFile = reader.readInfo();
			} catch (IOException e) {
				throw new ThumbnailerException("Error - Is this really a scratch project file?");
			}
			
			BufferedImage image = (BufferedImage) parsedScratchFile.get("thumbnail");

			/* Output internal data of Scratch files for debug purposes
			
			debugOutputHashtable(parsedScratchFile);
			
			// Rewind
			in = new FileInputStream(input);
			reader = new ObjReader(in);
			
			debugOutputObjects(reader.readObjects(null));
			*/

			ResizeImage imageResizer = new ResizeImage(thumbWidth, thumbHeight);
			imageResizer.setInputImage(image);
			imageResizer.writeOutput(output);
		} finally {
			IOUtil.quietlyClose(in);
		}
	}
	
	private void debugOutputObjects(Object[][] readObjects) {
		for (int i = 0; i < readObjects.length; i++)
		{
			for (int j = 0; j < readObjects[i].length; j++)
			{
				String str = readObjects[i][j].toString();
				if (!(readObjects[i][j] instanceof String))
					str = readObjects[i][j].getClass().getName() + ":" + str;
				System.out.println("obj[" + i + "][" + j + "]: " + readObjects[i][j]);
			}
		}
	}

	private void debugOutputHashtable(Hashtable<?, ?> parsedScratchFile) {
		for (Entry<?, ?> key : parsedScratchFile.entrySet())
		{
			System.out.println("Key: " + key.getKey() + " Value: " + key.getValue());
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
		return new String[]{ScratchFileIdentifier.SCRATCH_MIME_TYPE};
	}
}
