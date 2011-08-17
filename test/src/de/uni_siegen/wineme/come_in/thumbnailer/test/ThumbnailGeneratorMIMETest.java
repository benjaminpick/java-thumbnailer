package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;
import java.util.Collection;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import uk.ac.lkl.common.util.testing.LabelledParameterized;

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
