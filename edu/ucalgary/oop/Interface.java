package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing all user interface related code for the EWR volunteer
 * scheduling application.
 *
 * @author      Mariyah Malik
 * @author      Ethan Reed
 * @version     1.0
 * @since       0.7
 */
public class Interface extends JFrame implements ActionListener, MouseListener {
    
    private final Container CONTENT_PANE;

    private EwrScheduler ewrScheduler;

    // Objects used for styling the multiple pages of the GUI
    private final Dimension MIN_WIN_SIZE = new Dimension(1000, 700);
    private final Color BACKGROUND_C = new Color(0xF0EEE8);
    private final Color FOREGROUND_C = new Color(0x4C8C5C);
    private Border fieldBorder = BorderFactory.createLineBorder(FOREGROUND_C);

    // Objects used in login page
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel loginInstructions;
    private JButton loginButton;

    // Objects used in schedule creation page
    private JButton generateButton;
    private JLabel generateInstructions;
    private JButton printButton;
    private JTextField filenameField;
    private ArrayList<JPanel> hourTablePanels;
    private ArrayList<JLabel> hourTableLabels;

    // Objects used in treatment editing page
    private JTable taskTable;
    private JLabel modifyLabel;
    private JButton moveButton;
    private JButton removeButton;
    private JButton doneEditingButton;
    private JTextField newHourField;

    /**
     * Creates a new JFrame object and calls the method to display the
     * login page.
     */
    public Interface() {
        super("EWR Schedule Generator");
        CONTENT_PANE = this.getContentPane();
        loginGUI();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Displays the login page of the GUI
     */
    public void loginGUI() {
        this.setSize(600, 500);
        this.setResizable(false);

        // Logo display
        ImageIcon logo = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel("Volunteer Task Scheduler");
        logoLabel.setIcon(logo);
        logoLabel.setForeground(FOREGROUND_C);
        logoLabel.setHorizontalTextPosition(JLabel.RIGHT);
        logoLabel.setVerticalTextPosition(JLabel.CENTER);

        // Logo panel
        Dimension logoDimension = new Dimension(400, 108);
        JPanel logoPanel = new JPanel();
        logoPanel.setMaximumSize(logoDimension);
        logoPanel.setPreferredSize(logoDimension);
        logoPanel.setMinimumSize(logoDimension);
        logoPanel.setBackground(BACKGROUND_C);
        logoPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        logoPanel.add(logoLabel);

        // Multiple use dimensions
        Dimension textFieldDimension = new Dimension(250, 25);
        Dimension textPanelDimension = new Dimension(400, 25);

        // Multiple use layouts
        FlowLayout textPanelLayout = new FlowLayout();
        textPanelLayout.setAlignment(FlowLayout.RIGHT);
        fieldBorder = BorderFactory.createLineBorder(FOREGROUND_C);

        // Instructions
        loginInstructions = new JLabel("Enter username and password");
        loginInstructions.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Username field
        usernameField = new JTextField();
        usernameField.setBackground(BACKGROUND_C.brighter());
        usernameField.setBorder(fieldBorder);
        usernameField.setPreferredSize(textFieldDimension);
        usernameField.setAlignmentX(JTextField.CENTER_ALIGNMENT);

        // Username panel
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(textPanelLayout);
        usernamePanel.setMaximumSize(textPanelDimension);
        usernamePanel.setOpaque(false);
        usernamePanel.add(Box.createHorizontalGlue());
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.add(Box.createHorizontalStrut(33));

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBackground(BACKGROUND_C.brighter());
        passwordField.setBorder(fieldBorder);
        passwordField.setPreferredSize(textFieldDimension);
        passwordField.setAlignmentX(JPasswordField.CENTER_ALIGNMENT);

        // Password panel
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(textPanelLayout);
        passwordPanel.setMaximumSize(textPanelDimension);
        passwordPanel.setOpaque(false);
        passwordPanel.add(Box.createHorizontalGlue());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.add(Box.createHorizontalStrut(33));

        // Login button
        loginButton = new JButton("Log in");
        loginButton.addActionListener(this);
        loginButton.setAlignmentX((float)0.1);

        // Login panel
        Dimension loginDimension = new Dimension(400, 200);
        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(true);
        loginPanel.setPreferredSize(loginDimension);
        loginPanel.setMaximumSize(loginDimension);
        loginPanel.setMinimumSize(loginDimension);
        loginPanel.setBackground(BACKGROUND_C);
        loginPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C, 5));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
        loginPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(loginInstructions);
        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(usernamePanel);
        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(passwordPanel);
        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalGlue());

        // Content pane
        CONTENT_PANE.setBackground(BACKGROUND_C);
        CONTENT_PANE.setLayout(new BoxLayout(CONTENT_PANE, BoxLayout.PAGE_AXIS));
        CONTENT_PANE.add(Box.createVerticalGlue());
        CONTENT_PANE.add(logoPanel);
        CONTENT_PANE.add(Box.createVerticalStrut(20));
        CONTENT_PANE.add(loginPanel);
        CONTENT_PANE.add(Box.createVerticalGlue());
    }

    /**
     * Displays the schedule creation page of the GUI.
     */
    public void createScheduleGUI() {
        CONTENT_PANE.removeAll();

        // Build schedule button
        generateButton = new JButton("Build schedule");
        generateButton.addActionListener(this);
        generateButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Print to file button
        printButton = new JButton("Print to file");
        printButton.addActionListener(this);
        printButton.setEnabled(false);
        printButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Schedule grid panel
        GridLayout scheduleGrid = new GridLayout(6, 4, 2, 0);
        JPanel schedulePanel = new JPanel(scheduleGrid);
        schedulePanel.setBackground(BACKGROUND_C.darker());
        schedulePanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        // Initialize lists used when responding to input
        hourTablePanels = new ArrayList<>();
        hourTableLabels = new ArrayList<>();

        // Display empty schedule grid and fill lists
        for(int i = 0; i < 24; i++) {
            JLabel hourLabel = new JLabel(String.format("  %02d:00", i));
            hourTableLabels.add(hourLabel);
            hourLabel.setBackground(BACKGROUND_C.darker());
            hourLabel.setOpaque(true);
            hourLabel.setForeground(BACKGROUND_C.brighter());

            JPanel hourTablePanel = new JPanel(new BorderLayout());
            hourTablePanel.setBackground(BACKGROUND_C);
            hourTablePanels.add(hourTablePanel);

            JPanel hourPanel = new JPanel(new BorderLayout());
            hourPanel.add(hourLabel, BorderLayout.PAGE_START);
            hourPanel.add(hourTablePanel, BorderLayout.CENTER);
            schedulePanel.add(hourPanel);
        }

        // Filename field
        Dimension filenameSize = new Dimension(200, 20);
        filenameField = new JTextField();
        filenameField.setEnabled(false);
        filenameField.setMaximumSize(filenameSize);
        filenameField.setPreferredSize(filenameSize);
        filenameField.setBorder(fieldBorder);
        filenameField.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Instruction label
        generateInstructions = new JLabel("Press button to build schedule");
        generateInstructions.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Interaction panel
        Dimension buttonPanelSize = new Dimension(300, 670);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setMinimumSize(buttonPanelSize);
        buttonPanel.setPreferredSize(buttonPanelSize);
        buttonPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(generateInstructions);
        buttonPanel.add(Box.createVerticalStrut(100));
        buttonPanel.add(generateButton);
        buttonPanel.add(Box.createVerticalStrut(100));
        buttonPanel.add(filenameField);
        buttonPanel.add(printButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Schedule header
        JLabel scheduleHeader = new JLabel("Today's Schedule");
        scheduleHeader.setForeground(BACKGROUND_C);
        scheduleHeader.setHorizontalTextPosition(JLabel.CENTER);
        scheduleHeader.setHorizontalAlignment(JLabel.CENTER);

        // Interaction panel header
        Dimension spacerSize = new Dimension(300, 30);
        JPanel spacer = new JPanel();
        spacer.setBackground(FOREGROUND_C.darker());
        spacer.setPreferredSize(spacerSize);
        spacer.setMaximumSize(spacerSize);

        // Complete header
        Dimension headerSize = new Dimension(1000, 30);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(FOREGROUND_C.darker());
        headerPanel.setPreferredSize(headerSize);
        headerPanel.setMinimumSize(headerSize);
        headerPanel.add(spacer, BorderLayout.LINE_START);
        headerPanel.add(scheduleHeader, BorderLayout.CENTER);

        // Content pane
        CONTENT_PANE.setLayout(new BorderLayout());
        CONTENT_PANE.add(headerPanel, BorderLayout.PAGE_START);
        CONTENT_PANE.add(buttonPanel, BorderLayout.LINE_START);
        CONTENT_PANE.add(schedulePanel, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    /**
     * Displays the treatment editing page of the GUI
     */
    public void modifyTasksGUI() {
        CONTENT_PANE.removeAll();

        // Table of minutes already scheduled in each hour
        String[] timeTableHeader = {"Hour", "Scheduled Minutes"};
        JTable scheduledTimesTable = new JTable(new DefaultTableModel(timeTableHeader, 24) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        scheduledTimesTable.setGridColor(Color.black);
        scheduledTimesTable.setBackground(BACKGROUND_C.brighter());
        scheduledTimesTable.getTableHeader().setBackground(BACKGROUND_C);
        scheduledTimesTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        scheduledTimesTable.getColumnModel().getColumn(1).setPreferredWidth(115);

        // Set minutes scheduled in table
        int[] hourlyTime = ewrScheduler.getHourlyScheduledTime();
        for(int i = 0; i < 24; i++) {
            scheduledTimesTable.getModel().setValueAt(String.format("%02d:00", i), i, 0);
            scheduledTimesTable.getModel().setValueAt(hourlyTime[i], i, 1);
        }

        // Pane for scheduled time table
        Dimension scheduledTimesPaneSize = new Dimension(165, 405);
        JScrollPane scheduledTimesPane = new JScrollPane(
                scheduledTimesTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scheduledTimesPane.setForeground(FOREGROUND_C.darker());
        scheduledTimesPane.setMaximumSize(scheduledTimesPaneSize);
        scheduledTimesPane.setPreferredSize(scheduledTimesPaneSize);
        scheduledTimesPane.setMinimumSize(scheduledTimesPaneSize);
        scheduledTimesPane.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        // layout constraints for scheduled time table
        GridBagConstraints scheduledTimeConstraints = new GridBagConstraints();
        scheduledTimeConstraints.gridx = 0;
        scheduledTimeConstraints.gridy = 0;
        scheduledTimeConstraints.gridheight = 2;
        scheduledTimeConstraints.insets = new Insets(0, 0, 0, 10);

        // Get list of tasks that could be moved to solve conflict
        ArrayList<Task> movableTasks = ewrScheduler.getExcessiveList();

        // Table of task that could be moved
        taskTable = new JTable(new TaskTableModel(movableTasks));
        taskTable.setGridColor(Color.black);
        taskTable.setBackground(BACKGROUND_C.brighter());
        taskTable.getTableHeader().setBackground(BACKGROUND_C);
        taskTable.addMouseListener(this);

        // Pane for movable task table
        Dimension taskPaneSize = new Dimension(600, 165);
        JScrollPane taskPane = new JScrollPane(taskTable);
        taskPane.setForeground(FOREGROUND_C.darker());
        taskPane.setMaximumSize(taskPaneSize);
        taskPane.setPreferredSize(taskPaneSize);
        taskPane.setMinimumSize(taskPaneSize);
        taskPane.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        // layout constraints for movable task pane
        GridBagConstraints taskConstraints = new GridBagConstraints();
        taskConstraints.gridx = 1;
        taskConstraints.gridy = 0;
        taskConstraints.insets = new Insets(0, 10, 10, 0);

        // Dimension of treatment move and remove panels
        Dimension optionPanelSize = new Dimension(500, 30);

        // New window start hour field
        Dimension hourFieldSize = new Dimension(30, 20);
        newHourField = new JTextField();
        newHourField.setMinimumSize(hourFieldSize);
        newHourField.setPreferredSize(hourFieldSize);
        newHourField.setMaximumSize(hourFieldSize);
        newHourField.setBorder(fieldBorder);

        // New window start hour label
        JLabel moveHourLabel = new JLabel("Move window start to hour:");

        // New window start hour button
        moveButton = new JButton("Move");
        moveButton.addActionListener(this);

        // New window start hour panel
        JPanel moveHourPanel = new JPanel();
        moveHourPanel.setLayout(new FlowLayout());
        moveHourPanel.setMinimumSize(optionPanelSize);
        moveHourPanel.setPreferredSize(optionPanelSize);
        moveHourPanel.setMaximumSize(optionPanelSize);
        moveHourPanel.add(moveHourLabel);
        moveHourPanel.add(newHourField);
        moveHourPanel.add(moveButton);

        // Remove treatment label
        JLabel removeHourLabel = new JLabel("Or remove treatment from database:");

        // Remove treatment button
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);

        // Remove treatment pane
        JPanel removeHourPanel = new JPanel();
        removeHourPanel.setLayout(new FlowLayout());
        removeHourPanel.setMinimumSize(optionPanelSize);
        removeHourPanel.setPreferredSize(optionPanelSize);
        removeHourPanel.setMaximumSize(optionPanelSize);
        removeHourPanel.add(removeHourLabel);
        removeHourPanel.add(removeButton);

        // Finished button
        doneEditingButton = new JButton("Back to schedule creation");
        doneEditingButton.addActionListener(this);
        doneEditingButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Instructions for modification pane
        modifyLabel = new JLabel("Click on a row of the table to modify it");
        modifyLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        modifyLabel.setHorizontalTextPosition(JLabel.CENTER);
        modifyLabel.setHorizontalAlignment(JLabel.CENTER);

        // Modification pane
        Dimension modifyPanelSize = new Dimension(600, 220);
        JPanel modifyPanel = new JPanel();
        modifyPanel.setLayout(new BoxLayout(modifyPanel, BoxLayout.PAGE_AXIS));
        modifyPanel.setMaximumSize(modifyPanelSize);
        modifyPanel.setMinimumSize(modifyPanelSize);
        modifyPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        modifyPanel.add(Box.createVerticalGlue());
        modifyPanel.add(modifyLabel);
        modifyPanel.add(Box.createVerticalStrut(30));
        modifyPanel.add(moveHourPanel);
        modifyPanel.add(Box.createVerticalStrut(10));
        modifyPanel.add(removeHourPanel);
        modifyPanel.add(Box.createVerticalStrut(30));
        modifyPanel.add(doneEditingButton);
        modifyPanel.add(Box.createVerticalGlue());

        // Modification pane layout constraints
        GridBagConstraints modifyConstraints = new GridBagConstraints();
        modifyConstraints.gridx = 1;
        modifyConstraints.gridy = 1;
        modifyConstraints.fill = GridBagConstraints.BOTH;
        modifyConstraints.insets = new Insets(10, 10, 0, 0);

        // Grid containing all previous panels
        Dimension interactionPanelSize = new Dimension(825, 445);
        JPanel interactionPanel = new JPanel(new GridBagLayout());
        interactionPanel.setBackground(BACKGROUND_C.darker());
        interactionPanel.setMinimumSize(interactionPanelSize);
        interactionPanel.setPreferredSize(interactionPanelSize);
        interactionPanel.setMaximumSize(interactionPanelSize);
        interactionPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C, 5));

        interactionPanel.add(taskPane, taskConstraints);
        interactionPanel.add(scheduledTimesPane, scheduledTimeConstraints);
        interactionPanel.add(modifyPanel, modifyConstraints);

        // Content pane
        CONTENT_PANE.setLayout(new BoxLayout(CONTENT_PANE, BoxLayout.PAGE_AXIS));
        CONTENT_PANE.setBackground(BACKGROUND_C);
        CONTENT_PANE.add(Box.createVerticalGlue());
        CONTENT_PANE.add(interactionPanel);
        CONTENT_PANE.add(Box.createVerticalGlue());

        this.revalidate();
        this.repaint();
    }

    /**
     * One big if-else statement containing logic for responding to button
     * presses throughout all the pages of the GUI.
     *
     * @param  event
     *         The event that triggered this method call
     */
    public void actionPerformed(ActionEvent event) {
        Component source = (JButton) event.getSource();

        if(source.equals(loginButton)) { // login button

            // Attempt to create an EwrScheduler instance with provided credentials
            ewrScheduler = loginToScheduler(
                    usernameField.getText(),
                    new String(passwordField.getPassword())
            );

            // Return if unsuccessful
            if(ewrScheduler == null) return;

            // Reconfigure JFrame and display schedule creation page
            this.setResizable(true);
            this.setMinimumSize(MIN_WIN_SIZE);
            this.setSize(MIN_WIN_SIZE);
            createScheduleGUI();

        } else if(source.equals(generateButton)) { // Build schedule button

            boolean backupRequired = false;
            boolean impossibleSchedule = false;
            // Attempt to build the schedule
            try {
                backupRequired = ewrScheduler.buildSchedule();
            } catch(IllegalStateException e) {
                impossibleSchedule = true;
            } catch(SQLException e) {
                displayError(this, "An unknown error occurred while creating the schedule.");
            } catch(IllegalArgumentException e) {
                displayError(this, "Database contains an invalid treatment.");
            }
            // Display schedule if no problems
            if(!(backupRequired || impossibleSchedule)) {
                displaySchedule(ewrScheduler.getSchedule());
                return;
            }
            // Prompt user for information if backup is needed
            if(backupRequired) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Backup volunteer required\n" +
                        "for hour " + ewrScheduler.getBackupHour() + ".\n\n" +
                        "Press OK once the backup\n" +
                        "volunteer has been called in.",
                        "Schedule Backup", JOptionPane.OK_CANCEL_OPTION);
                // Display schedule if user agrees
                if(result == JOptionPane.OK_OPTION) {
                    displaySchedule(ewrScheduler.getSchedule());
                    return;
                }
            }
            // Notify user if schedule cannot be created
            JOptionPane.showMessageDialog(null,
                    "Unable to schedule all\n" +
                    "treatments.\n" +
                    "Moving to treatment editor.",
                    "Treatments not scheduled",
                    JOptionPane.INFORMATION_MESSAGE);

            modifyTasksGUI(); // Display treatment editing GUI

        // Treatment modification buttons:
        } else if(source.equals(moveButton) || source.equals(removeButton)) {

            // Get user selected row and provide instructions if none selected
            int row = taskTable.getSelectedRow();
            if(row < 0) {
                modifyLabel.setText("Please select a treatment to modify");
                modifyLabel.setForeground(Color.red);
                return;
            }

            // Get data from selected row
            ArrayList<Object> rowData = ((TaskTableModel)taskTable.getModel()).getRow(row);
            int treatmentID = (Integer) rowData.get(0);
            String taskDescription = (String) rowData.get(1);

            // If user wants to move the selected Tasks start hour:
            if(source.equals(moveButton)) {
                // Get inputted start hour and provide instructions if invalid
                int newStartHour;
                try {
                    newStartHour = Integer.parseInt(newHourField.getText());
                } catch(NumberFormatException e) {
                 modifyLabel.setText("Enter a valid number from 0-23");
                 modifyLabel.setForeground(Color.red);
                    return;
                }
                if(newStartHour < 0 || newStartHour > 23) {
                    modifyLabel.setText("Enter a valid number from 0-23");
                    modifyLabel.setForeground(Color.red);
                    return;
                }

                // Prompt user to confirm change
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Confirm rescheduling of task \"" +
                        taskDescription + "\"",
                        "Confirm Reschedule",
                        JOptionPane.OK_CANCEL_OPTION
                );
                // Execute the user's request and return
                switch(result) {
                case JOptionPane.OK_OPTION:
                    try {
                        ewrScheduler.editTreatmentDB(
                                treatmentID,
                                Integer.parseInt(newHourField.getText())
                        );
                    } catch(SQLException e) {
                        displayError(
                                this,
                                "An unknown error occurred while rescheduling treatment."
                        );
                    }
                    rowData.set(4, newHourField.getText());
                    break;
                case JOptionPane.CANCEL_OPTION:
                    break;
                }
                modifyLabel.setText("Click on a row of the table to modify it");
                modifyLabel.setForeground(Color.black);
                taskTable.clearSelection();
                return;
            }
            // If user wants to remove the selected task:

            // Prompt user to confirm change
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm removal of task \"" +
                    taskDescription + "\"",
                    "Confirm Removal",
                    JOptionPane.OK_CANCEL_OPTION
            );
            // Execute the user's request and confirm
            switch(result) {
                case JOptionPane.OK_OPTION:
                    try {
                        ewrScheduler.removeFromDB(treatmentID);
                        ((TaskTableModel) taskTable.getModel()).removeRow(row);
                    } catch(SQLException e) {
                        displayError(
                                this,
                                "An unknown error occurred while removing treatment."
                        );
                    }
                    break;
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
            modifyLabel.setText("Click on a row of the table to modify it");
            modifyLabel.setForeground(Color.black);
            taskTable.clearSelection();

        } else if(source.equals(doneEditingButton)) { // Done editing button
            // Return to schedule creation page
            createScheduleGUI();

        } else if(source.equals(printButton)) { // Print to file button

            // Parse filename
            Pattern filePattern = Pattern.compile("^[\\w -]+\\.txt$");
            String filename = filenameField.getText().trim();
            Matcher fileMatcher = filePattern.matcher(filename);

            // Provide instructions if filename was not in valid format
            if(!fileMatcher.find()) {
                generateInstructions.setText(
                        "Filename must be in form 'filename.txt'");
                generateInstructions.setForeground(Color.red);
                return;
            }
            // Set filename to input (with spaces replaced by underscores)
            filename = fileMatcher.group().replace(' ', '_');
            // Create the file
            try {
                ewrScheduler.printSchedule(filename);
            } catch(IOException e) {
                displayError(this, "An unknown error occurred while creating the file.");
            }
            // Show confirmation
            JOptionPane.showMessageDialog(
                    null,
                    "File '" + filename + "' created",
                    "Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // Update GUI
            generateInstructions.setText("Process complete, exit program when ready");
            generateInstructions.setForeground(Color.black);
        }
    }

    /**
     * Unimplemented.
     */
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Unimplemented.
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Responds to user selection of a row of the task table on the treatment
     * editing page of the GUI
     *
     * @param  e
     *         The MouseEvent that triggered this method call
     */
    public void mouseReleased(MouseEvent e) {
        // Get data from the selected row
        int row = taskTable.getSelectedRow();
        ArrayList<Object> rowData = ((TaskTableModel)taskTable.getModel()).getRow(row);

        // Update the instructions to reflect the selection
        modifyLabel.setForeground(Color.black);
        modifyLabel.setText(
                "Selected treatment " +
                rowData.get(0) +
                ": " + rowData.get(1) +
                " (" + rowData.get(2) +
                ")");
    }

    /**
     * Unimplemented.
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Unimplemented.
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Creates an EwrScheduler instance and attempts to log in to the database.
     * Returns null if unsuccessful.
     *
     * @param  username
     *         user inputted username
     *
     * @param  password
     *         user inputted password
     */
    private EwrScheduler loginToScheduler(String username, String password) {
        // Create new instance of EwrScheduler
        EwrScheduler scheduler = new EwrScheduler(LocalDate.now(), username, password);
        // Attempt connection and provide message if unsuccessful
        try {
            scheduler.testDbConnection();
        /*} catch (CommunicationsException e) {
            loginInstructions.setText("Error: Unable to connect to database");
            loginInstructions.setForeground(Color.red);
            return null;*/
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if(errorCode == 0) {
                loginInstructions.setText("Error: Unable to connect to MySQL server");
            } else if(errorCode == 1045) {
                loginInstructions.setText("Incorrect username or password.");
            } else if(errorCode == 1044 ||
                    errorCode == 1046 ||
                    errorCode == 1049) {
                loginInstructions.setText("Error: Database not found");
            } else {
                loginInstructions.setText(
                        "Unexpected database error: MySQL#" + e.getErrorCode());
            }
            loginInstructions.setForeground(Color.red);
            return null;
        }

        // If successful, remove instructions and return EwrScheduler instance
        loginInstructions.setForeground(BACKGROUND_C);
        return scheduler;
    }

    /**
     * Helper function that creates a JScrollPane containing a JTable
     * which displays a list of tasks and their patients.
     *
     * @param  hourList
     *         List of tasks to be displayed in pane
     */
    private JScrollPane makeHourPane(ArrayList<Task> hourList) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Animal");
        columnNames.add("Task");

        // Create table rows from data in hourList
        Vector<Vector<String>> hourData = new Vector<>();
        Vector<String> tableRow;
        for(Task task : hourList) {
            tableRow = new Vector<>();
            tableRow.add(task.getPatient().getName());
            tableRow.add(task.getDescription());
            hourData.add(tableRow);
        }

        // Create and configure a JTable containing the created rows
        JTable hourTable = new JTable(new DefaultTableModel(hourData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        hourTable.setCellSelectionEnabled(false);
        hourTable.setGridColor(BACKGROUND_C.darker());
        hourTable.getTableHeader().setBackground(BACKGROUND_C);

        // Create and return a JScrollPane containing the JTable
        return new JScrollPane(hourTable);
    }

    /**
     * Fills the cells of the schedule grid on the schedule creation page with
     * panes created by the makeHourPane method.
     *
     * @param  scheduleList
     *         A 2d list containing lists of Tasks to be displayed for each hour
     */
    private void displaySchedule(ArrayList<Task>[] scheduleList) {
        // Make and add tables to each cell in the grid
        for(int i = 0; i < 24; i++) {
            hourTablePanels.get(i).add(makeHourPane(scheduleList[i]));
        }

        // Update the cell label for the hour when backup is scheduled
        if(ewrScheduler.isBackupScheduled()) {
            JLabel backupHourLabel = hourTableLabels.get(ewrScheduler.getBackupHour());
            backupHourLabel.setText(backupHourLabel.getText() + " + BACKUP");
        }

        // After the schedule is displayed, schedule creation is disabled and
        // schedule printing is enabled.
        generateButton.setEnabled(false);
        printButton.setEnabled(true);
        filenameField.setEnabled(true);
        filenameField.setText("ewrSchedule_" + LocalDate.now() + ".txt");
        generateInstructions.setText("Change filename if desired, then press print");
        this.revalidate();
        this.repaint();
    }

    /**
     * If the GUI experiences a fatal error, this method is called to notify the
     * user, and replace the JFrame with a new one.
     *
     * @param  oldGUI
     *         JFrame that experienced error
     *
     * @param  eMessage
     *         Message to display to user
     */
    public static void displayError(Interface oldGUI, String eMessage) {
        // Display message to user
        String message = eMessage + "\nReturning to login screen.";
        JOptionPane.showMessageDialog(
                null,
                message,
                "Big time uh-oh",
                JOptionPane.ERROR_MESSAGE
        );

        // Out with the old; in with the new.
        oldGUI.dispose();
        new Interface().setVisible(true);
    }

    /**
     * Main method, creates a new JFrame and sets it to visible.
     *
     * @param  args
     *         Command-line arguments (ignored)
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Interface().setVisible(true);
        });
    }
}

/**
 * Custom implementation of TableModel used to display tasks in the treatment
 * modification interface.
 */
class TaskTableModel implements TableModel {

    private final String[] COLUMN_NAMES = {
            "Treatment ID",
            "Task",
            "Animal Name",
            "Animal ID",
            "Window Start Hour",
            "Window Length",
            "Duration"};

    // 2d list where the rows contain data from Task objects
    private final ArrayList<ArrayList<Object>> TASK_TABLE;

    /**
     * Creates a new TableModel containing the data from the provided task list.
     *
     * @param  taskList
     *         List of tasks to be created into rows of the table
     */
    public TaskTableModel(ArrayList<Task> taskList) {
        TASK_TABLE = new ArrayList<>();

        for(Task task : taskList) {
            ArrayList<Object> row = new ArrayList<>();
            row.add(task.getTreatmentID());
            row.add(task.getDescription());
            row.add(task.getPatient().getName());
            row.add(task.getPatient().getID());
            row.add(task.getWindowStartHour());
            row.add(task.getWindowLength());
            row.add(task.getDuration());
            TASK_TABLE.add(row);
        }
    }

    /**
     * Returns a list containing the data in one row of the table.
     *
     * @param  row
     *         Row to be returned
     */
    public ArrayList<Object> getRow(int row) {
        return TASK_TABLE.get(row);
    }

    /**
     * Returns number of treatments being displayed by the table.
     */
    @Override
    public int getRowCount() {
        return TASK_TABLE.size();
    }

    /**
     * Always returns 7
     */
    @Override
    public int getColumnCount() {
        return 7;
    }

    /**
     * Returns the name of the given column.
     *
     * @param  columnIndex
     *         Index of the column whose name should be returned
     */
    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    /**
     * Returns the class of the given column.
     *
     * @param  columnIndex
     *         Index of the column whose class should be returned
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex > 6 || columnIndex < 0 ) return null;
        if(columnIndex >= 3 || columnIndex == 0) return Integer.class;
        return String.class;
    }

    /**
     * Removes the given row from the table.
     *
     * @param  rowIndex
     *         Index of row to be removed
     */
    public void removeRow(int rowIndex) {
        TASK_TABLE.remove(rowIndex);
    }

    /**
     * Always returns false.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Returns the Task object located at the given position in the table.
     *
     * @param  rowIndex
     *         Index of row
     *
     * @param  columnIndex
     *         Index of column
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return TASK_TABLE.get(rowIndex).get(columnIndex);
    }

    /**
     * Unimplemented.
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {  }

    /**
     * Unimplemented.
     */
    @Override
    public void addTableModelListener(TableModelListener l) {  }

    /**
     * Unimplemented.
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {  }
}
