import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * This class is used to generate log files to write messages into.
 */
public class LogHelper {
    // Handler to write in the specified file
    private FileHandler fileHandler;

    // Instance used to log messages
    private static final Logger log = Logger.getLogger(LogHelper.class.getName());

    /**
     * This method is used to initialize logging configuration.
     * It creates a new log file and sets file handler to write messages into.
     * @param currentPeerID - peerID for which the log file needs to be created.
     */
    public void initializeLogger(String currentPeerID) {
        try {
            fileHandler = new FileHandler("log_peer_" + currentPeerID + ".log");
            fileHandler.setFormatter(new LogFormatter());
            log.addHandler(fileHandler);
            log.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Error setting up file handler for logger: " + e.getMessage());
        }
    }

    /**
     * This method is used to log a message in a log file and show it in console.
     * @param message - message to be logged and showed in console.
     */
    public static void logAndShowInConsole(String message) {
        log.info(message);
        System.out.println(LogFormatter.getFormattedMessage(message));
    }
}
