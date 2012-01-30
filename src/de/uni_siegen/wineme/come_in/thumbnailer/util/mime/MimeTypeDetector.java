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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifierFactory;
import org.semanticdesktop.aperture.tika.TikaMimeTypeIdentifier;

import de.uni_siegen.wineme.come_in.thumbnailer.util.IOUtil;

/**
 * Wrapper class for MIME Identification of Files.
 * 
 * Depends:
 * <li>Aperture (for MIME-Detection)
 */
public class MimeTypeDetector {

	private TikaMimeTypeIdentifier mimeTypeIdentifier;

	private List<MimeTypeIdentifier> extraIdentifiers;
	
	private static Logger mLog = Logger.getLogger(MimeTypeDetector.class);

	/**
	 * Create a MimeType Detector and init it.
	 */
	public MimeTypeDetector()
	{
		//MagicMimeTypeIdentifierFactory mimeTypeFactory = new MagicMimeTypeIdentifierFactory();
		//mimeTypeIdentifier = (MagicMimeTypeIdentifier) mimeTypeFactory.get();
		
		mimeTypeIdentifier = new TikaMimeTypeIdentifier();
		
		extraIdentifiers = new ArrayList<MimeTypeIdentifier>();
		
		addMimeTypeIdentifier(new ScratchFileIdentifier());
		addMimeTypeIdentifier(new Office2007FileIdentifier());
		//addMimeTypeIdentifier(new PptFileIdentifier());
		//addMimeTypeIdentifier(new XlsFileIdentifier());
		//addMimeTypeIdentifier(new DocFileIdentifier());
	}

	/**
	 * Add a new MimeTypeIdentifier to this Detector.
	 * MimeTypeIdentifier may override the decision of the detector.
	 * The order the identifiers are added will also be the order they will be executed
	 * (i.e., the last identifiers may override all others.)
	 * 
	 * @param identifier	a new MimeTypeIdentifier
	 */
	public void addMimeTypeIdentifier(MimeTypeIdentifier identifier)
	{
		extraIdentifiers.add(identifier);
	}
	
	/**
	 * Detect MIME-Type for this file.
	 * 
	 * @param file	File to analyse
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

		/* I don't see any effect of this
		if (mimeType != null && mimeType.equalsIgnoreCase("application/zip")) {
			mLog.info("Is a zip-file. Try second round-detection ...");
			// some new files like MS Office documents are zip files
			// so rewrite the URL for the correct mimetype detection
			mimeType = mimeTypeIdentifier.identify(bytes, null, new URIImpl("zip:mime:" + file_url));
		}
		*/
		
		if (mimeType != null && mimeType.length() == 0)
			mimeType = null;
		
		// Identifiers may re-write MIME.
		for (MimeTypeIdentifier identifier : extraIdentifiers)
			mimeType = identifier.identify(mimeType, bytes, file);
		
		mLog.info("Detected MIME-Type of " + file.getName() + " is " + mimeType);
		return mimeType;
	}
	
	/**
	 * Return the standard extension of a specific MIME-Type.
	 * What are these files "normally" called?
	 * 
	 * @param mimeType	MIME-Type, e.g. "text/plain"
	 * @return	Extension, e.g. "txt"
	 */
	public String getStandardExtensionForMimeType(String mimeType)
	{
		List<String> extensions = getExtensionsCached(mimeType);
		
		if (extensions == null)
			return null;
		
		try {
			return extensions.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	Map<String, List<String>> extensionsCache = new HashMap<String, List<String>>(); 
	
	@SuppressWarnings("unchecked")
	protected List<String> getExtensionsCached(String mimeType) {
		List<String> extensions = extensionsCache.get(mimeType);
		if (extensions != null)
			return extensions;
		
		extensions = (List<String>) mimeTypeIdentifier.getExtensionsFor(mimeType);
		
		for (MimeTypeIdentifier identifier : extraIdentifiers)
		{
			if (extensions != null)
				return extensions;
			
			extensions = identifier.getExtensionsFor(mimeType);
		}
		
		extensionsCache.put(mimeType, extensions);
		return extensions;
	}
	
	/**
	 * Test if an given extension can contain a File of MIME-Type
	 * @param extension	Filename extension (e.g. "txt")
	 * @param mimeType	MIME-Type		   (e.g. "text/plain")
	 * @return	True if compatible.
	 */
	public boolean doesExtensionMatchMimeType(String extension, String mimeType)
	{
		List<String> extensions = getExtensionsCached(mimeType); 
		
		if (extensions == null)
			return false;
	
		return extensions.contains(extension);
	}
}
