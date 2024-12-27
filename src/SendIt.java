import client.Client;

import java.util.Scanner;

import common.constants.Command;

class Main {
    public static void main(String[] args) {
        Client client = new Client();

        // Main loop - get user input and run accordingly
        Scanner sc = new Scanner(System.in);
        while (true) {
            String userInput = sc.nextLine();
            String command = userInput.split(" ")[0];

            switch (command.toLowerCase()) {
                case Command.SCAN -> client.scanPeers();
                case Command.SEND -> {
                    if (userInput.split(" ").length < 3)
                        System.err.println("Usage: 'send" + " ${DESTINATION_IP} " + "${FILE}'");
                    else
                        client.sendFile(userInput.split(" ")[2], userInput.split(" ")[1]);
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