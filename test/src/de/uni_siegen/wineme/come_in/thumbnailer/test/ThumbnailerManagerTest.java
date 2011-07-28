package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;
import java.io.IOException;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerManager;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.OpenOfficeThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.Thumbnailer;

public class ThumbnailerManagerTest extends MyTestCase {
	
	ThumbnailerManager thumbnailer;

	public void setUp() throws Exception
	{
		super.setUp();
		thumbnailer = new ThumbnailerManager();
		thumbnailer.setThumbnailFolder(MyTestSuite.TESTFILES_DIR);
	}

	public void testSetImageSize()
	{
		thumbnailer.setImageSize(100, 200, 0);
		Thumbnailer newThumbnailer = new OpenOfficeThumbnailer();
		thumbnailer.registerThumbnailer(newThumbnailer, 0);
		
		assertEquals("Width is not correct!", 100, thumbnailer.getCurrentImageWidth());
		assertEquals("Width is not correct!", 100, newThumbnailer.getCurrentImageWidth());
		
		thumbnailer.setImageSize(140, 240, 0);

		assertEquals("Width is not correct!", 140, thumbnailer.getCurrentImageWidth());
		assertEquals("Width is not correct!", 140, newThumbnailer.getCurrentImageWidth());
	}

	public void testThumbnailerChooseThumbnailNameExists()
	{
		File first = thumbnailer.chooseThumbnailFilename(new File("abc.png"), true);
		try {
			first.createNewFile();
		} catch (IOException e) {
			fail("The chosen thumbnail File " + first.getAbsolutePath() + " cannot be written.");
		}
		File second = thumbnailer.chooseThumbnailFilename(new File("abc.png"), true);
		assertTrue("The filenames are not unique (second thumbnail would overwrite first).", first.getAbsolutePath() != second.getAbsolutePath());
		first.delete();
		second.delete();
	}

}