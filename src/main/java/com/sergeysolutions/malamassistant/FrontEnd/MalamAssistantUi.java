package com.sergeysolutions.malamassistant.FrontEnd;

import com.sergeysolutions.malamassistant.Backend.CalculateHours;
import com.sergeysolutions.malamassistant.Backend.ChangeProjectNumber;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.prefs.Preferences;

public class MalamAssistantUi {
    private JTextField userNameFieldForCalc;
    private JTextField passwordFieldForCalc;
    private JTextField userNameFieldForSwitchProjects;
    private JTextField passwordFieldForSwitchProjects;
    private JTextField oldProjectNumberField;
    private JTextField newProjectNumberField;
    private final Font font;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JTextArea logArea;
    private static MalamAssistantUi instance;
    private final JFrame frame;
    private Font font1;
    private JButton optionsButton;
    private JDialog optionsDialog;
    private final Preferences preferences;
    private JCheckBox headlessOptionCheckBox;
    private JCheckBox dontCloseDriverCheckBox;



    private enum Operation {CALCULATE_HOURS, SWITCH_PROJECTS}

    private Operation currentOperation;

    public MalamAssistantUi() {
        instance = this;
        font = new Font("Arial", Font.BOLD, 18);
        this.oldProjectNumberField = new JTextField();
        this.newProjectNumberField = new JTextField();
        this.logArea = new JTextArea();
        logArea.setBorder(null);
        preferences = Preferences.userNodeForPackage(this.getClass());
        headlessOptionCheckBox = new JCheckBox("Show Browser");
        headlessOptionCheckBox.setSelected(preferences.getBoolean("Show Browser", false));
        headlessOptionCheckBox.setFont(font);

        dontCloseDriverCheckBox = new JCheckBox("Close Browser after execution");
        dontCloseDriverCheckBox.setSelected(preferences.getBoolean("Close Browser After Execution", true));
        dontCloseDriverCheckBox.setFont(font);
        FontUIResource fontRes = new FontUIResource(new Font("Arial", Font.BOLD, 20));
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }

        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(470, 650);
        frame.setResizable(false);

        // Define the logArea and logScrollPane
        logArea.insert("Welcome to Malam Assistant!\nPick a project\nEnter Input\nClick Start\n", 0);
        logArea.setBackground(Color.black);
        logArea.setForeground(Color.white);
        logArea.setEditable(false);
        logArea.setOpaque(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(null);

        // Create CardLayout for switching between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setPreferredSize(new Dimension(450, 650));

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

        optionsButton = createButton("Options", e -> showOptions());
        optionsDialog = new JDialog(frame, "Options", true);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(calculateHoursButton);
        buttonsPanel.add(projectSwitcherButton);
        buttonsPanel.add(optionsButton);
        buttonsPanel.setBackground(Color.black);

        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/m1_1_470x313.jpg")));
        JLabel imageLabel = new JLabel(imageIcon);

        // Create center panel with BoxLayout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
// Add buttonsPanel to the NORTH and cardPanel to the CENTER
        centerPanel.add(buttonsPanel, BorderLayout.NORTH);
        centerPanel.add(cardPanel, BorderLayout.CENTER);

        // Define the panel and add the components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(imageLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(logScrollPane, BorderLayout.SOUTH);

        // Add the components to frame

        frame.add(mainPanel);

        // Make the frame visible
        frame.setVisible(true);
        calculateHoursButton.doClick();
        frame.validate();
        frame.repaint();
    }

    private void showOptions() {
        // Create a new JPanel with a GridLayout
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        // Add a checkbox for the headless option
        optionsPanel.add(headlessOptionCheckBox);
        // Add a checkbox for the "don't close driver" option
        optionsDialog.setResizable(false);
        optionsPanel.add(dontCloseDriverCheckBox);
        // Add the optionsPanel to the optionsDialog
        optionsDialog.add(optionsPanel);
        // Pack the dialog to size it according to its contents
        optionsDialog.pack();
        // Center the dialog relative to the frame
        optionsDialog.setLocationRelativeTo(frame);
        // Show the dialog
        optionsDialog.setVisible(true);


        // Save the state of the checkboxes when the dialog is closed
        optionsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                preferences.putBoolean("headlessOption", headlessOptionCheckBox.isSelected());
                preferences.putBoolean("dontCloseDriver", dontCloseDriverCheckBox.isSelected());
            }
        });
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private JPanel createCalculateHoursPanel() {
        JPanel calculateHoursPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel username = new JLabel("Username");
        username.setFont(font);
        username.setForeground(Color.white);
        calculateHoursPanel.add(username, gbc);

        gbc.gridx = 1;
        userNameFieldForCalc = new JTextField(10);
        font1 = new Font("Arial", Font.BOLD, 18);
        userNameFieldForCalc.setFont(font1);
        calculateHoursPanel.add(userNameFieldForCalc, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel password = new JLabel("Password");
        password.setFont(font);
        password.setForeground(Color.white);
        calculateHoursPanel.add(password, gbc);

        gbc.gridx = 1;
        passwordFieldForCalc = new JPasswordField(10);
        calculateHoursPanel.add(passwordFieldForCalc, gbc);
        passwordFieldForCalc.setFont(font1);
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel statusLabel = new JLabel();
        statusLabel.setText("Activating Calculate Hours");
        statusLabel.setFont(font);
        statusLabel.setForeground(Color.white);
        calculateHoursPanel.add(statusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton startButton = createButton("Start", e -> startCurrentOperation());
        calculateHoursPanel.add(startButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        calculateHoursPanel.add(quitButton, gbc);
        calculateHoursPanel.setOpaque(false);

        return calculateHoursPanel;

    }

    private JPanel createSwitchProjectsPanel() {
        JPanel switchProjectsPanel = new JPanel(new GridBagLayout());
        switchProjectsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel username = new JLabel("Username");
        username.setFont(font);
        username.setForeground(Color.white);
        switchProjectsPanel.add(username, gbc);

        gbc.gridx = 1;
        userNameFieldForSwitchProjects = new JTextField(10);
        userNameFieldForSwitchProjects.setFont(font1);
        switchProjectsPanel.add(userNameFieldForSwitchProjects, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel password = new JLabel("Password");
        password.setFont(font);
        password.setForeground(Color.white);
        switchProjectsPanel.add(password, gbc);

        gbc.gridx = 1;
        passwordFieldForSwitchProjects = new JPasswordField(10);
        switchProjectsPanel.add(passwordFieldForSwitchProjects, gbc);
        passwordFieldForSwitchProjects.setFont(font1);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel oldProjectLabel = new JLabel("Project To Replace");
        oldProjectLabel.setFont(font);
        oldProjectLabel.setForeground(Color.white);
        switchProjectsPanel.add(oldProjectLabel, gbc);

        gbc.gridx = 1;
        oldProjectNumberField = new JTextField(10);
        oldProjectNumberField.setFont(font1);
        switchProjectsPanel.add(oldProjectNumberField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel newProjectLabel = new JLabel("Project To Save");
        newProjectLabel.setFont(font);
        newProjectLabel.setForeground(Color.white);
        switchProjectsPanel.add(newProjectLabel, gbc);

        gbc.gridx = 1;
        newProjectNumberField = new JTextField(10);
        switchProjectsPanel.add(newProjectNumberField, gbc);
        newProjectNumberField.setFont(font1);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel statusLabel = new JLabel();
        statusLabel.setText("Activating Project Switcher");
        statusLabel.setFont(font);
        statusLabel.setForeground(Color.white);
        switchProjectsPanel.add(statusLabel, gbc);

        gbc.gridy = 5;
        JButton startButton = createButton("Start", e -> startCurrentOperation());
        switchProjectsPanel.add(startButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        switchProjectsPanel.add(quitButton, gbc);

        return switchProjectsPanel;
    }

    private void chooseCalculateHours() {
        cardLayout.show(cardPanel, "CALCULATE_HOURS");
        currentOperation = Operation.CALCULATE_HOURS;
        frame.validate();
        frame.repaint();
    }

    private void chooseSwitchProjects() {
        cardLayout.show(cardPanel, "SWITCH_PROJECTS");
        cardPanel.repaint();
        currentOperation = Operation.SWITCH_PROJECTS;
        frame.validate();
        frame.repaint();
    }

    private void startCurrentOperation() {
        logArea.setText("");
        boolean isHeadless = !headlessOptionCheckBox.isSelected();
        boolean keepAlive= !dontCloseDriverCheckBox.isSelected();
        System.out.println("this is isHeadless " + isHeadless);
        System.out.println("This is keepAlive " + keepAlive);
        try {
            if (currentOperation == Operation.CALCULATE_HOURS) {
                String userName = userNameFieldForCalc.getText();
                String password = passwordFieldForCalc.getText();
                new CalculateHours(userName, password,isHeadless).start(keepAlive);
            } else if (currentOperation == Operation.SWITCH_PROJECTS) {
                String userName = userNameFieldForSwitchProjects.getText();
                String password = passwordFieldForSwitchProjects.getText();
                String oldProjectNumber = oldProjectNumberField.getText();
                String newProjectNumber = newProjectNumberField.getText();
                new ChangeProjectNumber(userName, password,isHeadless).start(oldProjectNumber, newProjectNumber,keepAlive);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (org.openqa.selenium.TimeoutException e) {
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
