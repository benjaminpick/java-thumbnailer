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
 * 
 * Concept from Nuxeo:
 * nuxeo-platform-mimetype-core/src/main/java/org/nuxeo/ecm/platform/mimetype/detectors/XlsMimetypeSniffer.java (v5.5)
 * Licenced LGPL 2.1 
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
 */

package de.uni_siegen.wineme.come_in.thumbnailer.util.mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XlsFileIdentifier extends OfficeFileIdentifier 
{
	public XlsFileIdentifier()
	{
		super();
		ext.add("xls");
	}

	@Override
	public String identify(String mimeType, byte[] bytes, File file) {

		if (isOfficeFile(mimeType) && !XLS_MIME_TYPE.equals(mimeType))
		{
			File destFile = null;
	        try {
	            destFile = File.createTempFile("fileidentifier", ".xls");
	            FileUtils.copyFile(file, destFile);
	            try {
	                FileInputStream stream = new FileInputStream(file);
	                HSSFWorkbook workbook = new HSSFWorkbook(stream);

	                if (workbook.getNumberOfSheets() != 0) {
	                    return XLS_MIME_TYPE;
	                }
	            } catch (Throwable e) {
	            	
	            }
	        } catch (IOException e) {
	        } finally {
	            if (destFile != null) {
	                destFile.delete();
	            }
	        }	
		}
		
		return mimeType;
	}
	
	/* Orig:

        public String[] process(byte[] data, int offset, int length, long bitmask,
            char comparator, String mimeType, Map params) {

        String[] mimetypes = { "" };
        File file = null;

        try {
            file = File.createTempFile("magicdetector", ".xls");
            FileUtils.writeFile(file, data);
            mimetypes = guessExcel(file);
        } catch (IOException e) {
            log.error(e, e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }

        return mimetypes;
    }

    public String[] process(File file, int offset, int length, long bitmask,
            char comparator, String mimeType, Map params) {

        return guessExcel(file);
    }

    public String[] guessExcel(File file) {

        String[] mimetypes = {};

        try {
            FileInputStream stream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(stream);
            if (workbook.getNumberOfSheets() != 0) {
                mimetypes = getHandledTypes();
            }
        } catch (FileNotFoundException e) {
            // This is not an excel file
            log.debug("MimeType detector : Not an excel file");
        } catch (IOException e) {
            // This is not an excel file
            log.debug("MimeType detector : Not an excel file");
        } catch (IllegalArgumentException e) {
            log.debug("MimeType detector : Not an excel file");
        } catch (Exception e) {
            log.error(e, e);
        }

        return mimetypes;
    }
    
    */

}
