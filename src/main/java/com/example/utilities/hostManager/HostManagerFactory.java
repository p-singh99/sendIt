package main.java.com.example.utilities.hostManager;

public class HostManagerFactory {
    public HostManager create() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return new WinManager();
        else if (os.contains("mac")) return new MacOSManager();

        return null;
    }
}
