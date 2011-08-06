package de.uni_siegen.wineme.come_in.thumbnailer.test;

import de.uni_siegen.wineme.come_in.thumbnailer.util.IOUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilTest {
    @Test
    public void testGetRelativePathsUnix() {
	    assertEquals("stuff/xyz.dat", IOUtil.getRelativeFilename("/var/data/", "/var/data/stuff/xyz.dat"));
	    assertEquals("stuff/xyz.dat", IOUtil.getRelativeFilename("/var/data", "/var/data/stuff/xyz.dat"));
	}
}
