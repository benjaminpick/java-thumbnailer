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

package de.uni_siegen.wineme.come_in.thumbnailer.util;

/**
 * Detect Platform.
 * @author Benjamin
 */

public class Platform {
	
	public static final int UNKNOWN = -1;

	public static final int WINDOWS = 0;
	
	public static final int UNIX_BEGIN = 10;
	public static final int MAC = 10;
	public static final int LINUX = 11;
	public static final int BSD = 12;
	public static final int UNIX_END = 19;
	
	private static int detectedPlatform;
	
	static
	{
		String os = System.getProperty("os.name");
		if (os.contains("indows"))
			detectedPlatform = WINDOWS;
		else if (os.contains("Mac"))
			detectedPlatform = MAC;
		else if (os.contains("Linux"))
			detectedPlatform = LINUX;
		else if (os.contains("BSD"))
			detectedPlatform = BSD;
		else
			detectedPlatform = UNKNOWN;
	}
	
	public static boolean isWindows()
	{
		return detectedPlatform == WINDOWS;
	}
	
	public static boolean isLinux()
	{
		return detectedPlatform == LINUX;
	}
	
	public static boolean isMac()
	{
		return detectedPlatform == MAC;
	}
	
	public static boolean isUnix()
	{
		return detectedPlatform >= UNIX_BEGIN && detectedPlatform >= UNIX_END;
	}
	
	public static int getDetectedPlatform()
	{
		return detectedPlatform;
	}
}
