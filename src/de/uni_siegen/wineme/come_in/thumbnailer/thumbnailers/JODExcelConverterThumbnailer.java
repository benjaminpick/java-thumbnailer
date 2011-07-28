package de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers;

import java.io.File;
import java.io.IOException;

/**
 * Dummy class for converting Spreadsheet documents into Openoffice-Textfiles.
 * @see JODConverterThumbnailer
 */
public class JODExcelConverterThumbnailer extends JODConverterThumbnailer {

	public JODExcelConverterThumbnailer() throws IOException { super(); }
	public JODExcelConverterThumbnailer(String openOfficeHomeFolder, String paramOpenOfficeProfile) throws IOException { super(openOfficeHomeFolder, paramOpenOfficeProfile); }

	protected File createTempfile(String prefix) throws IOException {
		return File.createTempFile(prefix, ".ods");
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
				"application/vnd.ms-office", // xls?
				"application/zip" // xlsx?
		};
	}
	
}
