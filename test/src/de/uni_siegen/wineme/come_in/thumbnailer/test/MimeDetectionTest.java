package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;

import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.MimeTypeDetector;

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
		assertMime("application/vnd.oasis.opendocument.presentation", "test2-odp.pps");
	}
	
	public void testOpenOfficeFiles2()
	{
		assertMime("application/vnd.oasis.opendocument.spreadsheet", "test2-ods.xls");
	}

	public void testOpenOfficeFiles3()
	{
		assertMime("application/vnd.oasis.opendocument.text", "test2-odt.sxw");
	}

	public void testOpenOfficeFiles4()
	{
		assertMime("application/vnd.oasis.opendocument.text", "test2-odt.doc");
	}

	public void testOffice2007Files()
	{
		assertMime("application/vnd.openxmlformats-officedocument.wordprocessingml", "test2-docx.doc");
	}

	public void testOffice2007Files2()
	{
		assertMime("application/vnd.openxmlformats-officedocument.presentationml", "test2-pptx.ppt");
	}

	public void testOffice2007Files3()
	{
		assertMime("application/vnd.openxmlformats-officedocument.spreadsheetml", "test2-xlsx.ods");
	}

	public void testOffice2007Files4()
	{
		assertMime("application/vnd.openxmlformats-officedocument.spreadsheetml", "test2-xlsx.ppt");
	}

	public void testOfficeFiles()
	{
		assertMime("application/vnd.ms-powerpoint", "test2-ppt.odp");
	}

	public void testOfficeFiles2()
	{
		assertMime("application/vnd.ms-word", "test2-doc.ott");
	}

	public void testOfficeFiles3()
	{
		assertMime("application/vnd.ms-excel", "test2-xls.odp");
	}
	
	public void testScratch()
	{
		assertEquals("Standard extension of scratch was wrongly returned", "sb", mimeType.getStandardExtensionForMimeType("application/x-mit-scratch"));
		assertTrue("doesExtensionMatchMimeType of Scratch didn't return true", mimeType.doesExtensionMatchMimeType("sb", "application/x-mit-scratch"));
		assertMime("application/x-mit-scratch", "test2-sb.sbx");
	}
	
	public void assertMime(String expectedMime, String filename)
	{
		String mime = mimeType.getMimeType(new File(parent, filename));
		if (!expectedMime.equalsIgnoreCase(mime))
			fail("File " + filename + ": Mime is not equal: expected \"" + expectedMime + "\", but was \"" + mime + "\".");
	}
}
