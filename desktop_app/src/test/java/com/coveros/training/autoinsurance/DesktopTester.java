package com.coveros.training.autoinsurance;

public class DesktopTester {
    private final AutoInsuranceScriptClient scriptClient;

    public DesktopTester(AutoInsuranceScriptClient scriptClient) {
        this.scriptClient = scriptClient;
    }


    public void setAge(int age) {
        scriptClient.send(String.format("set age %d", age));
    }

    public void setClaims(int claims) {
        scriptClient.send(String.format("set claims %d", claims));
    }

    public void clickCalculate() {
        scriptClient.send("click calculate");
    }

    public String getLabel() {
        return scriptClient.send("get label");
    }

    /**
     * kill the desktop UI and the tester client
     */
    public void quit() {
        scriptClient.send(AutoInsuranceScriptClient.QUIT);
    }
}
