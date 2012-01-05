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

package de.uni_siegen.wineme.come_in.thumbnailer.util.mime;

import java.util.ArrayList;
import java.util.List;

/**
 * Improve detection of non-XML Office files.
 * 
 * Requires:
 *  - POI (version 3.7 or higher)
 */
public abstract class OfficeFileIdentifier implements MimeTypeIdentifier {

	protected static final String PPT_MIME_TYPE = "application/vnd.ms-powerpoint";
	protected static final String XLS_MIME_TYPE = "application/vnd.ms-excel";
	protected static final String DOC_MIME_TYPE = "application/vnd.ms-word";
	
	protected static final String MS_OFFICE_MIME_TYPE = "application/vnd.ms-office";

	
	protected List<String> ext;
	
	public OfficeFileIdentifier() {
		ext = new ArrayList<String>();
	}
	
	@Override
	public List<String> getExtensionsFor(String mimeType) {
		if (PPT_MIME_TYPE.equals(mimeType))
		{
			return ext;
		}
		return null; // I don't know
	}
	
	protected boolean isOfficeFile(String mimeType) {
		if (MS_OFFICE_MIME_TYPE.equals(mimeType))
			return true;
		if (mimeType == null)
			return false;
		if (mimeType.startsWith("application/vnd.ms"))
			return true;
		if (mimeType.startsWith("application/vnd.openxmlformats"))
			return true;
		
		return false;
	}
}
