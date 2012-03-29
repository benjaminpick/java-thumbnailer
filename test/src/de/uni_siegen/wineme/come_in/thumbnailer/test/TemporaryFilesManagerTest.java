package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;

import de.uni_siegen.wineme.come_in.thumbnailer.util.TemporaryFilesManager;

import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TemporaryFilesManagerTest {
    
	TemporaryFilesManager tfm = null;
	
	@Before
	public void setUp() {
		tfm = new TemporaryFilesManager();
	}
	
	@After
	public void tearDown() {
		tfm.deleteAllTempfiles();
	}
	
	@Test
    public void test() throws Exception {
		File input = File.createTempFile("test-input", ".txt");
		
		File output = tfm.createTempfileCopy(input, "bla");
		assertEquals("bla", FilenameUtils.getExtension(output.getName()));
		assertFalse("Copy didn't work", input.equals(output));
		assertFalse("Copy/Rename didn't work", FilenameUtils.getBaseName(input.getName()).equals(
										FilenameUtils.getBaseName(output.getName())));

		File output2 = tfm.createTempfileCopy(input, "bla");
		assertEquals(output, output2);
		
		File output3 = tfm.createTempfileCopy(input, "blub");
		assertEquals(FilenameUtils.getBaseName(output.getName()) + ".blub", output3.getName());

		File output4 = tfm.createTempfileCopy(input, "blub");
		assertEquals(output3, output4);
	}
}
