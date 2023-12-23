import java.util.LinkedList;
import java.util.Queue;

/**
 * This class creates a message queue which is used to process messages received from a socket.
 */
public class MessageQueue {

    /**
     * Queue to process messages from socket.
     */
    private static final Queue<MessageDetails> messageDetailsQueue = new LinkedList<>();

    /**
     * This method is used to add a message to the message queue.
     * @param messageDetails - message to be added.
     */
    public static synchronized void addMessageToMessageQueue(MessageDetails messageDetails) {
        messageDetailsQueue.add(messageDetails);
    }

    /**
     * This method is used to get a message from the message queue.
     * @return message added in the queue, or null if the queue is empty.
     */
    public static synchronized MessageDetails getMessageFromQueue() {
        return messageDetailsQueue.poll();
    }
}
