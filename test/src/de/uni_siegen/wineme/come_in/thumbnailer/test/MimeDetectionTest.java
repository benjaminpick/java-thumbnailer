package de.uni_siegen.wineme.come_in.thumbnailer.test;

import static org.junit.Assert.*;

import java.io.File;

import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.MimeTypeDetector;
import org.junit.Before;
import org.junit.Test;

public class MimeDetectionTest extends MyTestCase {
	private MimeTypeDetector mimeType;
	private File parent;

	public MimeDetectionTest()
	{
		new MimeTypeDetector(); // Initialize beforehand
	}
	
    @Before
    public void setUp()
	{
		mimeType = new MimeTypeDetector();
		parent = new File(TESTFILES_DIR + "wrong_extension");
	}
	
    @Test
    public void testGetExtensionFor()
	{
		assertEquals("odt", mimeType.getStandardExtensionForMimeType("application/vnd.oasis.opendocument.text"));
		assertTrue(mimeType.doesExtensionMatchMimeType("odt", "application/vnd.oasis.opendocument.text"));
	}
	
    @Test
    public void testImageFiles()
	{
		assertMime("image/png", "test2-png.jpg");
	}
    @Test
    public void testImageFiles2()
	{
		assertMime("image/jpeg", "test2-jpg.bmp");
	}
    @Test
    public void testImageFiles3()
	{
		assertMime("image/bmp", "test2-bmp.png");
	}
    @Test
    public void testTextFiles()
	{
		assertMime("text/rtf", "test2-rtf.pdf");
	}
	
    @Test
    public void testTextFiles2()
	{
		assertMime("application/pdf", "test2-pdf.ps");
	}
	
    @Test
    public void testOpenOfficeFiles()
	{
		assertMime("application/vnd.oasis.opendocument.presentation", "test2-odp.pps");
	}
	
    @Test
    public void testOpenOfficeFiles2()
	{
		assertMime("application/vnd.oasis.opendocument.spreadsheet", "test2-ods.xls");
	}

    @Test
    public void testOpenOfficeFiles3()
	{
		assertMime("application/vnd.oasis.opendocument.text", "test2-odt.sxw");
	}

    @Test
    public void testOpenOfficeFiles4()
	{
		assertMime("application/vnd.oasis.opendocument.text", "test2-odt.doc");
	}

    @Test
    public void testOffice2007Files()
	{
		assertMime("application/vnd.openxmlformats-officedocument.wordprocessingml", "test2-docx.doc");
	}

    @Test
    public void testOffice2007Files2()
	{
		assertMime("application/vnd.openxmlformats-officedocument.presentationml", "test2-pptx.ppt");
	}

    @Test
    public void testOffice2007Files3()
	{
		assertMime("application/vnd.openxmlformats-officedocument.spreadsheetml", "test2-xlsx.ods");
	}

    @Test
    public void testOffice2007Files4()
	{
		assertMime("application/vnd.openxmlformats-officedocument.spreadsheetml", "test2-xlsx.ppt");
	}

    @Test
    public void testOfficeFiles()
	{
		assertMime("application/vnd.ms-powerpoint", "test2-ppt.odp");
	}

    @Test
    public void testOfficeFiles2()
	{
		assertMime("application/vnd.ms-word", "test2-doc.ott");
	}

    @Test
    public void testOfficeFiles3()
	{
		assertMime("application/vnd.ms-excel", "test2-xls.odp");
	}
	
    @Test
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
