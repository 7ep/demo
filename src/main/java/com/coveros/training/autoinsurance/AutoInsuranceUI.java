package com.coveros.training.autoinsurance;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.SwingConstants.CENTER;

public class AutoInsuranceUI extends JPanel {

    // server used for automating the UI
    AutoInsuranceScriptServer autoInsuranceScriptServer;

    JLabel label;
    JComboBox<String> claimsDropDown;
    JTextField ageField;
    JButton claimsCalcButton;
    JFrame frame;

    /** Creates the GUI shown inside the frame's content pane. */
    public AutoInsuranceUI(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;

        JPanel autoInsurancePanel = createAutoInsurancePanel();

        //Lay them out.
        Border padding = BorderFactory.createEmptyBorder(20,20,5,20);
        autoInsurancePanel.setBorder(padding);

        add(autoInsurancePanel, BorderLayout.CENTER);
        label = new JLabel("Click the \"Crunch\" button"
                + " to calculate your auto insurance results",
                CENTER);
        add(label, BorderLayout.PAGE_END);
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        startSocketServer();
    }

    private JPanel createAutoInsurancePanel() {
        JPanel box = new JPanel();

        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));

        addLabel(box, "Previous claims:");
        claimsDropDown = addClaimsDropDown(box);

        addLabel(box, "Driver's age:");
        ageField = addClaimsAgeTextField(box);

        claimsCalcButton = addClaimsCalcButton(box);
        claimsCalcButton.addActionListener(e -> {
            final int intPreviousClaims = getIntPreviousClaims();
            final String customerAge = ageField.getText();
            final int intCustomerAge = Integer.parseInt(customerAge);

            final AutoInsuranceAction result = AutoInsuranceProcessor.process(intPreviousClaims, intCustomerAge);

            setLabel(
                    "Premium increase: $" +  result.premiumIncreaseDollars +
                    " Warning Ltr: " + result.warningLetterEnum +
                    " is canceled: " + result.isPolicyCanceled);
        });

        return box;
    }

    /**
     * This method returns an integer representing the number of previous claims for a customer.
     */
    private int getIntPreviousClaims() {
        final String numberPreviousClaims = (String)claimsDropDown.getSelectedItem();

        assert numberPreviousClaims != null;
        switch (numberPreviousClaims) {
            case "0" : return 0;
            case "1" : return 1;
            case "2-4" : return 2;
            case ">=5" : return 5;
            default:
                throw new InvalidClaimsException("invalid value entered");
        }
    }

    private JTextField addClaimsAgeTextField(JPanel box) {
        JTextField textField = new JTextField();
        textField.setVisible(true);
        box.add(textField);
        return textField;
    }

    private JButton addClaimsCalcButton(JPanel box) {
        JButton crunch = new JButton("Crunch");
        crunch.setVisible(true);
        box.add(crunch);
        return crunch;
    }

    private void addLabel(JPanel box, String msg) {
        JLabel lbl = new JLabel(msg);
        lbl.setVisible(true);
        box.add(lbl);
    }

    private JComboBox<String> addClaimsDropDown(JPanel box) {
        String[] previousClaims = {"0", "1", "2-4", ">=5"};
        final JComboBox<String> cb = new JComboBox<>(previousClaims);
        cb.setVisible(true);
        box.add(cb);
        return cb;
    }

    /**
     * start a socket server that can run commands on this UI.
     * This is to enable automation scripts.
     */
    private void startSocketServer() {
        autoInsuranceScriptServer = new AutoInsuranceScriptServer(this);

        Thread newThread = new Thread(autoInsuranceScriptServer);
        newThread.start();
    }

    /**
     * Sets the text at the bottom of the panel
     * @param newText whatever you want the new text to be
     */
    void setLabel(String newText) {
        label.setText(newText);
    }

    /**
     * Selects one of the drop-down items for previous claims
     * @param claims number of previous claims
     */
    void setPreviousClaims(int claims) {
        if (claims <= 0) {
            claimsDropDown.setSelectedIndex(0);
        }

        if (claims == 1) {
            claimsDropDown.setSelectedIndex(1);
        }

        if (claims >= 2 && claims <= 4) {
            claimsDropDown.setSelectedIndex(2);
        }

        if (claims >= 5) {
            claimsDropDown.setSelectedIndex(3);
        }
    }

    /**
     * Sets the age field per what you enter
     * @param age the text that goes into the age field.
     */
    void setClaimsAge(String age) {
        ageField.setText(age);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("AutoInsuranceUI");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Create and set up the content pane.
        AutoInsuranceUI newContentPane = new AutoInsuranceUI(frame);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    // stops and closes the application
    public void close() {
        this.frame.dispose();
        System.exit(0);
    }
}

