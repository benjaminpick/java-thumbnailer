/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2012  Come_IN Computerclubs (University of Siegen)
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
package de.uni_siegen.wineme.come_in.thumbnailer.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Keep a list of temporary files so that the same file needn't be copied twice.
 */
public class TemporaryFilesManager {
	private HashMap<File, File> files = new HashMap<File, File>();
	
	/**
	 * Create a new, read-only temporary file.
	 * 
	 * @param file			Original file that you need a copy of
	 * @param newExtension	The extension that the new file should have
	 * @return File (read-only) 
	 * @throws IOException
	 */
	public File createTempfileCopy(File file, String newExtension) throws IOException
	{
		File destFile = files.get(file);
		if (destFile == null)
		{
			destFile = File.createTempFile("temp", "." + newExtension);
			createNewCopy(file, destFile);
			destFile.setWritable(false, false);
		}
		else
		{
			String newFilename = FilenameUtils.removeExtension(destFile.getAbsolutePath()) + "." + newExtension;
			File newFile = new File(newFilename);
			boolean renameSucces = destFile.renameTo(newFile);
			if (!renameSucces)
			{
				createNewCopy(file, newFile);
			}
			files.put(file, newFile);
			destFile = newFile;
		}
		return destFile;
	}

	private void createNewCopy(File file, File destFile)
			throws IOException {
		FileUtils.copyFile(file, destFile);
		files.put(file, destFile);
	}
	
	/**
	 * Delete all registered temporary files
	 */
	public void deleteAllTempfiles() {
		for (File destFile : files.values())
		{
			IOUtil.deleteQuietlyForce(destFile);
		}
	}
}
