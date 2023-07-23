package com.sergeysolutions.malamassistant.FrontEnd;

import com.sergeysolutions.malamassistant.Backend.CalculateHours;
import com.sergeysolutions.malamassistant.Backend.ChangeProjectNumber;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;

public class MalamAssistantUi {
    private JTextField userNameField;
    private JTextField passwordField;
    private JTextField oldProjectNumberField;
    private JTextField newProjectNumberField;
    private final JLabel statusLabel;
    private final Font font;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JTextArea logArea;
    private static MalamAssistantUi instance;



    private enum Operation {CALCULATE_HOURS, SWITCH_PROJECTS}

    private Operation currentOperation;

    public MalamAssistantUi() {
        instance = this;
        this.statusLabel=new JLabel();
        font = new Font("Arial",Font.BOLD,18);
        this.userNameField=new JTextField();
        this.passwordField=new JTextField();
        this.oldProjectNumberField=new JTextField();
        this.newProjectNumberField=new JTextField();
        this.logArea=new JTextArea();

        FontUIResource fontRes = new FontUIResource(new Font("Arial", Font.BOLD, 20));
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }

        JFrame frame = new JFrame("QT Malam Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setResizable(false);

        // Define the logArea and logScrollPane
        logArea.insert("Welcome to Malam Assistant!\nPick a project\nEnter Input\nClick Start\n",0);
        logArea.setBackground(Color.CYAN);
        logArea.setEditable(false);
        logArea.setOpaque(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // Create CardLayout for switching between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setPreferredSize(new Dimension(400, 400));

        // Create the CalculateHours Panel
        JPanel calculateHoursPanel = createCalculateHoursPanel();

        // Create the SwitchProjects Panel
        JPanel switchProjectsPanel = createSwitchProjectsPanel();

        // Add the panels to the card panel
        cardPanel.setOpaque(false);
        cardPanel.add(calculateHoursPanel, "CALCULATE_HOURS");
        cardPanel.add(switchProjectsPanel, "SWITCH_PROJECTS");

        // Define the buttonsPanel and add the buttons
        JButton calculateHoursButton = createButton("Calculate Hours", e -> chooseCalculateHours());
        JButton projectSwitcherButton = createButton("Project Switcher", e -> chooseSwitchProjects());
        JButton startButton = createButton("Start", e -> startCurrentOperation());
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(calculateHoursButton);
        buttonsPanel.add(projectSwitcherButton);
        buttonsPanel.add(startButton);

        // Define the panel and add the components
        ImagePanel panel = new ImagePanel(getClass().getResource("/m1.jpg"));
        panel.setLayout(new BorderLayout());
        panel.add(buttonsPanel, BorderLayout.NORTH);
        panel.add(cardPanel, BorderLayout.CENTER);

        // Add the components to frame
        frame.add(panel);
        frame.add(logScrollPane, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
        calculateHoursButton.doClick();
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private JPanel createCalculateHoursPanel() {
        JPanel calculateHoursPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        userNameField = new JTextField(10);
        gbc.gridy = 0;
        JLabel username = new JLabel("Username") ;
        username.setFont(font);
        calculateHoursPanel.add(username, gbc);
        gbc.gridx = 1;
        calculateHoursPanel.add(userNameField, gbc);

        passwordField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel password = new JLabel("Password") ;
        password.setFont(font);
        calculateHoursPanel.add(password , gbc);
        gbc.gridx = 1;
        calculateHoursPanel.add(passwordField, gbc);

        gbc.gridy = 2;
        statusLabel.setText("Activating Calculate Hours");
        statusLabel.setFont(font);
        calculateHoursPanel.add(statusLabel, gbc);

        gbc.gridy= 3;
        JButton startButton = createButton("Start", e -> startCurrentOperation());
        calculateHoursPanel.add(startButton,gbc);


        calculateHoursPanel.setOpaque(false);


        return calculateHoursPanel;
    }

    private JPanel createSwitchProjectsPanel() {
        JPanel switchProjectsPanel = new JPanel(new GridBagLayout());
        switchProjectsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        userNameField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel username = new JLabel("Username") ;
        username.setFont(font);
        switchProjectsPanel.add(username, gbc);
        gbc.gridx = 1;
        switchProjectsPanel.add(userNameField, gbc);

        passwordField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel password = new JLabel("Password") ;
        password.setFont(font);
        switchProjectsPanel.add(password , gbc);
        gbc.gridx = 1;
        switchProjectsPanel.add(passwordField, gbc);

        oldProjectNumberField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel oldProjectLabel = new JLabel("Project To Replace") ;
        oldProjectLabel.setFont(font);
        switchProjectsPanel.add(oldProjectLabel, gbc);
        gbc.gridx = 1;
        switchProjectsPanel.add(oldProjectNumberField, gbc);

        newProjectNumberField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel newProjectLabel = new JLabel("Project To Save") ;
        newProjectLabel.setFont(font);
        switchProjectsPanel.add(newProjectLabel, gbc);
        gbc.gridx = 1;
        switchProjectsPanel.add(newProjectNumberField, gbc);

        gbc.gridx=0;
        gbc.gridy=4;
        statusLabel.setText("Activating Project Switcher");
        statusLabel.setFont(font);
        switchProjectsPanel.add(statusLabel, gbc);


        return switchProjectsPanel;
    }

    private void chooseCalculateHours() {
        cardLayout.show(cardPanel, "CALCULATE_HOURS");
        currentOperation = Operation.CALCULATE_HOURS;
    }

    private void chooseSwitchProjects() {
        cardLayout.show(cardPanel, "SWITCH_PROJECTS");
        currentOperation = Operation.SWITCH_PROJECTS;
    }

    private void startCurrentOperation() {
        String userName = userNameField.getText();
        String password = passwordField.getText();

        try {
            if (currentOperation == Operation.CALCULATE_HOURS) {
                new CalculateHours(userName, password).start();
            } else if (currentOperation == Operation.SWITCH_PROJECTS) {
                String oldProjectNumber = oldProjectNumberField.getText();
                String newProjectNumber = newProjectNumberField.getText();
                new ChangeProjectNumber(userName, password).start(oldProjectNumber, newProjectNumber);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (org.openqa.selenium.TimeoutException  e){
            appendLog("Could not find one or more elements.\nProgram has stopped.");
        }
    }

    public static MalamAssistantUi getInstance() {
        return instance;
    }

    public void appendLog(String text) {
        SwingUtilities.invokeLater(() -> logArea.append(text + "\n"));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MalamAssistantUi();
        });
    }

}
