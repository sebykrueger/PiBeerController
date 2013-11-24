package beer.util;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
    	String message = 
    			String.format("%-50s | %-20s | %s | %s\n", record.getSourceClassName(), record.getSourceMethodName(), new Date(record.getMillis()), record.getMessage());
        return  message;
    }

}