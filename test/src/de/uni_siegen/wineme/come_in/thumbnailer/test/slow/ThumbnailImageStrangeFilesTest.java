package de.uni_siegen.wineme.come_in.thumbnailer.test.slow;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import de.uni_siegen.wineme.come_in.thumbnailer.test.TestConfiguration;
import de.uni_siegen.wineme.come_in.thumbnailer.test.ThumbnailerFileTestDummy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import uk.ac.lkl.common.util.testing.LabelledParameterized;

// Foreach Filename in TestFileDirectory : try to create a thumbnail. assertNoException.
@RunWith(LabelledParameterized.class)
// Strange as it sounds, these tests only run inside Eclipse. In Ant, the @RunWith-Annotation seems to go ignored. I'd really like to know why!
public class ThumbnailImageStrangeFilesTest extends ThumbnailerFileTestDummy implements TestConfiguration
{
	public ThumbnailImageStrangeFilesTest(String name, File input)
	{
		super(input);
	}
	
	@Test
	public void generateThumbnail() throws Exception
	{
		create_thumbnail(inputFile);
	}
	
	// Foreach strange filename: rename the file in the test dir. 
	final static String STRANGE_FILENAMES[] = new String[] { "test space", "test%prozent", "test\nenter", "testäßumlaut", "....." };

	@Parameters
	public static Collection<Object[]> suite() throws Exception
	{
		File path = new File (TESTFILES_DIR);
		File tmpDir = File.createTempFile("test-strange_files", "");
		File[] testfiles = path.listFiles();

		tmpDir.delete();
		tmpDir.mkdirs();
		for(File input : testfiles)
		{
			if (input.isDirectory())
				continue;
			
			for(String name : STRANGE_FILENAMES)
			{
				File output = new File(tmpDir.getAbsolutePath() + File.separator + name + "." + FilenameUtils.getExtension(input.getName()));
				try {
					FileUtils.copyFile(input, output);
				} catch (IOException e) {
					// Filename invalid on this filesystem.
					System.err.println("Warning: Filename " + output.getName() + " is invalid/could not be written.");
				}
			}
		}
		
		return getFileList(tmpDir);
	}
}
