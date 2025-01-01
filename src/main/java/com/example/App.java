package main.java.com.example;

import java.util.Scanner;

import main.java.com.example.constants.Command;

class App {
    public static void main(String[] args) throws InterruptedException {
        Peer peer = new Peer();

        // main.java.Main loop - get user input and run accordingly
        Scanner sc = new Scanner(System.in);
        while (true) {
            String userInput = sc.nextLine();
            String command = userInput.split(" ")[0];

            switch (command.toLowerCase()) {
                case Command.SCAN -> peer.scanPeers();
                case Command.SEND -> {
                    if (userInput.split(" ").length < 3)
                        System.err.println("Usage: 'send" + " ${DESTINATION_IP} " + "${FILE}'");
                    else
                        peer.sendFile(userInput.split(" ")[2], userInput.split(" ")[1]);
                }
                case Command.EXIT -> {
                    System.out.println("Exiting");
                    peer.stopListening();
                    return;
                }
                default -> {
                    System.out.println("Unknown command: " + userInput);
                }
            }
        }
    }
}