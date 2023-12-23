/**
 * This class is used to handle file piece information
 */
public class FilePiece {
    private int isPresent;  // Indicates if piece is present
    private String fromPeerID;  // Peer ID from where piece is retrieved
    private byte[] content;  // Content of the piece
    private int pieceIndex;  // Index of the piece in the list of pieces

    /**
     * Constructor to initialize file piece information
     */
    public FilePiece() {
        this.content = new byte[CommonConfiguration.pieceSize];
        this.pieceIndex = -1;
        this.isPresent = 0;
        this.fromPeerID = null;
    }

    // Getter and setter methods remain unchanged

    /**
     * This method is used to convert a file piece byte array to FilePiece object
     * @param payloadInBytes - byte array of file piece
     * @return FilePiece object
     */
    public static FilePiece convertByteArrayToFilePiece(byte[] payloadInBytes) {
        FilePiece filePiece = new FilePiece();
        filePiece.setPieceIndex(PeerProcessUtils.convertByteArrayToInt(
            Arrays.copyOfRange(payloadInBytes, 0, MessageConstants.PIECE_INDEX_LENGTH)
        ));
        filePiece.setContent(Arrays.copyOfRange(
            payloadInBytes, MessageConstants.PIECE_INDEX_LENGTH, payloadInBytes.length
        ));
        return filePiece;
    }
}
