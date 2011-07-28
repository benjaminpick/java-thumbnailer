package de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers;

import java.io.File;
import java.io.IOException;

/**
 * Dummy class for converting Presentation documents into Openoffice-Textfiles.
 * @see JODConverterThumbnailer
 */
public class JODPowerpointConverterThumbnailer extends JODConverterThumbnailer {

	public JODPowerpointConverterThumbnailer() throws IOException { super(); }
	public JODPowerpointConverterThumbnailer(String openOfficeHomeFolder, String paramOpenOfficeProfile) throws IOException { super(openOfficeHomeFolder, paramOpenOfficeProfile); }

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
				"application/vnd.openxmlformats-officedocument.presentationml", /* .presentation" */
				"application/vnd.ms-office", // ppt?
				"application/zip" // pptx?

		};
	}
	
}
