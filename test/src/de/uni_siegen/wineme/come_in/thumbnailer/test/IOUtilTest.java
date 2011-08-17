package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;
import java.io.IOException;

import de.uni_siegen.wineme.come_in.thumbnailer.util.IOUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilTest {
    @Test
    public void testGetRelativePathsUnix() {
	    assertEquals("stuff/xyz.dat", IOUtil.getRelativeFilename("/var/data/", "/var/data/stuff/xyz.dat"));
	    assertEquals("stuff/xyz.dat", IOUtil.getRelativeFilename("/var/data", "/var/data/stuff/xyz.dat"));
	    assertEquals("xyz.dat", IOUtil.getRelativeFilename("/var/data", "/var/data/xyz.dat"));
	    assertEquals("", IOUtil.getRelativeFilename("/var/data", "/var/data"));
	}
    
    @Test
    public void testCurrentPlatformCanonical() throws IOException
    {
    	File here = new File(".").getCanonicalFile();
	    File parent = new File("..").getCanonicalFile();
    	
	    System.out.println(parent.getAbsolutePath());
    	System.out.println(here.getAbsolutePath());
	    assertEquals(here.getName(), IOUtil.getRelativeFilename(parent, here));
    }
    
}
