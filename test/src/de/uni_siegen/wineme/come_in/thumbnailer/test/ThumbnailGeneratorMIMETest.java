package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;
import java.util.Collection;

import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.util.mime.MimeTypeDetector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import uk.ac.lkl.common.util.testing.LabelledParameterized;

import static org.junit.Assert.*;

@RunWith(LabelledParameterized.class)
public class ThumbnailGeneratorMIMETest extends ThumbnailerFileTestDummy
{
	public ThumbnailGeneratorMIMETest(String name, File input)
	{
		super(input);
	}
	
	@Test
	public void generateThumbnail() throws Exception
	{
		create_thumbnail(inputFile);
	}

	@Parameters
	public static Collection<Object[]> listFiles()
	{
		return getFileList(TESTFILES_DIR + "wrong_extension");
	}
}
