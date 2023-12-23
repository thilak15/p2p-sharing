import java.util.Comparator;

/**
 * This class is used to compare download rates of peers so as to determine preferred neighbors after unchoking interval
 */
public class DownloadRateSorter implements Comparator<RemotePeerDetails> {

    private boolean firstInstanceGreater;

    /**
     * Empty constructor to create downloaded sorter object
     */
    public DownloadRateSorter() {
        this.firstInstanceGreater = true;
    }

    /**
     * Parameterized constructor to create downloaded sorter object
     */
    public DownloadRateSorter(boolean constructor) {
        this.firstInstanceGreater = constructor;
    }

    /**
     * This method is used to compare download rates of 2 peers based on their download rates
     * @param rm1 - first peer
     * @param rm2 - second peer
     * @return 1 - rm1 > rm2; -1 - rm1 < rm2; 0 - rm1 = rm2
     */
    public int compare(RemotePeerDetails rm1, RemotePeerDetails rm2) {
        if (rm1 == null) return (rm2 == null) ? 0 : 1;
        if (rm2 == null) return -1;

        int comparisonResult;
        if (rm1 instanceof Comparable) {
            comparisonResult = rm1.compareTo(rm2);
        } else {
            comparisonResult = rm1.toString().compareTo(rm2.toString());
        }

        return firstInstanceGreater ? comparisonResult : -comparisonResult;
    }
}
