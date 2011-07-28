package de.uni_siegen.wineme.come_in.thumbnailer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifierFactory;

/**
 * Wrapper class for MIME Identification of Files.
 * 
 * Depends:
 * <li>Aperture (for MIME-Detection)
 */
public class MimeTypeDetector {

	private MagicMimeTypeIdentifier mimeTypeIdentifier;
	
	private static Logger mLog = Logger.getLogger(MimeTypeDetector.class);

	public MimeTypeDetector()
	{
		MagicMimeTypeIdentifierFactory mimeTypeFactory = new MagicMimeTypeIdentifierFactory();
		mimeTypeIdentifier = (MagicMimeTypeIdentifier) mimeTypeFactory.get();
	}
	
	/**
	 * Detect MIME-Type for this file.
	 * 
	 * @TODO: Add detection of Scratch files
	 * 
	 * @param file	
	 * @return	String of MIME-Type, or null if no detection was possible (or unknown MIME Type)
	 */
	public String getMimeType(File file)
	{
		byte[] bytes = new byte[mimeTypeIdentifier.getMinArrayLength()];
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
			fis = null;
		} catch (IOException e) {
			return null; // File does not exist or other I/O Error
		} finally {
			IOUtil.quietlyClose(fis);
		}
		String file_url = file.toURI().toASCIIString();
		String mimeType = mimeTypeIdentifier.identify(bytes, file.getPath(), new URIImpl(file_url)) ;

		if (mimeType != null && mimeType.equalsIgnoreCase("application/zip")) {
			mLog.info("Is a zip-file. Try second round-detection ...");
			// some new files like MS Office documents are zip files
			// so rewrite the URL for the correct mimetype detection
			mimeType = mimeTypeIdentifier.identify(bytes, null, new URIImpl("zip:mime:" + file_url));
		}
		
		if (mimeType == null || mimeType.length() == 0)
			return null;
		
		mLog.info("Detected MIME-Type of " + file.getName() + " is " + mimeType);
		return mimeType;
	}
	
	public String getStandardExtensionForMimeType(String mimeType)
	{
		List<?> extensions = mimeTypeIdentifier.getExtensionsFor(mimeType);
		if (extensions == null)
			return null;
		
		try {
			return (String) extensions.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public boolean doesExtensionMatchMimeType(String extension, String mimeType)
	{
		List<?> extensions = mimeTypeIdentifier.getExtensionsFor(mimeType); // TODO: cache result?
		if (extensions == null)
			return false;
		return extensions.contains(extension);
	}
}
