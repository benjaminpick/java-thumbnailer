package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;

import de.uni_siegen.wineme.come_in.thumbnailer.util.MimeTypeDetector;

public class MimeDetectionTest extends MyTestCase {
	private MimeTypeDetector mimeType;
	private File parent;

	public void setUp()
	{
		mimeType = new MimeTypeDetector();
		parent = new File(MyTestSuite.TESTFILES_DIR + "wrong_extension");
	}
	
	public void testImageFiles()
	{
		// Images
		assertMime("image/jpeg", "test2-jpg.bmp");
		assertMime("image/png", "test2-png.jpg");
		assertMime("image/bmp", "test2-bmp.png");
	}
	public void testTextFiles()
	{
		// Text
		assertMime("text/rtf", "test2-rtf.pdf");
		assertMime("application/pdf", "test2-pdf.ps");
	}
	
	public void testOpenOfficeFiles()
	{
		assertMime("application/zip", "test2-odp.pps");
	}
	
	public void testOpenOfficeFiles3()
	{
		assertMime("application/zip", "test2-ods.xls");
	}

	public void testOpenOfficeFiles5()
	{
		assertMime("application/vnd.sun.xml.writer", "test2-odt.sxw");
	}

	public void testOffice2007Files1()
	{
		assertMime("image/png", "test2-docx.doc");
	}

	public void testOfficeFiles2()
	{
		assertMime("image/png", "test2-ppt.odp");
	}

	public void testOfficeFiles3()
	{
		assertMime("image/png", "test2-doc.ott");
	}

	public void testOffice2007Files4()
	{
		assertMime("image/png", "test2-pptx.ppt");
	}
	public void testOfficeFiles5()
	{
		assertMime("image/png", "test2-xls.odp");
	}
	public void testOffice2007Files6()
	{
		assertMime("application/vnd.oasis.opendocument.spreadsh", "test2-xlsx.ods");
	}
	
	public void testScratch()
	{
		assertMime("application/x-mit-scratch", "test2-sb.sbx");
	}
	
	public void assertMime(String expectedMime, String filename)
	{
		String mime = mimeType.getMimeType(new File(parent, filename));
		if (!expectedMime.equalsIgnoreCase(mime))
			throw new AssertionError("File " + filename + ": Mime is not equal: expected \"" + expectedMime + "\", but was \"" + mime + "\".");
	}
}
