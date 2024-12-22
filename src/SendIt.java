import client.Client;

class Main {
    public static void main(String[] args) {

        System.out.println("App running...");
        Client client = new Client();
        client.scanPeers("192.168.0");
    }
}