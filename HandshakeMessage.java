import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * This class is used to handle handshake message information.
 */
public class HandshakeMessage {
    private byte[] headerInBytes = new byte[MessageConstants.HANDSHAKE_HEADER_LENGTH];
    private byte[] peerIDInBytes = new byte[MessageConstants.HANDSHAKE_PEERID_LENGTH];
    private byte[] zeroBits = new byte[MessageConstants.HANDSHAKE_ZEROBITS_LENGTH];
    private String header;
    private String peerID;

    /**
     * Empty constructor to create handshake object.
     */
    public HandshakeMessage() {
    }

    /**
     * Constructor to create handshake with the header and peerID.
     * It sets headerInBytes, peerIDInBytes, zeroBits, header and peerID fields.
     * @param header - Handshake header.
     * @param peerID - The peerID from where the handshake message is sent.
     */
    public HandshakeMessage(String header, String peerID) throws Exception {
        setHeader(header);
        setPeerID(peerID);
        Arrays.fill(this.zeroBits, (byte) 0);
    }

    /**
     * This method is used to convert handshakeMessage to byte array.
     * @param handshakeMessage - HandshakeMessage to be converted.
     * @return byte array of the handshakeMessage.
     */
    public static byte[] convertHandshakeMessageToBytes(HandshakeMessage handshakeMessage) throws Exception {
        validateHandshakeMessage(handshakeMessage);
        byte[] handshakeMessageInBytes = new byte[MessageConstants.HANDSHAKE_MESSAGE_LENGTH];
        System.arraycopy(handshakeMessage.getHeaderInBytes(), 0, handshakeMessageInBytes, 0, handshakeMessage.getHeaderInBytes().length);
        System.arraycopy(handshakeMessage.getZeroBits(), 0, handshakeMessageInBytes, MessageConstants.HANDSHAKE_HEADER_LENGTH, handshakeMessage.getZeroBits().length);
        System.arraycopy(handshakeMessage.getPeerIDInBytes(), 0, handshakeMessageInBytes, MessageConstants.HANDSHAKE_HEADER_LENGTH + MessageConstants.HANDSHAKE_ZEROBITS_LENGTH, handshakeMessage.getPeerIDInBytes().length);
        return handshakeMessageInBytes;
    }

    /**
     * This method is used to convert byte array to handshakeMessage.
     * @param handShakeMessage - byte array of HandshakeMessage.
     * @return - Handshake message object.
     */
    public static HandshakeMessage convertBytesToHandshakeMessage(byte[] handShakeMessage) throws Exception {
        if (handShakeMessage.length != MessageConstants.HANDSHAKE_MESSAGE_LENGTH) {
            throw new Exception("Invalid Handshake message length");
        }
        HandshakeMessage message = new HandshakeMessage();
        byte[] messageHeader = Arrays.copyOfRange(handShakeMessage, 0, MessageConstants.HANDSHAKE_HEADER_LENGTH);
        byte[] messagePeerID = Arrays.copyOfRange(handShakeMessage, MessageConstants.HANDSHAKE_HEADER_LENGTH + MessageConstants.HANDSHAKE_ZEROBITS_LENGTH, MessageConstants.HANDSHAKE_MESSAGE_LENGTH);
        message.setHeaderFromBytes(messageHeader);
        message.setPeerIDFromBytes(messagePeerID);
        return message;
    }

    /**
     * This method is used to set peerID from byte array.
     * @param messagePeerID - byte array of peerID.
     */
    public void setPeerIDFromBytes(byte[] messagePeerID) {
        try {
            this.peerID = new String(messagePeerID, MessageConstants.DEFAULT_CHARSET).trim();
            this.peerIDInBytes = messagePeerID;
        } catch (UnsupportedEncodingException e) {
            logAndShowInConsole(e.getMessage());
        }
    }

    /**
     * This message is used to set handshake header from byte array.
     * @param messageHeader - handshake header in bytes.
     */
    public void setHeaderFromBytes(byte[] messageHeader) {
        try {
            this.header = new String(messageHeader, MessageConstants.DEFAULT_CHARSET).trim();
            this.headerInBytes = messageHeader;
        } catch (UnsupportedEncodingException e) {
            logAndShowInConsole(e.getMessage());
        }
    }

    // Other getter and setter methods remain unchanged

    private static void validateHandshakeMessage(HandshakeMessage handshakeMessage) throws Exception {
        if (handshakeMessage.getHeaderInBytes() == null || handshakeMessage.getHeaderInBytes().length > MessageConstants.HANDSHAKE_HEADER_LENGTH || handshakeMessage.getHeaderInBytes().length == 0)
            throw new Exception("Invalid Handshake Message Header");
        if (handshakeMessage.getZeroBits() == null || handshakeMessage.getZeroBits().length > MessageConstants.HANDSHAKE_ZEROBITS_LENGTH || handshakeMessage.getZeroBits().length == 0)
            throw new Exception("Invalid Handshake Message Zero Bits");
        if (handshakeMessage.getPeerIDInBytes() == null || handshakeMessage.getPeerIDInBytes().length > MessageConstants.HANDSHAKE_PEERID_LENGTH || handshakeMessage.getPeerIDInBytes().length == 0)
            throw new Exception("Invalid Handshake Message Peer ID");
    }

    /**
     * This method is used to log a message in a log file and show it in console.
     * @param message - message to be logged and showed in console.
     */
    private static void logAndShowInConsole(String message) {
        LogHelper.logAndShowInConsole(message);
    }

    // Getters and setters for other fields follow the same pattern as the ones provided
}
