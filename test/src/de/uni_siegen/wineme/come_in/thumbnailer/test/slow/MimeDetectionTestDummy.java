package de.uni_siegen.wineme.come_in.thumbnailer.test.slow;

import java.io.File;

import de.uni_siegen.wineme.come_in.thumbnailer.test.MyTestCase;
import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.MimeTypeDetector;

public class MimeDetectionTestDummy extends MyTestCase {
	private MimeTypeDetector mimeType;
	
	public MimeDetectionTestDummy(String name)
	{
		super();
		this.setName(name);
	}
	
	public void setUp() throws Exception
	{
		mimeType = new MimeTypeDetector();
	}
	
	public void assertMime(String expectedMime, File file)
	{
		String mime = mimeType.getMimeType(file);
		if (!expectedMime.equalsIgnoreCase(mime))
			fail("File " + file.getName() + ": Mime is not equal: expected \"" + expectedMime + "\", but was \"" + mime + "\".");
	}
}
