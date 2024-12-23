package client;

import common.PeerRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    static int PORT = 8080;

    public void run() throws IOException {
        InetAddress host = InetAddress.getByName("192.168.0.134");

        try (
            Socket soc = new Socket(host, PORT);
            PrintWriter sendData = new PrintWriter(soc.getOutputStream(), true);
            BufferedReader readData = new BufferedReader(new InputStreamReader(soc.getInputStream()));  
        ) {
            Scanner enterVal = new Scanner(System.in);
            while (true) {
                System.out.println("Send next request to server");
                sendData.println(enterVal.nextLine());
            }
        }
    }

    public void scanPeers(String subnet) {
        System.out.println("Scanning available devices ...");
        for (int i = 0; i < 255; i++) {
            String host = subnet + "." + i;
            try {
                if (isReachable(host)) {
                    System.out.println(host);
                }
            } catch (Exception e) {

            }
        }
    }

    private boolean isReachable(String peer) throws IOException {
        InetAddress host = InetAddress.getByName(peer);
        boolean reachable = false;

        try (
                final Socket soc = new Socket();
        ) {
            soc.connect(new InetSocketAddress(host, PORT), 100);
            PrintWriter sendData = new PrintWriter(soc.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            sendData.println(PeerRequest.HELLO);
            String hostResponse = reader.readLine();
            reachable = hostResponse.equals(PeerRequest.HELLO);
        }

        return reachable;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}
