import constants.PeerRequest;
import utilities.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class PeerDiscovery {

    private static int PORT;

    public PeerDiscovery(int port) {
        this.PORT = port;
    }

    /*
     * Peer discovery, scan all WiFi IPs
     * */
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

    public boolean isReachable(String peer) throws IOException {
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

            if (hostResponse != null) {
                reachable = hostResponse.equals(PeerRequest.HELLO);
                if (reachable) System.out.println("- " + reader.readLine() + " @ " + peer);

                return reachable;
            }
        }

        return false;
    }
}
