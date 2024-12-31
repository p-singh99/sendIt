import client.Client;

import java.util.Scanner;

import constants.Command;

class Main {
    public static void main(String[] args) {
        Peer peer = new Peer();
        peer.start();

        // Main loop - get user input and run accordingly
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
                    return;
                }
                default -> {
                    System.out.println("Unknown command: " + userInput);
                }
            }
        }
    }
}