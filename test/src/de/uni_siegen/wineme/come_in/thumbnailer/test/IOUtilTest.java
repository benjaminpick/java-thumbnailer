package de.uni_siegen.wineme.come_in.thumbnailer.test;

import de.uni_siegen.wineme.come_in.thumbnailer.util.IOUtil;
import junit.framework.TestCase;

public class IOUtilTest extends TestCase {
	public void testGetRelativePathsUnix() {
	    assertEquals("stuff/xyz.dat", IOUtil.getRelativeFilename("/var/data/", "/var/data/stuff/xyz.dat"));
	}
}
