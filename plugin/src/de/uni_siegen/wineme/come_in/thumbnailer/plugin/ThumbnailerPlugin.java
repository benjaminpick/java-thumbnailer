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

package de.uni_siegen.wineme.come_in.thumbnailer.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.CompressionTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

import de.uni_siegen.wineme.come_in.thumbnailer.FileDoesNotExistException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerConstants;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerManager;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODExcelConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODHtmlConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODPowerpointConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODWordConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.NativeImageThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.OpenOfficeThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.PDFBoxThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.ScratchThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.util.IOUtil;
import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.MimeTypeDetector;

import net.sf.regain.RegainException;
import net.sf.regain.crawler.Crawler;
import net.sf.regain.crawler.config.PreparatorConfig;
import net.sf.regain.crawler.document.RawDocument;
import net.sf.regain.crawler.document.WriteablePreparator;
import net.sf.regain.crawler.plugin.AbstractCrawlerPlugin;

/**
 * Integration of Thumbnailer into Regain
 * 
 * In order to save ressources, the Thumbnail Library remains loaded only until the end of the crawling process.
 * 
 * @author Benjamin
 */
public class ThumbnailerPlugin extends AbstractCrawlerPlugin implements ThumbnailerLuceneConstants, ThumbnailerConstants
{
	/**
	 * @var Logger instance
	 */
	private static Logger mLog = Logger.getLogger(ThumbnailerPlugin.class);

	/**
	 * @var Thumbnailer Manager (can be null if deactivated)
	 */
	private ThumbnailerManager thumbnailer;
	
	/**
	 * @var If thumbnails should be generated.
	 * This can be deactivated due to initialization errors, or via config.
	 */
	private boolean thumbnailGenerationDeactivated = false;

	/**
	 * Parameters that were found in the XML Configuration
	 */
	private int paramThumbnailWidth;
	private int paramThumbnailHeight;
	private File paramThumbnailFolder;
	private String paramOpenOfficeHome;
	private String paramOpenOfficeProfile;
	private int paramOpenOfficePort;

	private MimeTypeDetector mimeTypeDetector;
		
	/**
	 * Initializes the plugin.
	 *
	 * @param 	config 	The configuration for this plugin.
	 * @throws RegainException When the configuration has an error.
	 */
	@Override
	public void init(PreparatorConfig config) throws RegainException {
		Map<String, String> thumbnailConfig = config.getSectionWithName("thumbnailing");
		
		String paramThumbnailFolderStr = null;
		if (thumbnailConfig != null)
			paramThumbnailFolderStr = thumbnailConfig.get("thumbnailFolder");
		
		if (paramThumbnailFolderStr == null || paramThumbnailFolderStr.isEmpty())
		{
			mLog.warn("Thumbnail folder is not given; using default value (thumbs/)");
			paramThumbnailFolderStr = "thumbs/";
		}
		paramThumbnailFolder = new File(paramThumbnailFolderStr);
		
		if (thumbnailConfig != null)
		{
			try {
				paramThumbnailWidth = Integer.parseInt(thumbnailConfig.get("imageWidth"));
				paramThumbnailHeight = Integer.parseInt(thumbnailConfig.get("imageHeight"));
			} catch (NumberFormatException e) {
				mLog.warn("Could not parse desired thumbnail height/width (are these really integers?); using default values (" + THUMBNAIL_DEFAULT_WIDTH + "x" + THUMBNAIL_DEFAULT_WIDTH +")", e);
			}
		}
		if (paramThumbnailHeight <= 0)
		{
			mLog.warn("Invalid value for thumbnail height (" + paramThumbnailHeight + "): taking default " + THUMBNAIL_DEFAULT_WIDTH + "x" + THUMBNAIL_DEFAULT_WIDTH);
			paramThumbnailWidth = THUMBNAIL_DEFAULT_WIDTH;
			paramThumbnailHeight = THUMBNAIL_DEFAULT_HEIGHT;				
		} else if (paramThumbnailWidth <= 0) {
			mLog.warn("Invalid value for thumbnail width (" + paramThumbnailWidth + "): taking default " + THUMBNAIL_DEFAULT_WIDTH + "x" + THUMBNAIL_DEFAULT_WIDTH);
			paramThumbnailWidth = THUMBNAIL_DEFAULT_WIDTH;
			paramThumbnailHeight = THUMBNAIL_DEFAULT_HEIGHT;				
		}
		
		Map<String, String> externalConfig = config.getSectionWithName("externalHelpers");
		if (externalConfig != null)
		{
			paramOpenOfficeHome = externalConfig.get("openOfficeHome");
			if (paramOpenOfficeHome != null && !(new File(paramOpenOfficeHome).exists()) )
			{
				mLog.error("ERROR: Could not find OpenOffice-Installation: The specified directory does not exist. Trying to auto-detect.");
				paramOpenOfficeHome = null;
			}
			
			paramOpenOfficeProfile = externalConfig.get("openOfficeProfile");

			try {
				if (externalConfig.get("openOfficePort") != null)
					paramOpenOfficePort = Integer.parseInt(externalConfig.get("openOfficePort"));
			} catch (NumberFormatException e) {
				mLog.error("ERROR: OpenOfficePort is not a number");
			}
		}
		
		JODConverterThumbnailer.setOpenOfficeHomeFolder(paramOpenOfficeHome);
		JODConverterThumbnailer.setOpenOfficeProfileFolder(paramOpenOfficeProfile);
		JODConverterThumbnailer.setOpenOfficePort(paramOpenOfficePort);
		
		mimeTypeDetector = new MimeTypeDetector();
	}
	
	/**
	 * Called before the crawling process starts (Crawler::run()).
	 * Initialize Thumbnail Generation:
	 * load Config and configure Thumbnailer
	 * 
	 * This may be called multiple times during the lifetime of a plugin instance,
	 * but CrawlerPlugin::onFinishCrawling() is always called in between.
	 * 
	 * @param crawler 		The crawler instance that is about to begin crawling
	 */
	public void onStartCrawling(Crawler crawler) {
		if (thumbnailGenerationDeactivated)
		{
			mLog.info("Do not start thumbnail generation (deactivated)");
			return;
		}	

		mLog.info("Initialize Thumbnail Generation...");
		thumbnailer = new ThumbnailerManager(); 
		
		thumbnailer.setImageSize(paramThumbnailWidth, paramThumbnailHeight, 0);
		try {
			thumbnailer.setThumbnailFolder(paramThumbnailFolder);
		} catch (FileDoesNotExistException e) {
			mLog.error("Could not set Thumbnail Directory:", e);
			thumbnailer = null;
			mLog.warn("Thumbnailer Plugin was deactivated due to prior errors");
			return;
		}

		try {
			thumbnailer.registerThumbnailer(new NativeImageThumbnailer());
	
			thumbnailer.registerThumbnailer(new OpenOfficeThumbnailer());
			thumbnailer.registerThumbnailer(new PDFBoxThumbnailer());
			try {
				thumbnailer.registerThumbnailer(new JODWordConverterThumbnailer());
				thumbnailer.registerThumbnailer(new JODExcelConverterThumbnailer());
				thumbnailer.registerThumbnailer(new JODPowerpointConverterThumbnailer());
				thumbnailer.registerThumbnailer(new JODHtmlConverterThumbnailer());
			} catch (IOException e) {
				mLog.error("Could not initialize JODConverter:", e);
			}
	
			thumbnailer.registerThumbnailer(new ScratchThumbnailer());
		} catch (RuntimeException e) {
			mLog.error("Not all thumbnailers could be registered:", e);
		}
	}

	/**
	 * Called after the crawling process has finished or aborted (because of an exception):
	 * Close the Thumbnail Generator.
	 * 
	 * This may be called multiple times during the lifetime of a plugin instance.
	 * 
	 * @param crawler 		The crawler instance that is about to finish crawling
	 */
	public void onFinishCrawling(Crawler crawler) {
		if (thumbnailer == null)
			return;
		
		mLog.info("De-Initialize Thumbnail Generation...");
		thumbnailer.close();
		thumbnailer = null;
	}

	/**
	 * Called when a document is deleted from the index:
	 * Delete the created thumbnail
	 * 
	 * Note that when being replaced by another document ("update index"),
	 * the old document is added to index first, deleting is part of the cleaning-up-at-the-end-Phase.
	 * 
	 * @param doc			  Document to read
	 * @param index			Luce Index Reader
	 */
	public void onDeleteIndexEntry(Document doc, IndexReader index) {
		String location = getLuceneField(doc, LUCENE_FIELD_NAME_FILE_LOCATION);
		
		if (location != null && !location.isEmpty())
		{
			File thumbnail = new File(paramThumbnailFolder, location);
			if (thumbnail.exists())
			{
				mLog.info("Deleting thumbnail " + thumbnail.getName() + "...");
				if (!thumbnail.delete())
				{
					mLog.warn("Couldn't delete thumbnail " + thumbnail.getName() + " - will delete it on program exit.");
					thumbnail.deleteOnExit();
				}
			}
		}
	}

	/**
	 * Get a field that was added as "addional field" before.
	 * 
	 * @param doc		Lucene Index entry
	 * @param fieldname	Index entry name
	 * @return String that was in this column
	 */
	protected String getLuceneField(Document doc, String fieldname) {
		String location;
		try {
			byte[] compressedData = doc.getBinaryValue(fieldname);
			if (compressedData == null)
				return null;
			location = CompressionTools.decompressString(compressedData);
			// uncompressed:
			// location = field.stringValue();
		} catch (DataFormatException e) {
			mLog.error("Compressed field could not be decompressed.");
			return null;
		}
		return location;
	}

	/**
	 * Called after a document is being prepared to be added to the index:
	 * Create the thumbnail and add the information about its creation to the lucene entry.
	 * 
	 * @param document		Regain document that was analysed
	 * @param preparator	Preparator that has analysed this document
	 */
	public void onAfterPrepare(RawDocument document, WriteablePreparator preparator) {
		String thumbnailerStatus = "";
		String thumbnailLocation = "";
		
		if (!thumbnailGenerationDeactivated && thumbnailer != null)
		{
			// Fetch input file
			File input;
			try {
				input = document.getContentAsFile();
			} catch (RegainException e) {
				mLog.error("File could not be thumbnailed: input file could not be retrieved from Regain", e);
				return;
			}
			// Get Mime
			String mimeType = mimeTypeDetector.getMimeType(input);
			mLog.debug("Detected Mime-Typ: " + mimeType);
			document.setMimeType(mimeType);
			
			// Generate Thumbnail
			File output = thumbnailer.chooseThumbnailFilename(input, true);

			try {
				thumbnailer.generateThumbnail(input, output, mimeType);

				thumbnailLocation = IOUtil.getRelativeFilename(paramThumbnailFolder, output);
				mLog.info("Generated Thumbnail at " + thumbnailLocation);
				thumbnailerStatus = LUCENE_FIELD_VALUE_STATUS_OK;
			} catch (IOException e) {
				mLog.error("File could not be thumbnailed: ", e);
				thumbnailerStatus = LUCENE_FIELD_VALUE_STATUS_FAILED;
			} catch (ThumbnailerException e) {
				thumbnailerStatus = LUCENE_FIELD_VALUE_STATUS_NO_THUMBNAILER_FOUND;
				mLog.error("File could not be thumbnailed: ", e);
			}
		}
		
		// Add infos to Lucene index
		preparator.addAdditionalField(LUCENE_FIELD_NAME_STATUS, thumbnailerStatus);
		preparator.addAdditionalField(LUCENE_FIELD_NAME_FILE_LOCATION, thumbnailLocation);
	}
}
