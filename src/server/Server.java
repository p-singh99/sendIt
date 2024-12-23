package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import common.PeerRequest;

public class Server {

    static int SERVER_PORT = 8080;

    public void run() throws IOException {
        ServerSocket socketConnection = new ServerSocket(SERVER_PORT);
        while (true) {
            try (
                Socket soc = socketConnection.accept();
                PrintWriter clientWriter = new PrintWriter(soc.getOutputStream(), true);
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            ) {
                String clientMessage;
                while ((clientMessage = clientReader.readLine()) != null) {
                    switch (clientMessage) {
                        case PeerRequest.HELLO:
                            System.out.println("Hello!");
                            clientWriter.println(PeerRequest.HELLO);
                            break;
                    }
                    System.out.println(clientMessage);
                    clientWriter.println("Message received!");
                }

//                break;
            } catch (IOException e) {
                System.out.println("I/O error occurred: Client closed the connection");
                break;
            }
        }

        socketConnection.close();
        System.out.println("Connection closed");
    }

    public static void main(String args[]) throws IOException {
        Server server = new Server();
        server.run();
    }
}
