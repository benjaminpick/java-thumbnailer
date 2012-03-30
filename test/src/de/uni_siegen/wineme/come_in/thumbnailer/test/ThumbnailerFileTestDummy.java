package de.uni_siegen.wineme.come_in.thumbnailer.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;

import de.uni_siegen.wineme.come_in.thumbnailer.FileDoesNotExistException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerManager;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODExcelConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODHtmlConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODPowerpointConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODWordConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.NativeImageThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.OpenOfficeThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.PDFBoxThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.ScratchThumbnailer;

public class ThumbnailerFileTestDummy extends MyTestCase {
	protected File inputFile;

	public ThumbnailerFileTestDummy(File inputFile) {
		super();
		this.inputFile = inputFile;
	}
	
	ThumbnailerManager thumbnailer;
	
	public ThumbnailerFileTestDummy(String name)
	{
		this();
	}

	public ThumbnailerFileTestDummy() {
		super();
		JODConverterThumbnailer.connect();
	}

	@Before
	public void setUp() throws Exception
	{
		thumbnailer = new ThumbnailerManager();
		thumbnailer.setThumbnailFolder("thumbs/");
		
		thumbnailer.registerThumbnailer(new NativeImageThumbnailer());


		thumbnailer.registerThumbnailer(new OpenOfficeThumbnailer());
		thumbnailer.registerThumbnailer(new PDFBoxThumbnailer());
		
		try {
			thumbnailer.registerThumbnailer(new JODWordConverterThumbnailer());
			thumbnailer.registerThumbnailer(new JODExcelConverterThumbnailer());
			thumbnailer.registerThumbnailer(new JODPowerpointConverterThumbnailer());
			thumbnailer.registerThumbnailer(new JODHtmlConverterThumbnailer());
		} catch (IOException e) {
			mLog.error("Could not initialize JODConverter:", e);
		}

		thumbnailer.registerThumbnailer(new ScratchThumbnailer());

	}
	
	public void create_thumbnail(File file) throws FileDoesNotExistException, IOException, ThumbnailerException {
		create_thumbnail(file, thumbnailer.chooseThumbnailFilename(file, false));
	}
	
	public void create_thumbnail(File input, File output) throws FileDoesNotExistException, IOException, ThumbnailerException
	{
		assertFileExists("Input file does not exist", input);
		if (output != null && output.exists())
			output.delete();

		thumbnailer.generateThumbnail(input, output);
		assertFileExists("Output could not be generated", output);
		assertFalse("Output file is empty", 0 == output.length());
		assertPictureFormat(output, thumbnailer.getCurrentImageWidth(), thumbnailer.getCurrentImageHeight());
	}
	
	public void setImageSize(int height, int width, int opt)
	{
		thumbnailer.setImageSize(height, width, opt);
	}
	
	
	public static Collection<Object[]> getFileList(String path) {
		return getFileList(new File(path));
	}

	public static Collection<Object[]> getFileList(File path) {
		Collection<Object[]> files = new ArrayList<Object[]>();
		
		File[] testfiles = path.listFiles();
		
		for(File input : testfiles)
		{
			if (input.isDirectory())
				continue;
			
			files.add(new Object[] {getDisplayName(input), input});
		}
		return files;
	}
	
	public static String getDisplayName(File inputFile)
	{
		return inputFile.getName().replaceAll("[^a-zA-Z0-9]", "_");
	}
	

}