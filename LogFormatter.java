import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class handles the formatting of messages in log files.
 */
public class LogFormatter extends Formatter {
    // Format in which date should be written
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    /**
     * This method is used to format the log record.
     * @param record - record to be formatted
     * @return - formatted log message
     */
    @Override
    public String format(LogRecord record) {
        return getFormattedMessage(record.getMessage());
    }

    /**
     * This method specifies the format in which log record should be written.
     * @param message - message to be formatted
     * @return formatted message
     */
    public static String getFormattedMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s: Peer %s%n", dateTimeFormatter.format(now), message);
    }
}
