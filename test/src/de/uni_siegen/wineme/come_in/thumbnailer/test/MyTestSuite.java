package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.io.File;

import junit.framework.TestSuite;

public class MyTestSuite extends TestSuite {

	protected static final String MY_DIR = "plugin"+ File.separatorChar;
	public static final String TESTFILES_DIR = MY_DIR+"test" + File.separatorChar + "testfiles" + File.separatorChar;

	
	protected static String getDisplayname(File input) {
		return input.getName().replaceAll("[^a-zA-Z0-9]", "_");
	}
}
