package com.coveros.training.autoinsurance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.coveros.training.autoinsurance.AutoInsuranceScriptClient.QUIT;

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
        boolean loopAgain = true;

        while(loopAgain) {
            try (
                    ServerSocket serverSocket = new ServerSocket(portNumber);
                    Socket clientSocket = serverSocket.accept();
            ) {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String result = "OK";

                    final String[] inputTokens = inputLine.split(" ");
                    if (inputTokens[0].equals(QUIT)) {
                        loopAgain = false;
                    }

                    if (inputTokens[0].equals("set")) {
                        if (inputTokens[1].equals("label")) {
                            autoInsuranceUI.setLabel(inputTokens[2]);
                        }
                        if (inputTokens[1].equals("age")) {
                            autoInsuranceUI.setClaimsAge(inputTokens[2]);
                        }
                        if (inputTokens[1].equals("claims")) {
                            final int i = Integer.parseInt(inputTokens[2]);
                            autoInsuranceUI.setPreviousClaims(i);
                        }
                    }

                    if (inputTokens[0].equals("get")) {
                        if (inputTokens[1].equals("label")) {
                            result = autoInsuranceUI.label.getText();
                        }
                        if (inputTokens[1].equals("age")) {
                            result = autoInsuranceUI.ageField.getText();
                        }
                        if (inputTokens[1].equals("claims")) {
                            result = autoInsuranceUI.claimsDropDown.getSelectedItem().toString();
                        }
                    }

                    if (inputTokens[0].equals("click")) {
                        if (inputTokens[1].equals("calculate")) {
                            autoInsuranceUI.claimsCalcButton.doClick();
                        }
                    }

                    out.println(result);

                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        serverStart();
    }
}
