package de.uni_siegen.wineme.come_in.thumbnailer.test.slow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import de.uni_siegen.wineme.come_in.thumbnailer.test.MyTestSuite;
import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.MimeTypeDetector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// Foreach Filename in TestFileDirectory : combine extensions.
public class MIMEDectectionExtensiveTest extends MyTestSuite
{
	// Foreach strange filename: rename the file in the test dir. 
	private static final char DS = File.separatorChar;

	public static Test suite() throws IOException
	{
		TestSuite ts = new TestSuite("MIMEDectectionExtensive");
		MimeTypeDetector mimeType = new MimeTypeDetector();
		
		File path = new File (TESTFILES_DIR);
		File tmpDir = File.createTempFile("test-mime-detection", "");
		File[] testfiles = path.listFiles();

		tmpDir.delete();
		tmpDir.mkdirs();
		
		ArrayList<String> extensions = new ArrayList<String>(testfiles.length);
		for(File input : testfiles)
		{
			if (input.isDirectory() || input.isHidden())
				continue;
			
			extensions.add(FilenameUtils.getExtension(input.getName()));
		}
		
		for (File input : testfiles)
		{
			if (input.isDirectory() || input.isHidden())
				continue;
			
			String myName       = FilenameUtils.getBaseName(input.getName());
			String myExt  	    = FilenameUtils.getExtension(input.getName());
			final String myMime	= mimeType.getMimeType(input);
			
			for (String otherExt : extensions)
			{
				if (otherExt.equals(myExt))
					continue;
				
				final File output = new File(tmpDir.getAbsolutePath() + DS + myName + "- " + myExt + "." + otherExt);
				try {
					FileUtils.copyFile(input, output);
					
					TestCase t = new MimeDetectionTestDummy("test_mime_" + myExt + "_" + otherExt) {
						public void runTest() throws Exception {
							assertMime(myMime, output);
						}
					};
					ts.addTest(t);
					
				} catch (IOException e) {
					System.err.println("Warning: Filename " + output.getName() + " is could not be written.");
				}
			}
		}
		
		return ts;
	}
}
