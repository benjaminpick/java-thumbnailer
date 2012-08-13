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

import java.io.IOException;

/**
 * Dummy class for converting Spreadsheet documents into Openoffice-Textfiles.
 * @see JODConverterThumbnailer
 */
public class JODExcelConverterThumbnailer extends JODConverterThumbnailer {

	public JODExcelConverterThumbnailer() throws IOException { super(); }

	protected String getStandardOpenOfficeExtension() {
		return "ods";
	}

	protected String getStandardZipExtension() {
		return "xlsx";
	}
	protected String getStandardOfficeExtension() {
		return "xls";
	}

	/**
     * Get a List of accepted File Types.
     * All Spreadsheet Office Formats that OpenOffice understands are accepted.
     * 
     * @return MIME-Types
     * @see http://www.artofsolving.com/opensource/jodconverter/guide/supportedformats
     */
	public String[] getAcceptedMIMETypes()
	{
		return new String[]{
				"application/vnd.ms-excel",
				"application/vnd.openxmlformats-officedocument.spreadsheetml",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
		/*		"application/vnd.ms-office", // xls?
				"application/zip" // xlsx? */
		};
	}


	
}
