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
package de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.util.ResizeImage;

/**
 * Use Xuggler to create a thumbnail of a video file
 * @author Benjamin
 * @see http://build.xuggle.com/view/Stable/job/xuggler_jdk5_stable/ws/workingcopy/src/com/xuggle/mediatool/demos/DecodeAndCaptureFrames.java
 */
public class XugglerVideoThumbnailer extends AbstractThumbnailer {

	private static final float THUMBNAIL_IMAGE_TIME_PERCENT = 0.05f;

	protected IMediaReader videoReader;
	
	public XugglerVideoThumbnailer()
	{
		
	}
	
	/**
	 * Generate a Thumbnail of the input file.
	 * 
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	@Override
	public void generateThumbnail(File input, File output) throws IOException, ThumbnailerException {
		videoReader = ToolFactory.makeReader(input.getAbsolutePath());
		videoReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		
		long duration = videoReader.getContainer().getDuration();
		if (duration == Global.NO_PTS)
			duration = 30 * Global.DEFAULT_PTS_PER_SECOND;

		long whenToTakeThePicture = (long) THUMBNAIL_IMAGE_TIME_PERCENT * duration;
		DecodeAndCaptureFrames frameCapturer = new DecodeAndCaptureFrames(whenToTakeThePicture);
		videoReader.addListener(frameCapturer);
		
		while (videoReader.readPacket() == null)
		{
			// Do nothing, everything is in the listener!
		}
		
		BufferedImage thumbnailImage = frameCapturer.getThumbnailImage();
		if (thumbnailImage == null)
			thumbnailImage = frameCapturer.getFirstImage();
		
		new ResizeImage(thumbWidth, thumbHeight)
			.setInputImage(thumbnailImage)
			.writeOutput(output);
	}

    /**
     * Get a List of accepted File Types.
     * All OpenOffice Formats are accepted.
     * 
     * @return MIME-Types
     */
	@Override
	public String[] getAcceptedMIMETypes()
	{
		return new String[] {
			      "video/*"
		};
	}
	
	private class DecodeAndCaptureFrames extends MediaListenerAdapter
	{
		public DecodeAndCaptureFrames(long targetTimeThumbnailImage) {
			this.targetTimeThumbnailImage = targetTimeThumbnailImage;
		}

		public BufferedImage getThumbnailImage() {
			return thumbnailImage;
		}

		public BufferedImage getFirstImage() {
			return firstImage;
		}

		protected BufferedImage thumbnailImage;
		protected BufferedImage firstImage;

		  /**
		   * The video stream index, used to ensure we display frames from one
		   * and only one video stream from the media container.
		   */

		  private int mVideoStreamIndex = -1;
		
		  /**
		   * Target time when to take the thumbnail
		   */
		  private long targetTimeThumbnailImage = Global.NO_PTS;
		
		/** 
		   * Called after a video frame has been decoded from a media stream.
		   * Optionally a BufferedImage version of the frame may be passed
		   * if the calling {@link IMediaReader} instance was configured to
		   * create BufferedImages.
		   * 
		   * This method blocks, so return quickly.
		   */

		  public void onVideoPicture(IVideoPictureEvent event)
		  {
		    try
		    {
		      // if the stream index does not match the selected stream index,
		      // then have a closer look
		      
		      if (event.getStreamIndex() != mVideoStreamIndex)
		      {
		        // if the selected video stream id is not yet set, go ahead an
		        // select this lucky video stream
		        
		        if (-1 == mVideoStreamIndex)
		          mVideoStreamIndex = event.getStreamIndex();
		        
		        // otherwise return, no need to show frames from this video stream
		        
		        else
		          return;
		      }
		      
		      if (firstImage == null)
		      {
		    	firstImage = event.getImage();  
		      }
		      
		      if (event.getTimeStamp() >= targetTimeThumbnailImage )
		      {
		    	  thumbnailImage = event.getImage();
		      }
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		  }
	}
}
