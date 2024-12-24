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
                case Command.CONNECT -> {
                    if (userInput.split(" ").length > 1) {
                        System.out.println("Connecting to IP: " + userInput.split(" ")[1]);
                    } else {
                        System.err.println("No IP address provided");
                    }
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