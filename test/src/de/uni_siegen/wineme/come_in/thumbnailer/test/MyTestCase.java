package de.uni_siegen.wineme.come_in.thumbnailer.test;

import junit.framework.TestCase;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyTestCase extends TestCase {

	protected static Logger mLog = Logger.getLogger(MyTestCase.class);

	private static final String LOG4J_CONFIG_FILE = "test/log4j.properties";
	static
	{
		System.setProperty("log4j.configuration", LOG4J_CONFIG_FILE);
		
		File logConfigFile = new File(LOG4J_CONFIG_FILE);
	    if (!logConfigFile.exists()) {
	        System.out.println("ERROR: Logging configuration file not found: " + logConfigFile.getAbsolutePath());
	        System.exit(1); // Abort
	      }

      PropertyConfigurator.configureAndWatch(logConfigFile.getAbsolutePath(), 10 * 1000);
      mLog.info("Logging initialized");

	}
	
	public void assertFileExists(File file)
	{
		assertFileExists("", file);
	}
	
	public void assertFileExists(String msg, File file)
	{
		if (!msg.isEmpty())
			msg += ": ";

		assertNotNull(msg + "File is null", file);
		assertTrue(msg + "File " + file.getAbsolutePath() + " does not exist", file.exists());
	}
	
	public void assertPictureFormat(File file, int width, int height) throws IOException
	{
		assertFileExists("Picture file does not exist", file);
		Image image = ImageIO.read(file);
		assertNotNull("Picture " + file.getAbsolutePath() + " could not be decoded by ImageIO", image);
		assertPictureFormat(image, width, height);
	}
	
	public void assertPictureFormat(Image img, int expectedWidth, int expectedHeight)
	{
		assertNotNull("Picture is null", img);
		int realWidth = img.getWidth(null);
		int realHeight = img.getHeight(null);
		String realFormat =  realWidth + "x" + realHeight;
		String expectedFormat = expectedWidth + "x" + expectedHeight;
		assertEquals("Picture has not the right width (expected: " + expectedFormat + ", actual: " + realFormat + ")", expectedWidth, realWidth);
		assertEquals("Picture has not the right height (expected: " + expectedFormat + ", actual: " + realFormat + ")", expectedHeight, realHeight);
	}
	
	public void testBla()
	{
		assertTrue(true);
	}
}
