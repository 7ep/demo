package com.coveros.training.autoinsurance;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class AutoInsuranceScriptClient {

    public static final String QUIT = "quit";

    public static void main(String[] args) {
        String hostName = "localhost";
        int portNumber = 8000;
        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {

            if (args.length > 0) {
                // loop through all the script files, if any exist.
                for (String file : args) {
                    System.out.println("running script: " + file);
                    Scanner scanner = new Scanner(new File(file));
                    while (scanner.hasNextLine()) {
                        String userInput = scanner.nextLine();
                        System.out.println(userInput);
                        processLineOfInput(echoSocket, out, in, userInput);
                    }
                }
            } else {
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    processLineOfInput(echoSocket, out, in, userInput);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    private static void processLineOfInput(Socket echoSocket, PrintWriter out, BufferedReader in, String userInput) throws IOException {
        if (userInput.equals(QUIT)) {
            out.println(QUIT);
            System.out.println("bye!");
            echoSocket.close();
            System.exit(0);
        }
        System.out.println("sending: " + userInput);
        out.println(userInput);
        System.out.println("response: " + in.readLine());
    }
}
