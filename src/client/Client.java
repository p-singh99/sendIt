package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    static int PORT = 8080;

    public void run() throws IOException {
        InetAddress host = InetAddress.getByName("localhost");

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

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}
