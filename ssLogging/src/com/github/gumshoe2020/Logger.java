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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Logger {

	private static String FILE_NAME_PATH = "";
	private static Path path = null;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private static List<String> logLines = null;
	private LogLevel llev = null;
	private static String fileName = "";
	private static PrintWriter out;
	private static boolean packages = false;

	/**
	 * This constructor uses basic string representations
	 * of system file paths and file name to interpret where
	 *  the log is to be placed and named.
	 * @param filePath Examples: "C:\\temp\\" or "/var/log/temp/"
	 * @param fileName  The name of the log file; it will automatically
	 * have a .log extension appended.
	 */
	public Logger(Path filePath, String loggerFileName, LogLevel ll){
		fileName = loggerFileName;
		path = filePath;
		FILE_NAME_PATH = filePath + File.separator + fileName + ".log";
		llev = ll;
		logLines = Collections.synchronizedList(new ArrayList<String>());
		writeLog("/******************************************************************************");
		writeLog("*      " + fileName + " log");
		writeLog("*******************************************************************************/");
	}
	
	public Logger(Path filePath, String loggerFileName){
		fileName = loggerFileName;
		path = filePath;
		FILE_NAME_PATH = filePath + File.separator + fileName + ".log";
		llev = LogLevel.DEBUG;
		logLines = Collections.synchronizedList(new ArrayList<String>());
		writeLog("/******************************************************************************");
		writeLog("*      " + fileName + " log");
		writeLog("*******************************************************************************/");
	}
	
	public Logger(String loggerFileName){
		fileName = loggerFileName;
		path = Paths.get("D:\\temp\\");
		FILE_NAME_PATH = "D:\\temp\\" + fileName + ".log";
		llev = LogLevel.DEBUG;
		logLines = Collections.synchronizedList(new ArrayList<String>());
		writeLog("/******************************************************************************");
		writeLog("*      " + fileName + " log");
		writeLog("*******************************************************************************/");
	}
	
	public Logger(){
		String s = String.valueOf(Thread.currentThread().getStackTrace()[2]);
		fileName = s.substring((s.indexOf("(")+1), s.indexOf(":"));
		path = Paths.get("D:\\temp\\");
		FILE_NAME_PATH = "D:\\temp\\" + fileName + ".log";
		llev = LogLevel.DEBUG;
		logLines = Collections.synchronizedList(new ArrayList<String>());
		writeLog("/******************************************************************************");
		writeLog("*      " + fileName + " log");
		writeLog("*******************************************************************************/");
	}
	
	public Logger setPackagesVisible(){
		packages = true;
		return this;
	}

	private static void readLog() {
		try {
			Path path = Paths.get(FILE_NAME_PATH);
			if(Files.exists(path)) logLines = Files.readAllLines(path, ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeLog(String message) {
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
				Path path = Paths.get(FILE_NAME_PATH);
				Files.write(path, logLines, ENCODING);
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
	public void severe(String message){
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
	public void critical(String message){
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
			case SEVERE:
				break;
			}
		}
	}
	/**
	 * The error logger will log messages for
	 * ERROR, INFO, and DEBUG LogLevels.
	 * @param message A string representation of the message
	 */
	public void error(String message){
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
			case CRITICAL:
				break;
			case SEVERE:
				break;
			}
		}
	}
	/**
	 * The info logger will log messages for INFO and DEBUG LogLevels.
	 * @param message A string representation of the message
	 */
	public void info(String message){
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
			case ERROR:
				break;
			case CRITICAL:
				break;
			case SEVERE:
				break;
			}
		}
	}
	/**
	 * The debug logger will log messages only for the DEBUG LogLevel.
	 * @param message A string representation of the message
	 */
	public void debug(String message){
		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		synchronized(this){
			switch(llev){
			case DEBUG: 
				readLog();
				Date dt = new Date();
				writeLog(pad("[" + callerClassName + " DEBUG] ") + dt + " : " + message);
				break;
			case INFO:
				break;
			case ERROR:
				break;
			case CRITICAL:
				break;
			case SEVERE:
				break;
			}
		}
	}
	
	public static String pad(String string) {
		if(packages)
			return ("                                                                  " + string).substring(string.length());
		else{
			string = "[" + string.substring(string.lastIndexOf('.')+1);
			return ("                                 " + string).substring(string.length());
		}
		
	}
}
