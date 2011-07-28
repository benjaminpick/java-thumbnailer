package de.uni_siegen.wineme.come_in.thumbnailer.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
	/**
	 * Close, ignoring IOExceptions
	 * @param stream	Stream to be closed. May be null (in this case, nothing is done).
	 * @see Apache I/O Utils
	 */
	public static void quietlyClose(Closeable stream)
	{
		try
		{
			if (stream != null)
				stream.close();
		}
		catch (IOException e)
		{
			// Ignore
		}
	}
}
