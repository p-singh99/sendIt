package main.java.com.example.utilities.hostManager;

public class WinManager extends HostManager {

    @Override
    public String getHostName() {
        return System.getenv("COMPUTERNAME");
    }
}
