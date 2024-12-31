import constants.PeerRequest;

import java.io.*;
import java.net.*;

public class Peer {
    static int PORT = 8080;
    static int BUFFER_SIZE = 4096;

    private PeerDiscovery peerDiscovery;

    public Peer() {
        this.peerDiscovery = new PeerDiscovery(PORT);
    }

    /*
    * Start listening for incoming connections
    * */
    public void start() {
        new Thread(() -> {
            System.out.println("Peer listening at port: " + PORT);
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                while (true) {
                    try {
                        Socket soc = serverSocket.accept();
                        System.out.println("Starting Peer Handler");
                        new Thread(new PeerHandler(soc)).start();
                    } catch (Exception e) {

                    }
                }
            } catch (IOException e) {
                System.err.println("Could not listen at port: " + PORT);
            }
        }).start();
    }

    public void sendFile(String file, String peer) {
        System.out.println("File is: " + file);

        // Open the file for reading
        File f;
        byte[] fileData;
        FileInputStream fis;
        BufferedInputStream bis;

        try (
            Socket soc = new Socket()
        ) {
            f = new File(file);
            fileData = new byte[BUFFER_SIZE];

            System.out.println("File length is: " + f.length());
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);

            try {
                if (peerDiscovery.isReachable(peer)) {

                    // Transfer data to peer
                    InetAddress host = InetAddress.getByName(peer);
                    soc.connect(new InetSocketAddress(host, PORT), 50);
                    PrintWriter writer = new PrintWriter(soc.getOutputStream(), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                    OutputStream os = soc.getOutputStream();

                    writer.println(PeerRequest.TRANSFER + " " + f.getName() + " " + f.length());
                    String peerResponse = reader.readLine();

                    if (peerResponse.equals(PeerRequest.OK)) {
                        int bytesRead;
                        while ((bytesRead = bis.read(fileData, 0, BUFFER_SIZE)) != -1) {
                            os.write(fileData, 0, bytesRead);
                        }
                        os.flush();

                        peerResponse = reader.readLine();
                        if (peerResponse.equals(PeerRequest.OK))
                            System.out.println("Transfer complete!");
                        else
                            System.err.println("Errors encountered during file transfer");
                    } else {
                        System.out.println("Could not transfer file, rejected by the other device");
                    }
                }
            } catch (UnknownHostException e) {
                System.err.println("Unknown device - " + e);
            } catch (IOException e) {
                System.out.println("Could not transfer file, rejected by the other device");
            }
        } catch (IOException e) {
            System.err.println("Error opening file - " + e);
        }
    }

    public void scanPeers() {
        peerDiscovery.scanPeers();
    }
}
