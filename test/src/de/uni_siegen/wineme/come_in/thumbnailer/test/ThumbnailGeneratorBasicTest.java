package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// Foreach Filename in TestFileDirectory : try to create a thumbnail. assertNoException.
public class ThumbnailGeneratorBasicTest extends MyTestSuite
{

	public static Test suite()
	{
		TestSuite ts = new TestSuite("ThumbnailGeneratorBasic");
		
		File path = new File(TESTFILES_DIR);
		File[] testfiles = path.listFiles();
		
		for(File input : testfiles)
		{
			if (input.isDirectory())
				continue;
			
			final File f = input;
			TestCase t = new ThumbnailerFileTestDummy(getDisplayname(input)) {
				public void runTest() throws Exception {
					setImageSize(100, 75, 0);
					create_thumbnail(f);
				}
			};
			ts.addTest(t);
		}
		
		return ts;
	}
}
