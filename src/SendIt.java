import client.Client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

class Main {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        String hostName = InetAddress.getLocalHost().getHostName();
//        System.out.println("Hostname: " + hostName);
//        System.out.println("IP: " + InetAddress.getByName(hostName).getHostAddress());
//        System.out.println("IP: " + InetAddress.getByName("Pawanjots-MacBook-Pro").getHostAddress());
        //        System.out.println(InetAddress.getLocalHost().getHostAddress());
//        System.out.println("App running...");
//        Client client = new Client();
//        client.scanPeers("192.168.0");
//        client.scanPeers("127.0.0");

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
//            System.out.println("Interface: " + networkInterface.getDisplayName());
            if (!networkInterface.isUp()) continue;
            if (networkInterface.isLoopback()) continue;

            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address.isLinkLocalAddress()) {
                    continue;
                }
                if (address.isSiteLocalAddress()) {
                    System.out.println("IP: " + address.getHostAddress());
                }
            }
        }
    }
}