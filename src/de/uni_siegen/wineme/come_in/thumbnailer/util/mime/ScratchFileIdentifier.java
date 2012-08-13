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

package de.uni_siegen.wineme.come_in.thumbnailer.util.mime;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 * Add detection of Scratch files.
 * A MIME Type didn't exist, so I invented "application/x-mit-scratch".
 */
public class ScratchFileIdentifier implements MimeTypeIdentifier {
	public static final String SCRATCH_MIME_TYPE = "application/x-mit-scratch";
	public static final String SCRATCH_EXTENSION = "sb";
	private static final String MAGIC_SCRATCH_HEADER = "ScratchV0";
	
	@Override
	public String identify(String mimeType, byte[] bytes, File file) {
		if (mimeType == null && SCRATCH_EXTENSION.equals(FilenameUtils.getExtension(file.getName())))
			return SCRATCH_MIME_TYPE;

		if (startWith(bytes, MAGIC_SCRATCH_HEADER))
			return SCRATCH_MIME_TYPE;
	
		return mimeType;
	}

	private boolean startWith(byte[] haystick, String needle) {
		try {
			byte[] b_needle = needle.getBytes("US-ASCII");

			for (int i = 0; i < b_needle.length; i++)
			{
				if (haystick[i] != b_needle[i])
					return false;
			}
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	@Override
	public List<String> getExtensionsFor(String mimeType) {
		if (SCRATCH_MIME_TYPE.equals(mimeType))
		{
			List<String> ext = new ArrayList<String>();
			ext.add(SCRATCH_EXTENSION);
			return ext;
		}
		return null; // I don't know
	}
}
