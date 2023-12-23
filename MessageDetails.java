/**
 * This class is used to handle message and its metadata.
 */
public class MessageDetails {
    // Message sent/received
    private Message message;
    // peerID of sender
    private String fromPeerID;

    /**
     * Constructor creating Message Details instance and setting required fields.
     */
    public MessageDetails() {
        this.message = new Message();
        this.fromPeerID = null;
    }

    /**
     * This method is used to get the message.
     * @return message.
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * This method is used to set the message.
     * @param message - The message to set.
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * This method is used to get the peerID of the sender.
     * @return peerID.
     */
    public String getFromPeerID() {
        return this.fromPeerID;
    }

    /**
     * This method is used to set the peerID of the sender.
     * @param fromPeerID - The peerID to set.
     */
    public void setFromPeerID(String fromPeerID) {
        this.fromPeerID = fromPeerID;
    }
}
