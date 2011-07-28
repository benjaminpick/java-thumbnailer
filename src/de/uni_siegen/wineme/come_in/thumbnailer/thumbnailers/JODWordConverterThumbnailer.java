package de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers;

import java.io.File;
import java.io.IOException;

/**
 * Dummy class for converting Text documents into Openoffice-Textfiles.
 * @see JODConverterThumbnailer
 */
public class JODWordConverterThumbnailer extends JODConverterThumbnailer {

	public JODWordConverterThumbnailer() throws IOException { super(); }
	public JODWordConverterThumbnailer(String openOfficeHomeFolder, String paramOpenOfficeProfile) throws IOException { super(openOfficeHomeFolder, paramOpenOfficeProfile); }

	protected File createTempfile(String prefix) throws IOException {
		return File.createTempFile(prefix, ".odt");
	}

	protected String getStandardZipExtension() {
		return "docx";
	}
	protected String getStandardOfficeExtension() {
		return "doc";
	}

    /**
     * Get a List of accepted File Types.
     * All Text Office Formats that OpenOffice understands are accepted.
     * (txt, rtf, doc, docx, wpd)
     * 
     * @return MIME-Types
     * @see http://www.artofsolving.com/opensource/jodconverter/guide/supportedformats
     */
	public String[] getAcceptedMIMETypes()
	{
		return new String[]{
				"text/plain",
				"text/rtf",
/*				"application/msword", */
				"application/vnd.ms-word",
				"application/vnd.openxmlformats-officedocument.wordprocessingml",
/*				"application/vnd.openxmlformats-officedocument.wordprocessingml.document", */
				"application/wordperfect",
				"application/vnd.ms-office", // ppt?
				"application/zip" // docx?
		};
	}
	
}
