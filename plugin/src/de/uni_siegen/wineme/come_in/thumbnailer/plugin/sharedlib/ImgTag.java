/*
 * regain - A file search engine providing plenty of formats
 * Copyright (C) 2004  Til Schneider
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
 * Contact: Til Schneider, info@murfman.de
 *
 * CVS information:
 *  $RCSfile$
 *   $Source$
 *     $Date$
 *   $Author$
 * $Revision$
 */
package de.uni_siegen.wineme.come_in.thumbnailer.plugin.sharedlib;

import net.sf.regain.RegainException;
import net.sf.regain.search.SearchToolkit;
import net.sf.regain.search.sharedlib.hit.AbstractHitTag;
import net.sf.regain.util.sharedtag.PageRequest;
import net.sf.regain.util.sharedtag.PageResponse;

import org.apache.lucene.document.Document;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerConstants;
import de.uni_siegen.wineme.come_in.thumbnailer.plugin.ThumbnailerLuceneConstants;

/**
 * Generates the value of an index field of the current hit's document.
 * <p>
 * Tag Parameters:
 * <ul>
 * </ul>
 *
 * @author b
 */
public class ImgTag extends AbstractHitTag implements ThumbnailerLuceneConstants, ThumbnailerConstants {

  /**
   * Generates the tag.
   *
   * @param request The page request.
   * @param response The page response.
   * @param hit The current search hit.
   * @param hitIndex The index of the hit.
   * @throws RegainException If there was an exception.
   */
  protected void printEndTag(PageRequest request, PageResponse response,
          Document hit, int hitIndex)
          throws RegainException {
    //SearchResults results = SearchToolkit.getSearchResults(request);

    int width = getParameterAsInt("width", THUMBNAIL_DEFAULT_WIDTH);
    if (width < 0)
      width = THUMBNAIL_DEFAULT_WIDTH;
    
    int height = getParameterAsInt("height", THUMBNAIL_DEFAULT_HEIGHT);
    if (height < 0)
      height = THUMBNAIL_DEFAULT_HEIGHT;
    
    String size = "width=\"" + width + "\" height=\"" + height + "\"";

    String status = SearchToolkit.getCompressedFieldValue(hit, LUCENE_FIELD_NAME_STATUS);
    
    String location = null;
    if (LUCENE_FIELD_VALUE_STATUS_OK.equals(status))
      location = SearchToolkit.getCompressedFieldValue(hit, LUCENE_FIELD_NAME_FILE_LOCATION);
    
    // New For Thumbnail Tag
    if (location != null) {
        response.print("<img src=\"");
        response.printNoHtml(rewrite(location));
        response.print("\" " + size + " />");
    }
    else {
      String img_missing = getParameter("missing", false);
      if (img_missing != null && !img_missing.isEmpty())
        response.print("<img src=\"" +img_missing + "\" " + size + " />");
    }
  }

private String rewrite(String value) {
  // TODO: Config Rewrite Rule / Thumbnail Base Path
	return "thumbs/" + value;
}
}
