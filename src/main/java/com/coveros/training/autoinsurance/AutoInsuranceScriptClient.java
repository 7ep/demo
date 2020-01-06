package com.coveros.training.autoinsurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class AutoInsuranceScriptClient {

    public static final String QUIT = "quit";
    public static final String CLOSE = "close";
    static Logger logger = LoggerFactory.getLogger(AutoInsuranceScriptClient.class);

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
                    logger.info("running script: {}", file);
                    try (Scanner scanner = new Scanner(new File(file))) {
                        while (scanner.hasNextLine()) {
                            String userInput = scanner.nextLine();
                            logger.info(userInput);
                            processLineOfInput(echoSocket, out, in, userInput);
                        }
                    }
                }
            } else {
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    processLineOfInput(echoSocket, out, in, userInput);
                }
            }

        } catch (UnknownHostException e) {
            logger.error("Don't know about host {}", hostName);
            System.exit(1);
        } catch (IOException e) {
            logger.error("Couldn't get I/O for the connection to {}", hostName);
            System.exit(1);
        }
    }

    private static void processLineOfInput(Socket echoSocket, PrintWriter out, BufferedReader in, String userInput) throws IOException {
        if (userInput.equals(QUIT) || userInput.equals(CLOSE)) {
            out.println(userInput);
            logger.info("bye!");
            echoSocket.close();
            System.exit(0);
        }
        logger.info("sending: {}", userInput);
        out.println(userInput);
        final String response = in.readLine();
        logger.info("response: {}", response);
    }
}
