package com.coveros.training.autoinsurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AutoInsuranceScriptClient {

    public static final String QUIT = "quit";
    static Logger logger = LoggerFactory.getLogger(AutoInsuranceScriptClient.class);

    public String send(String command) {
        String hostName = "localhost";
        String result = "";
        int portNumber = 8000;
        try (Socket echoSocket = new Socket(hostName, portNumber)) {
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(echoSocket.getInputStream()));
            logger.info("running command: {}", command);
            if (command.equals(QUIT)) {
                result = processCommand(out, in, command);
            }
            result = processCommand(out, in, command);
        } catch (UnknownHostException e) {
            logger.error("Don't know about host {}", hostName);
        } catch (IOException e) {
            logger.error("Couldn't get I/O for the connection to {}", hostName);
        }
        return result;
    }

    private String processCommand(PrintWriter out, BufferedReader in, String command) throws IOException {
        logger.info("sending: {}", command);
        out.println(command);
        final String response = in.readLine();
        logger.info("response: {}", response);
        return response;
    }

}
