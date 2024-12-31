package common.utilities.hostManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MacOSManager extends HostManager{

    @Override
    public String getHostName() {

        try {
            Process process = Runtime.getRuntime().exec("scutil --get ComputerName");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return  reader.readLine();
        } catch (IOException e) {
            System.err.println("Unable to get device name " + e);
        }

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return new String("Mac OS device");
        }
    }
}
