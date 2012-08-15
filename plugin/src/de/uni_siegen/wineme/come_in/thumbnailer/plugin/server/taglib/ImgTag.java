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
package de.uni_siegen.wineme.come_in.thumbnailer.plugin.server.taglib;

import net.sf.regain.util.sharedtag.taglib.SharedTagWrapperTag;

/**
 * Taglib wrapper for the shared msg tag.
 *
 * @see net.sf.regain.search.sharedlib.MsgTag
 * @author Til Schneider, www.murfman.de
 */
public class ImgTag extends SharedTagWrapperTag {

	private static final long serialVersionUID = 9145558444625323945L;

  /**
   * Creates a new instance of MsgTag.
   */
  public ImgTag() {
    super(new de.uni_siegen.wineme.come_in.thumbnailer.plugin.sharedlib.ImgTag());
  }
  
  public void setWidth(String width) {
    getNestedTag().setParameter("width", width);
  }

  public void setHeight(String height) {
    getNestedTag().setParameter("height", height);
  }

  public void setMissing(String missing) {
    getNestedTag().setParameter("missing", missing);
  }
}
