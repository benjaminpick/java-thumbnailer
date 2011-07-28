package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// Foreach Filename in TestFileDirectory : try to create a thumbnail. assertNoException.
public class ThumbnailImageStrangeFilesTest extends MyTestSuite
{
	// Foreach strange filename: rename the file in the test dir. 
	final static String STRANGE_FILENAMES[] = new String[] { "test space", "test%prozent", "test\nenter", "testäßumlaut", "....." };
	private static final char DS = File.separatorChar;

	public static Test suite() throws IOException
	{
		TestSuite ts = new TestSuite("ThumbnailStrangeFilenames");
		
		File path = new File (TESTFILES_DIR);
		File tmpDir = File.createTempFile("strange_files", "");
		File[] testfiles = path.listFiles();

		tmpDir.delete();
		tmpDir.mkdirs();
		for(File input : testfiles)
		{
			if (input.isDirectory())
				continue;
			
			for(String name : STRANGE_FILENAMES)
			{
				File output = new File(tmpDir.getAbsolutePath() + DS + name + "." + FilenameUtils.getExtension(input.getName()));
				try {
					FileUtils.copyFile(input, output);
				} catch (IOException e) {
					// Filename invalid on this filesystem.
					System.err.println("Warning: Filename " + output.getName() + " is invalid/could not be written.");
				}
			}
		}

		testfiles = tmpDir.listFiles();
		for(File input : testfiles)
		{
			if (input.isDirectory())
				continue;
			
			final File f = input;
			TestCase t = new ThumbnailerFileTestDummy("strange_names_" + getDisplayname(input)) {
				public void runTest() throws Exception {
					//setImageSize(160, 120, 0);
					create_thumbnail(f);
				}
			};
			ts.addTest(t);
		}
		
		return ts;
	}
}
