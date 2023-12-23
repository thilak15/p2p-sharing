### P2P File Sharing - Implementing BitTorrent Protocol
Develop a Peer-to-Peer (P2P) file-sharing application using the BitTorrent protocol, programmed in Java.
Utilize a choking and unchoking mechanism for file distribution among peers.
Base all operations on the reliable TCP (Transmission Control Protocol).
### Protocol Overview:
The project leverages the TCP protocol for peer connections in file sharing.
Peers initiate sharing by exchanging a handshake message, comprising a header, zero bits, and a peer ID.
Subsequent data messages include message length, type, and payload, encompassing different payload types like piece and bitfield, and message types such as have, bitfield, choke, unchoke, interested, not interested, request, and piece.
### Operational Workflow:
Peers are activated using startRemotePeers in the sequence outlined in the PeerInfo configuration file, with each peerProcess initiated by a unique peer ID.
Newly active peers establish TCP connections with already active peers in the network.
Peers also access a common configuration file detailing shared file information, file size, intervals for choking/unchooking, and preferred neighbor count.
The PeerInfo file indicates complete file possession by peers using 0 or 1 bits. When a peer acquires the entire file, its corresponding bit in PeerInfo.cfg is updated to 1.
The initially started peer listens on its designated port, as per PeerInfo file, due to the absence of other peers.
A log is maintained for each peer, tracking events like TCP connections with other peers, changes in preferred or optimistically unchoked neighbors, choke/unchoke statuses, receipt of have/interested/not interested messages, and completion of piece or file downloads.
### File Sharing Process:
When a peer seeks a file, it searches using the filename or keywords, initiating with a hop count of 1.

The search request propagates through the overlay network to peers within the current hop count radius, expiring after a set hop count duration. Repeated requests are not processed.
Peers possessing the requested file respond to the search request originator. The initiating peer either uses the response directly or passes it back through the chain.
The requesting peer compiles responses until the request's expiry, disregarding any subsequent replies.
Upon matching a response with the desired filename and piece index, the requester establishes a TCP connection with the responding peer, transferring the file to its directory. The connection concludes post-transfer.
Unsuccessful searches prompt a hop count increment and a new search request, repeating until success or surpassing a predefined maximum hop count.
### Protocol Termination:
The protocol terminates if the node count surpasses the maximum hop count.
A departing peer with a single neighbor simply ends its TCP connection.
With multiple neighbors, it designates one neighbor as a common connection for others, barring pre-existing connections, then terminates all its connections and file transfers.

### Project Details
Project Members:
1. Thilak Reddy Daggula (UFID: 6314-1289)
2. DevangKale (UFID: 3340-9661)
3. Ayush shrivastava (UFID: 4121-8133)
4. Rutwik saraf (UFID:5079-5872)

Steps to Run the project:
1. Add tree.jpg file in 1001 folder and add Common.cfg and PeerInfo.cfg as needed (here we have changed the peerinfo.cfg to run it locally)
2. Run javac peerProcess.java command to compile the code
2. Then manually start all the peers using the command java peerProcess peer_number


### Demo

https://uflorida-my.sharepoint.com/:v:/g/personal/t_daggula_ufl_edu/EafcW7Jbo9JBhgpIT8888ZYBPzC3eTHUfBx_ggyDXgGjiA?e=StiTcs&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D
