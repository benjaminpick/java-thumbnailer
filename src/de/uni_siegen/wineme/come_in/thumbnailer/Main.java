/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2011  Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */

package de.uni_siegen.wineme.come_in.thumbnailer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODExcelConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODHtmlConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODPowerpointConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODWordConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.NativeImageThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.OpenOfficeThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.PDFBoxThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.ScratchThumbnailer;

/**
 * Little Command-line Application to illustrate the usage of this library.
 * 
 * @author Benjamin
 *
 */
public class Main {
	protected static Logger mLog = Logger.getLogger(Main.class);
	private static final String LOG4J_CONFIG_FILE = "conf/javathumbnailer.log4j.properties";
	
	public static void main(String[] params) throws Exception
	{
		if (params.length == 0)
		{
			explainUsage();
			System.exit(-1);
		}
		initLogging();
		
		ThumbnailerManager thumbnailer = new ThumbnailerManager();
	
		loadExistingThumbnailers(thumbnailer);
		
		thumbnailer.setImageSize(160, 120, 0);
		thumbnailer.setThumbnailFolder("thumbs/");
		
		File in = new File(params[0]);
		File out = null;
		if (params.length > 1)
			out = new File(params[1]);
		
		if (out == null)
			out = thumbnailer.createThumbnail(in);
		else
			thumbnailer.generateThumbnail(in, out);
		
		System.out.println("SUCCESS: Thumbnail created:\n" + out.getAbsolutePath());
	}

	private static void explainUsage() {
		System.out.println("JavaThumbnailer");
		System.out.println("===============");
		System.out.println("");
		System.out.println("Usage: java -jar javathumbnailer-standalone.jar inputfile [outputfile]");
		
	}

	protected static void loadExistingThumbnailers(
			ThumbnailerManager thumbnailer) {

		if (classExists("de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.NativeImageThumbnailer"))
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
	
	protected static void initLogging() throws IOException
	{
		System.setProperty("log4j.configuration", LOG4J_CONFIG_FILE);

		File logConfigFile = new File(LOG4J_CONFIG_FILE);
		if (!logConfigFile.exists())
		{
			// Extract config properties from jar
			InputStream in = Main.class.getResourceAsStream("/" + LOG4J_CONFIG_FILE);
			if (in == null)
			{
				System.err.println("Packaging error: can't find logging configuration inside jar. (Neither can I find the config file on the file system: " + logConfigFile.getAbsolutePath() + ")");
				System.exit(1);
			}

			OutputStream out = null;
			try {
				out = FileUtils.openOutputStream(logConfigFile);
				IOUtils.copy(in, out);
			} finally { try { if (in != null) in.close(); } finally { if (out != null) out.close(); } }
		}

		PropertyConfigurator.configureAndWatch(logConfigFile.getAbsolutePath(), 10 * 1000);
		mLog.info("Logging initialized");
	}
	
	public static boolean classExists(String qualifiedClassname)
	{
		try {
			Class.forName(qualifiedClassname);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
