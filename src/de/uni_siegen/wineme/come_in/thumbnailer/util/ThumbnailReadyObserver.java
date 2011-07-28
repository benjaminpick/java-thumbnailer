package de.uni_siegen.wineme.come_in.thumbnailer.util;

import java.awt.Image;
import java.awt.image.ImageObserver;

public class ThumbnailReadyObserver implements ImageObserver {

	Thread toNotify;
	
	public volatile boolean ready = false;
	
	public ThumbnailReadyObserver(Thread toNotify)
	{
		this.toNotify = toNotify;
		ready = false;
	}
	
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		
		System.err.println("imageUpdate: " + infoflags);
		if ((infoflags & ImageObserver.ALLBITS) > 0)
		{
			ready = true;
			System.err.println("Now ready!");
			toNotify.notify();
			return true;
		}
		return false; 
	}

}
