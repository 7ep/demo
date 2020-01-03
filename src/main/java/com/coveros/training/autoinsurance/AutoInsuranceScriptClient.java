package com.coveros.training.autoinsurance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AutoInsuranceScriptClient {

    public static final String QUIT = "quit";

    public static void main(String[] args) {
        System.out.println(String.join(";", args));
        String hostName = "localhost";
        int portNumber = 8000;
        String[] runThis = {"set age 22", "set claims 1", "click calculate", "get label"};
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
//            String userInput;
//            while ((userInput = stdIn.readLine()) != null) {
            for (String userInput : runThis) {
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
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
