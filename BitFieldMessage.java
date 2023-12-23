import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is used to store bitfield message of peers
 */
public class BitFieldMessage {

    //List of file pieces
    private FilePiece[] filePieces;
    //number of file pieces
    private int numberOfPieces;

    /**
     * This is a constructor to initialize bitfield message. It sets file size and file pieces according to values provided in configuration
     */
    public BitFieldMessage() {
    // Parsing the file size and piece size from the configuration
    double fileSize = Double.parseDouble(String.valueOf(CommonConfiguration.fileSize));
    double pieceSize = Double.parseDouble(String.valueOf(CommonConfiguration.pieceSize));

    // Calculating the number of pieces
    numberOfPieces = (int) Math.ceil(fileSize / pieceSize);

    // Initializing the array of FilePiece objects
    filePieces = new FilePiece[numberOfPieces];

    // Populating the filePieces array
    for (int i = 0; i < numberOfPieces; i++) {
        filePieces[i] = new FilePiece();
    }
}


    /**
     * This method is used to return the list of file pieces
     * @return filepieces - list of file pieces
     */
    public FilePiece[] getFilePieces() {
        return filePieces;
    }

    /**
     * This method is used to set the list of file pieces
     * @param filePieces - list of file pieces
     */
    public void setFilePieces(FilePiece[] filePieces) {
        this.filePieces = filePieces;
    }

    /**
     * This method is used to get the number of file pieces
     * @return numberOfPieces - number of file pieces
     */
    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    /**
     * This method is used to set the number of file pieces
     * @param numberOfPieces - number of file pieces
     */
    public void setNumberOfPieces(int numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    /**
     * This method is used to set if pieces are present of that file or not.
     * It accepts peerID which is set for a particular file piece and hasfile whether the file is present or not.
     * Used for initializing a bitfield message
     * @param peerId - ID of the peer from where the piece is found
     * @param hasFile - whether the file is present or not
     */
    public void setPieceDetails(String peerId, int hasFile) {
        for (FilePiece filePiece : filePieces) {
            filePiece.setIsPresent(hasFile == 1 ? 1 : 0);
            filePiece.setFromPeerID(peerId);
        }
    }

    /**
     * This method is used to convert bitfield message to byte array.
     * @return bitfield message converted into byte array
     */
    public byte[] getBytes() {
    int byteSize = (numberOfPieces + 7) / 8; // Calculate the size of byte array needed
    byte[] iP = new byte[byteSize]; // Initialize the byte array
    int tempInt = 0; // Temporary integer for building the byte
    int count = 0; // Counter for the byte array index

    for (int Cnt = 1; Cnt <= numberOfPieces; Cnt++) {
        tempInt = (tempInt << 1) | filePieces[Cnt - 1].getIsPresent(); // Shift and set the bit

        if (Cnt % 8 == 0 || Cnt == numberOfPieces) { // Check if a byte is completed or it's the last piece
            if (Cnt == numberOfPieces && numberOfPieces % 8 != 0) { // If it's the last piece and not a complete byte
                tempInt <<= 8 - (numberOfPieces % 8); // Left shift to align the last bits
            }
            iP[count++] = (byte) tempInt; // Store the built byte
            tempInt = 0; // Reset the temporary integer
        }
    }
    return iP;
}


    /**
 * This method is used to convert a byte array to bitfield message
 * @param bitField - bitfield message in byte array
 * @return - bitfield message object
 */
public static BitFieldMessage decodeMessage(byte[] bitField) {
    BitFieldMessage bitFieldMessage = new BitFieldMessage();
    int totalPieces = bitFieldMessage.getNumberOfPieces();
    FilePiece[] filePieces = bitFieldMessage.getFilePieces();

    for (int i = 0; i < bitField.length; i++) {
        for (int count = 7; count >= 0; count--) {
            int pieceIndex = i * 8 + 7 - count;
            if (pieceIndex < totalPieces) {
                int bitValue = (bitField[i] >> count) & 1;
                filePieces[pieceIndex].setIsPresent(bitValue);
            }
        }
    }

    return bitFieldMessage;
}


    /**
     * This method is used to get number of file pieces present in a peer
     * @return number of file pieces present
     */
    public int getNumberOfPiecesPresent() {
        int count = 0;
        for (FilePiece filePiece : filePieces) {
            if (filePiece.getIsPresent() == 1) {
                count++;
            }
        }

        return count;
    }

    /**
     * This method is used to check if all the pieces of a file have been downloaded
     * @return true - file has been downloaded; false - file has not been downloaded
     */
    public boolean isFileDownloadComplete() {
        boolean isFileDownloaded = true;
        for (FilePiece filePiece : filePieces) {
            if (filePiece.getIsPresent() == 0) {
                isFileDownloaded = false;
                break;
            }
        }

        return isFileDownloaded;
    }

    /**
     * This method returns the index of first piece which is present in remote peer and not in current peer
     * @param bitFieldMessage - bitfield of remote peer
     * @return index of first piece which is present in remote peer and not in current peer
     */
    public synchronized int getInterestingPieceIndex(BitFieldMessage bitFieldMessage) {
        int numberOfPieces = bitFieldMessage.getNumberOfPieces();
        int interestingPiece = -1;

        for (int i = 0; i < numberOfPieces; i++) {
            if (bitFieldMessage.getFilePieces()[i].getIsPresent() == 1
                    && this.getFilePieces()[i].getIsPresent() == 0) {
                interestingPiece = i;
                break;
            }
        }

        return interestingPiece;
    }

    /**
     * This method returns the index of first piece which is present in remote peer and not in current peer
     * @param bitFieldMessage - bitfield of remote peer
     * @return index of first piece which is present in remote peer and not in current peer
     */
    public synchronized int getFirstDifferentPieceIndex(BitFieldMessage bitFieldMessage) {
    int minPieces = Math.min(numberOfPieces, bitFieldMessage.getNumberOfPieces());
    int pieceIndex = -1;

    for (int i = 0; i < minPieces; i++) {
        if (filePieces[i].getIsPresent() == 0 && bitFieldMessage.getFilePieces()[i].getIsPresent() == 1) {
            pieceIndex = i;
            break;
        }
    }

    return pieceIndex;
}


    /**
     * This method is used to update current peer bitfield with file piece.
     * If complete file is downloaded it updates peerinfo.cfg to set hasfile value of current peer to 1
     * @param peerID - The peer from where the piece is received
     * @param filePiece - The file piece received
     */
    public void updateBitFieldInformation(String peerID, FilePiece filePiece) {
        int pieceIndex = filePiece.getPieceIndex();
        try {
            if (isPieceAlreadyPresent(pieceIndex)) {
                logAndShowInConsole(peerID + " Piece already received");
            } else {
                String fileName = CommonConfiguration.fileName;

                File file = new File(peerProcess.currentPeerID, fileName);
                int offSet = pieceIndex * CommonConfiguration.pieceSize;
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                byte[] pieceToWrite = filePiece.getContent();
                randomAccessFile.seek(offSet);
                randomAccessFile.write(pieceToWrite);

                filePieces[pieceIndex].setIsPresent(1);
                filePieces[pieceIndex].setFromPeerID(peerID);
                randomAccessFile.close();
                logAndShowInConsole(peerProcess.currentPeerID + " has downloaded the PIECE " + pieceIndex
                        + " from Peer " + peerID + ". Now the number of pieces it has is "
                        + peerProcess.bitFieldMessage.getNumberOfPiecesPresent());

                if (peerProcess.bitFieldMessage.isFileDownloadComplete()) {
                    //update file download details
                    peerProcess.remotePeerDetailsMap.get(peerID).setIsInterested(0);
                    peerProcess.remotePeerDetailsMap.get(peerID).setIsComplete(1);
                    peerProcess.remotePeerDetailsMap.get(peerID).setIsChoked(0);
                    peerProcess.remotePeerDetailsMap.get(peerID).updatePeerDetails(peerProcess.currentPeerID, 1);
                    logAndShowInConsole(peerProcess.currentPeerID + " has DOWNLOADED the complete file.");
                }
            }
        } catch (IOException e) {
            logAndShowInConsole(peerProcess.currentPeerID + " EROR in updating bitfield " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method is used to check if a piece is already present.
     * @param pieceIndex - index of the piece to be checked
     * @return true - piece is present; false - piece is not present
     */
    private boolean isPieceAlreadyPresent(int pieceIndex) {
        return peerProcess.bitFieldMessage.getFilePieces()[pieceIndex].getIsPresent() == 1;
    }

    /**
     * This method is used to log a message in a log file and show it in console
     * @param message - message to be logged and showed in console
     */
    private static void logAndShowInConsole(String message) {
        LogHelper.logAndShowInConsole(message);
    }
}
