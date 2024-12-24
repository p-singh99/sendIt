package common.utilities;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Network {

    /*
    * Get the local IP address of Host Machine
    * @return List of Interface addresses
    * */
    public static List<String> getLocalAddress() {
        List<String> localAddresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp()) continue;
                if (networkInterface.isLoopback()) continue;

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.isLinkLocalAddress()) continue;

                    if (address.isSiteLocalAddress()) {
                        localAddresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error getting system network interfaces... " + e);
        }

        return localAddresses;
    }

    public static String getSubnet(String address) {
        return address.substring(0, address.lastIndexOf('.'));
    }
}
