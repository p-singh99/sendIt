package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import common.constants.PeerRequest;

public class Server {

    static int SERVER_PORT = 8080;
    static String DOWNLOAD_PATH = "/Users/pawanjotsingh/Downloads/";

    public void run() throws IOException {
        ServerSocket socketConnection = new ServerSocket(SERVER_PORT);
        while (true) {
            try (
                Socket soc = socketConnection.accept();
                PrintWriter clientWriter = new PrintWriter(soc.getOutputStream(), true);
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                InputStream is = soc.getInputStream();
            ) {
                String clientMessage;
                while ((clientMessage = clientReader.readLine()) != null) {
                    String request = clientMessage.split(" ")[0];
                    switch (request) {
                        case PeerRequest.HELLO -> {
                            clientWriter.println(PeerRequest.HELLO);
                            clientWriter.println(InetAddress.getLocalHost().getHostName());
                        }

                        case PeerRequest.TRANSFER -> {
                            String file = clientMessage.split(" ")[1];
                            int length = Integer.parseInt(clientMessage.split(" ")[2]);
                            System.out.println("Receiving file " + file + "...");
                            clientWriter.println(PeerRequest.OK);
                            byte[] data = new byte[length];
                            FileOutputStream fos = new FileOutputStream(DOWNLOAD_PATH + file);
                            BufferedOutputStream bos = new BufferedOutputStream(fos);

                            int bytesRead;
                            int totalBytes = 0;
                            while ((bytesRead = is.read(data, 0, length)) != -1) {
                                bos.write(data, 0, bytesRead);
                                totalBytes += bytesRead;
                                if (totalBytes == length) break;
                            }
                            bos.flush();
                            clientWriter.println(PeerRequest.OK);
                            System.out.println("Transfer complete!");
                        }
                    }
                }
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
