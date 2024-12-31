package common.utilities.hostManager;

public abstract class HostManager {

    public String getDownloadPath() {
        String userDir = System.getProperty("user.home");
        return userDir + "/Downloads/";
    }

    public abstract String getHostName();
}
