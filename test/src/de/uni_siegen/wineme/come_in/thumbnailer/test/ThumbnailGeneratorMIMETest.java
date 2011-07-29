package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.MimeTypeDetector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// Foreach Filename in TestFileDirectory : try to create a thumbnail. assertNoException.
public class ThumbnailGeneratorMIMETest extends MyTestSuite
{

	public static Test suite()
	{
		TestSuite ts = new TestSuite("ThumbnailGeneratorBasic");
		
		File path = new File (TESTFILES_DIR + "wrong_extension" + File.separatorChar);
		File[] testfiles = path.listFiles();
		
		final MimeTypeDetector mimeDetector = new MimeTypeDetector();
		for(File input : testfiles)
		{
			if (input.isDirectory())
				continue;
			
			final File f = input;
			TestCase t = new ThumbnailerFileTestDummy(getDisplayname(input)) {

				public void runTest() throws Exception {
					try {
						create_thumbnail(f);
					} catch (ThumbnailerException e) {
						fail("No suitable Thumbnailer for MIME Type " + mimeDetector.getMimeType(f));
					}
				}
			};
			ts.addTest(t);
		}
		
		return ts;
	}
}
