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

/**
 * Dummy class for converting Presentation documents into Openoffice-Textfiles.
 * @see JODConverterThumbnailer
 */
public class JODPowerpointConverterThumbnailer extends JODConverterThumbnailer {

	public JODPowerpointConverterThumbnailer() throws IOException { super(); }

	protected File createTempfile(String prefix) throws IOException {
		return File.createTempFile(prefix, ".odp");
	}
	
	protected String getStandardZipExtension() {
		return "pptx";
	}
	protected String getStandardOfficeExtension() {
		return "ppt";
	}


    /**
     * Get a List of accepted File Types.
     * All Presentation Office Formats that OpenOffice understands are accepted.
     * (ppt, pptx, pps, ppsx)
     * 
     * @return MIME-Types
     * @see http://www.artofsolving.com/opensource/jodconverter/guide/supportedformats
     */
	public String[] getAcceptedMIMETypes()
	{
		return new String[]{
				"application/vnd.ms-powerpoint",
				"application/vnd.openxmlformats-officedocument.presentationml",
				"application/vnd.openxmlformats-officedocument.presentationml.presentation",
				"application/vnd.ms-office", // ppt?
				"application/zip" // pptx?

		};
	}
	
}
