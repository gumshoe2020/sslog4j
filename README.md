# sslog4j
Simple &amp; Stupid Logger for Java

Use:

protected Logger fLogger = new Logger();
in the global variable area of a servlet.

then

fLogger.debug("This log file is located at: " + fLogger.getLogFilePath());

NOTE: Javadoc is not up-to-date, as of 2017.04.08