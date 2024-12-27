package client;

import common.constants.PeerRequest;
import common.utilities.Network;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Client {

    static int PORT = 8080;
    static int BUFFER_SIZE = 4096;

    public void scanPeers() {
        System.out.println("Scanning available devices ...");
        List<String> localAddress = Network.getLocalAddress();
        String subnet = Network.getSubnet(localAddress.getFirst());

        for (int i = 0; i < 255; i++) {
            String host = subnet + "." + i;
            if (!localAddress.contains(host)) {
                try {
                    isReachable(host);
                } catch (Exception e) {

                }
            }
        }

        System.out.println("Scan complete");
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
                if (isReachable(peer)) {

                    // Transfer data to Peer
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

    private boolean isReachable(String peer) throws IOException {
        InetAddress host = InetAddress.getByName(peer);
        boolean reachable;

        try (
                final Socket soc = new Socket()
        ) {
            soc.connect(new InetSocketAddress(host, PORT), 50);
            PrintWriter sendData = new PrintWriter(soc.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            sendData.println(PeerRequest.HELLO);
            String hostResponse = reader.readLine();
            reachable = hostResponse.equals(PeerRequest.HELLO);

            if (reachable) System.out.println("- " + reader.readLine() + " @ " + peer);
        }

        return reachable;
    }
}
