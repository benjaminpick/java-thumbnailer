package de.uni_siegen.wineme.come_in.thumbnailer.plugin;

/**
 * Constants that are used to read/write lucene fields.
 * 
 * @author Benjamin
 */
public interface ThumbnailerLuceneConstants {
	/** Fieldname prefix that is used within the Lucene index */
	public static final String THUMBNAILER_INDEX_PREFIX = "thumbnailer_";

	/** Fieldnames and -values */
	public static final String LUCENE_FIELD_NAME_STATUS = THUMBNAILER_INDEX_PREFIX + "status";
	public static final String LUCENE_FIELD_VALUE_STATUS_OK = "ok";
	public static final String LUCENE_FIELD_VALUE_STATUS_NO_THUMBNAILER_FOUND = "nothumbnailerfound";
	public static final String LUCENE_FIELD_VALUE_STATUS_FAILED = "failed";
	public static final String LUCENE_FIELD_NAME_FILE_LOCATION = THUMBNAILER_INDEX_PREFIX + "filelocation";
}