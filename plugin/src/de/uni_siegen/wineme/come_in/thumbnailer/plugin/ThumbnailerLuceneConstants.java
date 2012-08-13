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