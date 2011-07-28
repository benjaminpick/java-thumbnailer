package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;
import java.io.IOException;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODWordConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.NativeImageThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.OpenOfficeThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.PDFBoxThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.ScratchThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.Thumbnailer;

/**
 * Failures due to a file type mismatch
 * must emit a ThumbnailerException
 * 
 * @author Benjamin
 *
 */
public class ThumbnailersFailingTest extends MyTestCase {
	
	Thumbnailer currentThumbnailer;
	
	public void assert_fail_generation(File input) throws IOException
	{
		assertFileExists("Input file does not exist", input);
		
		File output = null;
		try {
			output = File.createTempFile("tmp", ".png");
		} catch (IOException e1) {
			fail("Could not create Temp file");
		}
		
		currentThumbnailer.setImageSize(160, 120, 0);
		try {
			currentThumbnailer.generateThumbnail(input, output);
		} catch (ThumbnailerException e) {
			return; // OK
		}
		fail("No Fail-Exception was thrown.");
	}
	
	public void tearDown()
	{
		try {
			if (currentThumbnailer != null)
				currentThumbnailer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testFailingScratch() throws Throwable
	{
		currentThumbnailer = new ScratchThumbnailer();
		File input = new File(MyTestSuite.TESTFILES_DIR + "test.txt");
		
		assert_fail_generation(input);
	}
	
	public void testFailingJOD() throws IOException
	{
		currentThumbnailer = new JODWordConverterThumbnailer();
		File input = new File(MyTestSuite.TESTFILES_DIR + "test.sb");
		
		assert_fail_generation(input);
	}

	public void testFailingOOo() throws IOException
	{
		currentThumbnailer = new OpenOfficeThumbnailer();

		File input = new File(MyTestSuite.TESTFILES_DIR + "test.jpg");
		assert_fail_generation(input);
		input = new File(MyTestSuite.TESTFILES_DIR + "do_not_work_yet" + File.separatorChar + "test.zip");
		assert_fail_generation(input);
	}

	public void testFailingPDFBox() throws IOException
	{
		currentThumbnailer = new PDFBoxThumbnailer();
		File input = new File(MyTestSuite.TESTFILES_DIR + "test.jpg");
		
		assert_fail_generation(input);
	}

	public void testFailingNativeImage() throws IOException
	{
		currentThumbnailer = new NativeImageThumbnailer();
		File input = new File(MyTestSuite.TESTFILES_DIR + "test.doc");
		
		assert_fail_generation(input);
	}
}
