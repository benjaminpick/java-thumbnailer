package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// Foreach Filename in TestFileDirectory : try to create a thumbnail. assertNoException.
public class ThumbnailImageFormatTest extends MyTestSuite
{
	public static Test suite()
	{
		TestSuite ts = new TestSuite("ThumbnailGeneratorBasic");
		
		File path = new File (TESTFILES_DIR + "format/");

		File[] testfiles = path.listFiles();
		
		for(File input : testfiles)
		{
			final File f = input;

			TestCase t = new ThumbnailerFileTestDummy("format_" + getDisplayname(input)) {
				public void runTest() throws Exception {
					create_thumbnail(f);
				}
			};
			ts.addTest(t);
		}
		
		return ts;
	}
}
