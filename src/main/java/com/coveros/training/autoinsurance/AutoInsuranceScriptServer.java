package com.coveros.training.autoinsurance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AutoInsuranceScriptServer implements Runnable {

    private AutoInsuranceUI autoInsuranceUI;

    /**
     * Construct this with access to the UI we can control
     * @param autoInsuranceUI the UI we are controlling
     */
    public AutoInsuranceScriptServer(AutoInsuranceUI autoInsuranceUI) {
        this.autoInsuranceUI = autoInsuranceUI;
    }

    public void serverStart() {

        int portNumber = 8000;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
                autoInsuranceUI.setLabel(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        serverStart();
    }
}
