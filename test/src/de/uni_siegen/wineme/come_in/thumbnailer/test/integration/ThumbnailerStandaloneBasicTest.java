package de.uni_siegen.wineme.come_in.thumbnailer.test.integration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import de.uni_siegen.wineme.come_in.thumbnailer.test.ThumbnailerFileTestDummy;


import uk.ac.lkl.common.util.testing.LabelledParameterized;

@RunWith(LabelledParameterized.class)
public class ThumbnailerStandaloneBasicTest extends ThumbnailerFileTestDummy
{
	private DefaultExecuteResultHandler resultHandler;
	private ExecuteWatchdog watchdog;


	public ThumbnailerStandaloneBasicTest(String name, File input)
	{
		super(input);
	}
	
	@Before
	public void setSize()
	{
		//setImageSize(160, 120, 0);
	}
	
	@Test
	public void generateThumbnail() throws Exception
	{
		create_thumbnail(inputFile);
	}

	@Parameters
	public static Collection<Object[]> listFiles()
	{
		return getFileList(TESTFILES_DIR);
	}
	
	public void _create_thumbnail(File input, File output) throws Exception
	{
		CommandLine cmd = new CommandLine("java");
		cmd.addArgument("-jar");
		cmd.addArgument("test/build/javathumbnailer-standalone.jar");
		
	//	cmd.addArgument("-size");
	//	cmd.addArgument(width + "x" + height);
		
		cmd.addArgument(input.getAbsolutePath());
		cmd.addArgument(output.getAbsolutePath());
		
System.out.println(cmd);
		
		startUp(cmd, new File(MY_DIR), 10, true);
	}
	
	
	  /**
	   * Wrapper function for commons-exec:
	   * Execute a Command as a background or blocking process.
	   * 
	   * @param cmd         Command to execute
	   * @param workingDir  Working directory
	   * @param timeout     Kill process after this time (in sec) (0: no timeout)
	   * @param blocking    Synchronous/blocking (true) or asynchronous/background startup (false).
	   * @return  An outputstream that contains the output of the process into stdout/stderr
	   * @throws ExecuteException Error during execution
	   * @throws IOException    File does not exist, and so could not be executed.
	   */
	  protected ByteArrayOutputStream startUp(CommandLine cmdLine, File workingDir, int timeout, boolean blocking) throws ExecuteException, IOException
	  {
	    Executor executor = new DefaultExecutor();
	    resultHandler = new DefaultExecuteResultHandler();
	    
	    if (timeout > 0)
	    {
	      watchdog = new ExecuteWatchdog(1000 * timeout);
	      executor.setWatchdog(watchdog);
	    }
	    
	    /* No live-streaming needed
	    PipedOutputStream os = new PipedOutputStream();
	    InputStream is = new PipedInputStream(os);
	    executor.setStreamHandler(new PumpStreamHandler(os));
	    */
	    
	    ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
	    executor.setStreamHandler(new PumpStreamHandler(os));
	    
	    executor.setWorkingDirectory(workingDir);
	    executor.execute(cmdLine, resultHandler);
	    
	    if (blocking)
	    {
	      while (!resultHandler.hasResult()) {
	        try {
	          resultHandler.waitFor();
	        } catch (InterruptedException e) { }
	      }
	    }
	    
	    return os;
	  }
}
