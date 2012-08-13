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
 * 
 * Concept from Nuxeo:
 * Licenced LGPL 2.1 
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
 */

package de.uni_siegen.wineme.come_in.thumbnailer.util.mime;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hwpf.HWPFDocument;

public class DocFileIdentifier extends OfficeFileIdentifier 
{
	public DocFileIdentifier()
	{
		super();
		ext.add("doc");
	}

	@Override
	public String identify(String mimeType, byte[] bytes, File file) {

		if (isOfficeFile(mimeType) && !DOC_MIME_TYPE.equals(mimeType))
		{
			try {
                FileInputStream stream = new FileInputStream(file);
                HWPFDocument document = new HWPFDocument(stream);

                if (document.getRange().getEndOffset() > 0) {
                    return DOC_MIME_TYPE;
                }
            } catch (Throwable e) {
            }
		}
		
		return mimeType;
	}

}
