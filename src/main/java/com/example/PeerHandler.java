package main.java.com.example;

import main.java.com.example.constants.PeerRequest;
import main.java.com.example.utilities.hostManager.HostManager;
import main.java.com.example.utilities.hostManager.HostManagerFactory;

import java.io.*;
import java.net.Socket;

public class PeerHandler implements Runnable {
    private Socket socket;
    private final HostManager hostManager;

    public PeerHandler(Socket socket) {
        this.socket = socket;
        this.hostManager = new HostManagerFactory().create();
    }

    @Override
    public void run() {
        try (
            PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            InputStream is = this.socket.getInputStream();
        ) {
            String message;
            while ((message = reader.readLine()) != null) {
                String request = message.split(" ")[0];
                switch (request) {
                    case PeerRequest.HELLO -> {
                        writer.println(PeerRequest.HELLO);
                        writer.println(hostManager.getHostName());
                    }

                    case PeerRequest.TRANSFER -> {
                        String file = message.split(" ")[1];
                        int length = Integer.parseInt(message.split(" ")[2]);
                        System.out.println("Receiving file: " + file + "...");

                        writer.println(PeerRequest.OK);
                        byte[] data = new byte[length];
                        System.out.println("Location: " + hostManager.getDownloadPath());

                        try (
                            FileOutputStream fos = new FileOutputStream(hostManager.getDownloadPath() + file);
                            BufferedOutputStream bos = new BufferedOutputStream(fos);
                        ) {
                            int bytesRead = 0;
                            int totalBytes = 0;
                            while ((bytesRead = is.read(data, 0, length)) != -1) {
                                bos.write(data, 0, bytesRead);
                                totalBytes += bytesRead;
                                if (totalBytes == length) break;
                            }
                            bos.flush();
                            writer.println(PeerRequest.OK);
                            System.out.println(file + " saved successfully!");
                        } catch (Exception e) {
                            System.err.println("Error creating " + file + ": " + e);
                        }
                    }

                    case PeerRequest.DISCONNECT -> {
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("I/O error occurred: " + e);
        }
    }
}
