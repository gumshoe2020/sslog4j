package com.github.gumshoe2020;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * https://github.com/gumshoe2020/sslog4j
 * 
 * @author Gumshoe
 */
public class Logger {

	private String FILE_NAME_PATH = "";
	private Path path = null;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private List<String> logLines = null;
	private LogLevel llev = null;
	private String fileName = "gumshoeLog";
	private PrintWriter out;
	private boolean packages = false;
	private boolean first = true;

	/**
	 * This constructor uses basic string representations
	 * of system file paths and file name to interpret where
	 *  the log is to be placed and named.
	 * @param filePath Examples: "C:\\temp\\" or "/var/log/temp/"; 
	 * defaults to the Java project directory
	 * @param fileName The name of the log file; it will automatically
	 * have a .log extension appended.  Defaults to the caller's class name.
	 * @param LogLevel The minimum log level of messages to be logged.
	 */
	public Logger(Path filePath, String loggerFileName, LogLevel ll){
		fileName = loggerFileName;
		path = filePath;
		FILE_NAME_PATH = filePath + File.separator + fileName + ".log";
		llev = ll;
		logLines = new CopyOnWriteArrayList<String>();
	}
	/**
	 * This constructor uses basic string representations
	 * of system file paths and file name to interpret where
	 *  the log is to be placed and named.
	 * @param filePath Examples: "C:\\temp\\" or "/var/log/temp/"; 
	 * defaults to the Java project directory
	 * @param fileName The name of the log file; it will automatically
	 * have a .log extension appended.  Defaults to the caller's class name.
	 * @param LogLevel The minimum log level of messages to be logged.
	 */
	public Logger(Path filePath, String loggerFileName){
		this(filePath, loggerFileName, LogLevel.DEBUG);
	}
	/**
	 * This constructor uses basic string representations
	 * of system file paths and file name to interpret where
	 *  the log is to be placed and named.
	 * @param filePath Examples: "C:\\temp\\" or "/var/log/temp/"; 
	 * defaults to the Java project directory
	 * @param fileName The name of the log file; it will automatically
	 * have a .log extension appended.  Defaults to the caller's class name.
	 * @param LogLevel The minimum log level of messages to be logged.
	 */
	public Logger(Path logRootPath, LogLevel ll){
		this(logRootPath, "tempLog",ll);
		String s = String.valueOf(Thread.currentThread().getStackTrace()[2]);
		fileName = s.substring((s.indexOf("(")+1), s.indexOf(":"));
		this.setLogFileName(fileName);
	}
	/**
	 * This constructor uses basic string representations
	 * of system file paths and file name to interpret where
	 *  the log is to be placed and named.
	 * @param filePath Examples: "C:\\temp\\" or "/var/log/temp/"; 
	 * defaults to the Java project directory
	 * @param fileName The name of the log file; it will automatically
	 * have a .log extension appended.  Defaults to the caller's class name.
	 * @param LogLevel The minimum log level of messages to be logged.
	 */
	public Logger(String loggerFileName){
		this(Paths.get(System.getProperty("user.dir") + File.separator + "tempLogs"), loggerFileName, LogLevel.DEBUG);
		
	}
	/**
	 * This constructor uses basic string representations
	 * of system file paths and file name to interpret where
	 *  the log is to be placed and named.
	 * @param filePath Examples: "C:\\temp\\" or "/var/log/temp/"; 
	 * defaults to the Java project directory
	 * @param fileName The name of the log file; it will automatically
	 * have a .log extension appended.  Defaults to the caller's class name.
	 * @param LogLevel The minimum log level of messages to be logged.
	 */
	public Logger(LogLevel ll){
		this(Paths.get(System.getProperty("user.dir") + File.separator + "tempLogs"), 
				String.valueOf(Thread.currentThread().getStackTrace()[2]).substring((
						String.valueOf(Thread.currentThread().getStackTrace()[2]).indexOf("(")+1), 
						String.valueOf(Thread.currentThread().getStackTrace()[2]).indexOf(":")), ll);
	}
	/**
	 * This constructor uses basic string representations
	 * of system file paths and file name to interpret where
	 *  the log is to be placed and named.
	 * @param filePath Examples: "C:\\temp\\" or "/var/log/temp/"; 
	 * defaults to the Java project directory
	 * @param fileName The name of the log file; it will automatically
	 * have a .log extension appended.  Defaults to the caller's class name.
	 * @param LogLevel The minimum log level of messages to be logged.
	 */
	public Logger(){
		this(Paths.get(System.getProperty("user.dir") + File.separator + "tempLogs"), 
				String.valueOf(Thread.currentThread().getStackTrace()[2]).substring((
						String.valueOf(Thread.currentThread().getStackTrace()[2]).indexOf("(")+1), 
						String.valueOf(Thread.currentThread().getStackTrace()[2]).indexOf(":")),
				LogLevel.DEBUG);
	}
	
	public Logger setPackagesVisible(){
		packages = true;
		return this;
	}
	
	public void setLogFilePath(Path logRootDirPath){
		this.path = logRootDirPath;
		this.first = true;
	}
	
	public String getLogFilePath(){
		return this.FILE_NAME_PATH;
	}
	
	public void setLogFileName(String logFileName){
		this.fileName = logFileName;
		FILE_NAME_PATH = path + File.separator + fileName + ".log";
		this.first = true;
	}
	
	private void printHeader(){
		writeLog("/******************************************************************************");
		writeLog("*      " + fileName + " log");
		writeLog("*******************************************************************************/");
		
	}

	private void readLog() {
		try {
			Path path = Paths.get(FILE_NAME_PATH);
			if(Files.exists(path)) logLines = Files.readAllLines(path, ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeLog(String message) {
		if(first){
			this.printHeader();
			this.first = false;
		}
		if(Files.exists(path)) {
			BufferedWriter bufWriter = null;
			try{
		        bufWriter =
		            Files.newBufferedWriter(
		            		Paths.get(FILE_NAME_PATH),
		                Charset.forName("UTF8"),
		                StandardOpenOption.WRITE, 
		                StandardOpenOption.APPEND,
		                StandardOpenOption.CREATE);
		        out = new PrintWriter(bufWriter, true);
			    out.println(message);
		    }catch(IOException e){
		        e.printStackTrace();
		    } finally {
		    	try {
					if(bufWriter!=null) bufWriter.close();
					bufWriter = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			    if(out!=null) out.close();
		    }
		} else {
			logLines.add(message);
			try {
				Files.createDirectory(path);
				Path xpath = Paths.get(FILE_NAME_PATH);
				Files.write(xpath, logLines, ENCODING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * The severe logger will log the message regardless of the
	 * LogLevel chosen.
	 * @param message A string representation of the message
	 */
	public synchronized void severe(String message){
		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		synchronized(this){
			Date dt = new Date();
			switch(llev){
			case DEBUG: 
				readLog();
				writeLog(pad("[" + callerClassName + " SEVERE] ") + dt + " : " + message);
				break;
			case INFO: 
				readLog();
				writeLog(pad("[" + callerClassName + " SEVERE] ") + dt + " : " + message);
				break;
			case ERROR:  
				readLog();
				writeLog(pad("[" + callerClassName + " SEVERE] ") + dt + " : " + message);
				break;
			case CRITICAL:
				readLog();  
				writeLog(pad("[" + callerClassName + " SEVERE] ") + dt + " : " + message);
				break;
			case SEVERE:  
				readLog();
				writeLog(pad("[" + callerClassName + " SEVERE] ") + dt + " : " + message);
				break;
			}
		}
	}
	/**
	 * The critical logger will log messages for CRITICAL,
	 * ERROR, INFO, and DEBUG LogLevels.
	 * @param message A string representation of the message
	 */
	public synchronized void critical(String message){
		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		synchronized(this){
			Date dt = new Date();
			switch(llev){
			case DEBUG:
				readLog();
				writeLog(pad("[" + callerClassName + " CRITICAL] ") + dt + " : " + message);
				break;
			case INFO:
				readLog();
				writeLog(pad("[" + callerClassName + " CRITICAL] ") + dt + " : " + message);
				break;
			case ERROR: 
				readLog();
				writeLog(pad("[" + callerClassName + " CRITICAL] ") + dt + " : " + message);
				break;
			case CRITICAL:
				readLog();
				writeLog(pad("[" + callerClassName + " CRITICAL] ") + dt + " : " + message);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * The error logger will log messages for
	 * ERROR, INFO, and DEBUG LogLevels.
	 * @param message A string representation of the message
	 */
	public synchronized void error(String message){
		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		synchronized(this){
			Date dt = new Date();
			switch(llev){
			case DEBUG:
				readLog();
				writeLog(pad("[" + callerClassName + " ERROR] ") +  dt + " : " + message);
				break;
			case INFO: 
				readLog();
				writeLog(pad("[" + callerClassName + " ERROR] ") +  dt + " : " + message);
				break;
			case ERROR: 
				readLog();
				writeLog(pad("[" + callerClassName + " ERROR] ") + dt + " : " + message);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * The info logger will log messages for INFO and DEBUG LogLevels.
	 * @param message A string representation of the message
	 */
	public synchronized void info(String message){
		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		synchronized(this){
			Date dt = new Date();
			switch(llev){
			case DEBUG: 
				readLog();
				writeLog(pad("[" + callerClassName + " INFO] ") + dt + " : " + message);
				break;
			case INFO: 
				readLog();
				writeLog(pad("[" + callerClassName + " INFO] ") + dt + " : " + message);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * The debug logger will log messages only for the DEBUG LogLevel.
	 * @param message A string representation of the message
	 */
	public synchronized void debug(String message){
		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		synchronized(this){
			switch(llev){
			case DEBUG: 
				readLog();
				Date dt = new Date();
				writeLog(pad("[" + callerClassName + " DEBUG] ") + dt + " : " + message);
				break;
			default:
				break;
			}
		}
	}
	
	public String pad(String string) {
		if(packages)
			return ("                                                                  " + string).substring(string.length());
		else{
			string = "[" + string.substring(string.lastIndexOf('.')+1);
			return ("                                 " + string).substring(string.length());
		}
		
	}
}