package com.coveros.training.autoinsurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Transient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static com.coveros.training.autoinsurance.AutoInsuranceScriptClient.CLOSE;
import static com.coveros.training.autoinsurance.AutoInsuranceScriptClient.QUIT;
import static java.util.stream.Collectors.toList;

public class AutoInsuranceScriptServer implements Runnable, Transient {

    static Logger logger = LoggerFactory.getLogger(AutoInsuranceScriptServer.class);

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

                    final String[] inputTokens =
                            Arrays.stream(inputLine.split(" "))
                                    .map(t -> t.toLowerCase())
                                    .collect(toList())
                                    .toArray(new String[0]);

                    if (inputTokens[0].toLowerCase().equals(QUIT)) {
                        loopAgain = false;
                    }

                    if (inputTokens[0].equals(CLOSE)) {
                        autoInsuranceUI.close();
                        loopAgain = false;
                    }

                    handleSetCases(inputTokens);

                    result = handleGetCases(result, inputTokens);

                    handleClickActions(inputTokens);

                    out.println(result);

                }
            } catch (IOException e) {
                logger.error("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                logger.error(e.getMessage());
            }
        }
    }

    private void handleClickActions(String[] inputTokens) {
        if (inputTokens[0].equals("click")) {
            if (inputTokens[1].equals("calculate")) {
                autoInsuranceUI.claimsCalcButton.doClick();
            }
        }
    }

    private String handleGetCases(String result, String[] inputTokens) {
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
        return result;
    }

    private void handleSetCases(String[] inputTokens) {
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
    }

    @Override
    public void run() {
        serverStart();
    }

    @Override
    public boolean value() {
        return false;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
