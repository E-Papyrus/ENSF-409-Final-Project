/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.8
 @since 0.1
 */

package edu.ucalgary.oop;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

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

public class Interface extends JFrame implements ActionListener, MouseListener {
    
    private final Container CONTENT_PANE;
    private final Dimension MIN_WIN_SIZE = new Dimension(1000, 700);
    private Border fieldBorder = BorderFactory.createLineBorder(FOREGROUND_C);

    private EwrScheduler ewrScheduler;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel loginInstructions;
    private JButton loginButton;

    private JButton generateButton;
    private JLabel generateInstructions;
    private JButton printButton;
    private JTextField filenameField;
    private ArrayList<JPanel> hourTablePanels;
    private ArrayList<JLabel> hourTableLabels;

    private JTable taskTable;
    private JLabel modifyLabel;
    private JButton moveButton;
    private JButton removeButton;
    private JButton doneEditingButton;
    private JTextField newHourField;


    public final static Color BACKGROUND_C = new Color(0xF0EEE8);
    public final static Color FOREGROUND_C = new Color(0x4C8C5C);

    public Interface() {
        super("EWR Schedule Generator");
        CONTENT_PANE = this.getContentPane();
        loginGUI();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void loginGUI() {
        this.setSize(600, 500);
        this.setResizable(false);
        loginInstructions = new JLabel("Enter username and password");
        loginInstructions.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        CONTENT_PANE.setBackground(BACKGROUND_C);

        ImageIcon logo = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel("Volunteer Task Scheduler");
        logoLabel.setIcon(logo);
        logoLabel.setForeground(FOREGROUND_C);
        logoLabel.setHorizontalTextPosition(JLabel.RIGHT);
        logoLabel.setVerticalTextPosition(JLabel.CENTER);

        Dimension textFieldDimension = new Dimension(250, 25);
        Dimension textPanelDimension = new Dimension(400, 25);
        FlowLayout textPanelLayout = new FlowLayout();
        textPanelLayout.setAlignment(FlowLayout.RIGHT);
        fieldBorder = BorderFactory.createLineBorder(FOREGROUND_C);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(textPanelLayout);
        usernamePanel.setMaximumSize(textPanelDimension);
        usernamePanel.setOpaque(false);

        usernameField = new JTextField();
        usernameField.setBackground(BACKGROUND_C.brighter());
        usernameField.setBorder(fieldBorder);
        usernameField.setPreferredSize(textFieldDimension);
        usernameField.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        usernamePanel.add(Box.createHorizontalGlue());
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.add(Box.createHorizontalStrut(33));

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(textPanelLayout);
        passwordPanel.setMaximumSize(textPanelDimension);
        passwordPanel.setOpaque(false);

        passwordField = new JPasswordField();
        passwordField.setBackground(BACKGROUND_C.brighter());
        passwordField.setBorder(fieldBorder);
        passwordField.setPreferredSize(textFieldDimension);
        passwordField.setAlignmentX(JPasswordField.CENTER_ALIGNMENT);
        passwordPanel.add(Box.createHorizontalGlue());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.add(Box.createHorizontalStrut(33));

        loginButton = new JButton("Log in");
        loginButton.addActionListener(this);
        loginButton.setAlignmentX((float)0.1);

        Dimension loginDimension = new Dimension(400, 200);

        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(true);
        loginPanel.setPreferredSize(loginDimension);
        loginPanel.setMaximumSize(loginDimension);
        loginPanel.setMinimumSize(loginDimension);
        loginPanel.setBackground(BACKGROUND_C);
        loginPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C, 5));

        Dimension logoDimension = new Dimension(400, 108);

        JPanel logoPanel = new JPanel();
        logoPanel.setMaximumSize(logoDimension);
        logoPanel.setPreferredSize(logoDimension);
        logoPanel.setMinimumSize(logoDimension);
        logoPanel.setBackground(BACKGROUND_C);

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

        logoPanel.add(logoLabel);
        logoPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        CONTENT_PANE.setLayout(new BoxLayout(CONTENT_PANE, BoxLayout.PAGE_AXIS));
        CONTENT_PANE.add(Box.createVerticalGlue());
        CONTENT_PANE.add(logoPanel);
        CONTENT_PANE.add(Box.createVerticalStrut(20));
        CONTENT_PANE.add(loginPanel);
        CONTENT_PANE.add(Box.createVerticalGlue());
    }

    public void createScheduleGUI() {

        CONTENT_PANE.removeAll();
        CONTENT_PANE.setLayout(new BorderLayout());

        generateButton = new JButton("Build schedule");
        generateButton.addActionListener(this);
        generateButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        printButton = new JButton("Print to file");
        printButton.addActionListener(this);
        printButton.setEnabled(false);
        printButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        GridLayout scheduleGrid = new GridLayout(6, 4, 2, 0);

        JPanel schedulePanel = new JPanel(scheduleGrid);
        schedulePanel.setBackground(BACKGROUND_C.darker());
        schedulePanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        hourTablePanels = new ArrayList<>();
        hourTableLabels = new ArrayList<>();

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

        Dimension filenameSize = new Dimension(200, 20);

        filenameField = new JTextField();
        filenameField.setEnabled(false);
        filenameField.setMaximumSize(filenameSize);
        filenameField.setPreferredSize(filenameSize);
        filenameField.setBorder(fieldBorder);

        filenameField.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        generateInstructions = new JLabel("Press button to build schedule");
        generateInstructions.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        Dimension buttonPanelSize = new Dimension(300, 670);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(generateInstructions);
        buttonPanel.add(Box.createVerticalStrut(100));
        buttonPanel.add(generateButton);
        buttonPanel.add(Box.createVerticalStrut(100));
        buttonPanel.add(filenameField);
        buttonPanel.add(printButton);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.setMinimumSize(buttonPanelSize);
        buttonPanel.setPreferredSize(buttonPanelSize);
        buttonPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        JLabel scheduleHeader = new JLabel("Today's Schedule");
        scheduleHeader.setForeground(BACKGROUND_C);
        scheduleHeader.setHorizontalTextPosition(JLabel.CENTER);
        scheduleHeader.setHorizontalAlignment(JLabel.CENTER);

        Dimension spacerSize = new Dimension(300, 30);

        JPanel spacer = new JPanel();
        spacer.setBackground(FOREGROUND_C.darker());
        spacer.setPreferredSize(spacerSize);
        spacer.setMaximumSize(spacerSize);

        Dimension headerSize = new Dimension(1000, 30);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(FOREGROUND_C.darker());
        headerPanel.setPreferredSize(headerSize);
        headerPanel.setMinimumSize(headerSize);
        headerPanel.add(spacer, BorderLayout.LINE_START);
        headerPanel.add(scheduleHeader, BorderLayout.CENTER);

        CONTENT_PANE.add(headerPanel, BorderLayout.PAGE_START);
        CONTENT_PANE.add(buttonPanel, BorderLayout.LINE_START);
        CONTENT_PANE.add(schedulePanel, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    private JScrollPane makeHourPane(ArrayList<Task> hourList) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Animal");
        columnNames.add("Task");

        Vector<Vector<String>> hourData = new Vector<>();
        Vector<String> tableRow;
        for(Task task : hourList) {
            tableRow = new Vector<>();
            tableRow.add(task.getPatient().getName());
            tableRow.add(task.getDescription());
            hourData.add(tableRow);
        }

        JTable hourTable = new JTable(new DefaultTableModel(hourData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        hourTable.setCellSelectionEnabled(false);
        hourTable.setGridColor(BACKGROUND_C.darker());
        hourTable.getTableHeader().setBackground(BACKGROUND_C);

        return new JScrollPane(hourTable);
    }

    private void displaySchedule(ArrayList<Task>[] scheduleList) {
        for(int i = 0; i < 24; i++) {
            hourTablePanels.get(i).add(makeHourPane(scheduleList[i]));
        }

        if(ewrScheduler.isBackupScheduled()) {
            JLabel backupHourLabel = hourTableLabels.get(ewrScheduler.getBackupHour());
            backupHourLabel.setText(backupHourLabel.getText() + " + BACKUP");
        }

        generateButton.setEnabled(false);
        printButton.setEnabled(true);
        filenameField.setEnabled(true);
        filenameField.setText("ewrSchedule_" + LocalDate.now() + ".txt");
        generateInstructions.setText("Change filename if desired, then press print");
        this.revalidate();
        this.repaint();
    }

    public void modifyTasksGUI() {
        CONTENT_PANE.removeAll();
        //this.setSize(825, 465);

        ArrayList<Task> movableTasks = ewrScheduler.getExcessiveList();

        taskTable = new JTable(new TaskTableModel(movableTasks));
        taskTable.setGridColor(Color.black);
        taskTable.setBackground(BACKGROUND_C.brighter());
        taskTable.getTableHeader().setBackground(BACKGROUND_C);
        taskTable.addMouseListener(this);

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

        int[] hourlyTime = ewrScheduler.getHourlyScheduledTime();
        for(int i = 0; i < 24; i++) {
            scheduledTimesTable.getModel().setValueAt(String.format("%02d:00", i), i, 0);
            scheduledTimesTable.getModel().setValueAt(hourlyTime[i], i, 1);
        }

        Dimension taskPaneSize = new Dimension(600, 165);

        JScrollPane taskPane = new JScrollPane(taskTable);
        taskPane.setForeground(FOREGROUND_C.darker());
        taskPane.setMaximumSize(taskPaneSize);
        taskPane.setPreferredSize(taskPaneSize);
        taskPane.setMinimumSize(taskPaneSize);
        taskPane.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        GridBagConstraints taskConstraints = new GridBagConstraints();
        taskConstraints.gridx = 1;
        taskConstraints.gridy = 0;
        taskConstraints.insets = new Insets(0, 10, 10, 0);

        Dimension scheduledTimesPaneSize = new Dimension(165, 405);

        JScrollPane scheduledTimesPane = new JScrollPane(scheduledTimesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scheduledTimesPane.setForeground(FOREGROUND_C.darker());
        scheduledTimesPane.setMaximumSize(scheduledTimesPaneSize);
        scheduledTimesPane.setPreferredSize(scheduledTimesPaneSize);
        scheduledTimesPane.setMinimumSize(scheduledTimesPaneSize);
        scheduledTimesPane.setBorder(BorderFactory.createLineBorder(FOREGROUND_C.darker(), 3));

        GridBagConstraints scheduledTimeConstraints = new GridBagConstraints();
        scheduledTimeConstraints.gridx = 0;
        scheduledTimeConstraints.gridy = 0;
        scheduledTimeConstraints.gridheight = 2;
        scheduledTimeConstraints.insets = new Insets(0, 0, 0, 10);

        modifyLabel = new JLabel("Click on a row of the table to modify it");
        modifyLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        modifyLabel.setHorizontalTextPosition(JLabel.CENTER);
        modifyLabel.setHorizontalAlignment(JLabel.CENTER);

        Dimension hourFieldSize = new Dimension(30, 20);

        JLabel moveHourLabel = new JLabel("Move window start to hour:");

        newHourField = new JTextField();
        newHourField.setMinimumSize(hourFieldSize);
        newHourField.setPreferredSize(hourFieldSize);
        newHourField.setMaximumSize(hourFieldSize);
        newHourField.setBorder(fieldBorder);

        moveButton = new JButton("Move");
        moveButton.addActionListener(this);
        
        JLabel removeHourLabel = new JLabel("Or remove treatment from database:");

        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);

        doneEditingButton = new JButton("Back to schedule creation");
        doneEditingButton.addActionListener(this);
        doneEditingButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        Dimension optionPanelSize = new Dimension(500, 30);

        JPanel moveHourPanel = new JPanel();
        moveHourPanel.setLayout(new FlowLayout());
        moveHourPanel.setMinimumSize(optionPanelSize);
        moveHourPanel.setPreferredSize(optionPanelSize);
        moveHourPanel.setMaximumSize(optionPanelSize);
        moveHourPanel.add(moveHourLabel);
        moveHourPanel.add(newHourField);
        moveHourPanel.add(moveButton);
        
        JPanel removeHourPanel = new JPanel();
        removeHourPanel.setLayout(new FlowLayout());
        removeHourPanel.setMinimumSize(optionPanelSize);
        removeHourPanel.setPreferredSize(optionPanelSize);
        removeHourPanel.setMaximumSize(optionPanelSize);
        removeHourPanel.add(removeHourLabel);
        removeHourPanel.add(removeButton);

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

        GridBagConstraints modifyConstraints = new GridBagConstraints();
        modifyConstraints.gridx = 1;
        modifyConstraints.gridy = 1;
        modifyConstraints.fill = GridBagConstraints.BOTH;
        modifyConstraints.insets = new Insets(10, 10, 0, 0);

        Dimension interactionPanelSize = new Dimension(825, 445);

        JPanel interactionPanel = new JPanel(new GridBagLayout());
        interactionPanel.setBackground(BACKGROUND_C.darker());
        interactionPanel.add(taskPane, taskConstraints);
        interactionPanel.add(scheduledTimesPane, scheduledTimeConstraints);
        interactionPanel.add(modifyPanel, modifyConstraints);
        interactionPanel.setBorder(BorderFactory.createLineBorder(BACKGROUND_C.darker(), 20));
        interactionPanel.setMinimumSize(interactionPanelSize);
        interactionPanel.setPreferredSize(interactionPanelSize);
        interactionPanel.setMaximumSize(interactionPanelSize);
        interactionPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C, 5));

        CONTENT_PANE.setLayout(new BoxLayout(CONTENT_PANE, BoxLayout.PAGE_AXIS));
        CONTENT_PANE.setBackground(BACKGROUND_C);
        CONTENT_PANE.add(Box.createVerticalGlue());
        CONTENT_PANE.add(interactionPanel);
        CONTENT_PANE.add(Box.createVerticalGlue());

        this.revalidate();
        this.repaint();
    }

    public void actionPerformed(ActionEvent event) {
        Component source = (JButton) event.getSource();
        if(source.equals(loginButton)) {
            ewrScheduler = loginToScheduler(
                    usernameField.getText(),
                    new String(passwordField.getPassword())
            );

            if(ewrScheduler == null) return;

            this.setResizable(true);
            this.setMinimumSize(MIN_WIN_SIZE);
            this.setSize(MIN_WIN_SIZE);
            createScheduleGUI();

        } else if(source.equals(generateButton)) {
            boolean backupRequired = false;
            boolean impossibleSchedule = false;
            try {
                backupRequired = ewrScheduler.buildSchedule();
            } catch(IllegalStateException e) {
                impossibleSchedule = true;
            } catch(SQLException e) {
                displayError(this, "An unknown error occured while creating schedule");
            } catch(IllegalArgumentException e) {
                displayError(this, "Database contains an invalid treatment.");
            }
            if(!(backupRequired || impossibleSchedule)) {
                displaySchedule(ewrScheduler.getSchedule());
                return;
            }
            if(backupRequired) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Backup volunteer required\n" +
                        "for hour " + ewrScheduler.getBackupHour() + ".\n\n" +
                        "Press OK once the backup\n" +
                        "volunteer has been called in.",
                        "Schedule Backup", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION) {
                    displaySchedule(ewrScheduler.getSchedule());
                    return;
                }
            }
            JOptionPane.showMessageDialog(null,
                    "Unable to schedule all\n" +
                    "treatments.\n" +
                    "Moving to treatment editor.",
                    "Treatments not scheduled",
                    JOptionPane.INFORMATION_MESSAGE);
            modifyTasksGUI();

        } else if(source.equals(moveButton) || source.equals(removeButton)) {
            int row = taskTable.getSelectedRow();

            if(row < 0) {
                modifyLabel.setText("Please select a treatment to modify");
                modifyLabel.setForeground(Color.red);
                return;
            }

            ArrayList<Object> rowData = ((TaskTableModel)taskTable.getModel()).getRow(row);
            int treatmentID = (Integer) rowData.get(0);
            String taskDescription = (String) rowData.get(1);

            if(source.equals(moveButton)) {
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

                int result = JOptionPane.showConfirmDialog(null, "Confirm rescheduling of task \"" + taskDescription + "\"", "Confirm Reschedule", JOptionPane.OK_CANCEL_OPTION);
                switch(result) {
                case JOptionPane.OK_OPTION:
                    try {
                        ewrScheduler.editTreatmentDB(treatmentID, Integer.parseInt(newHourField.getText()));
                    } catch(SQLException e) {
                        displayError(this, "An unknown error occurred while rescheduling treatment.");
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
            int result = JOptionPane.showConfirmDialog(null, "Confirm removal of task \"" + taskDescription + "\"", "Confirm Removal", JOptionPane.OK_CANCEL_OPTION);
            switch(result) {
                case JOptionPane.OK_OPTION:
                    try {
                        ewrScheduler.removeFromDB(treatmentID);
                        ((TaskTableModel) taskTable.getModel()).removeRow(row);
                    } catch(SQLException e) {
                        displayError(this, "An unknown error occurred while removing treatment.");
                    }
                    break;
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
            modifyLabel.setText("Click on a row of the table to modify it");
            modifyLabel.setForeground(Color.black);
            taskTable.clearSelection();

        } else if(source.equals(doneEditingButton)) {
            createScheduleGUI();

        } else if(source.equals(printButton)) {
            Pattern filePattern = Pattern.compile("^[\\w -]+\\.txt$");
            String filename = filenameField.getText().trim();
            Matcher fileMatcher = filePattern.matcher(filename);
            if(!fileMatcher.find()) {
                generateInstructions.setText("Filename must be in form 'filename.txt'");
                generateInstructions.setForeground(Color.red);
            }
            filename = fileMatcher.group().replace(' ', '_');
            try {
                ewrScheduler.printSchedule(filename);
            } catch(IOException e) {
                displayError(this, "An unknown error occurred while creating the file.");
            }
            generateInstructions.setText("Process complete, exit program when ready");
            generateInstructions.setForeground(Color.black);
            JOptionPane.showMessageDialog(null, "File '" + filename + "' created", "Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        int row = taskTable.getSelectedRow();
        ArrayList<Object> rowData = ((TaskTableModel)taskTable.getModel()).getRow(row);
        modifyLabel.setForeground(Color.black);
        modifyLabel.setText(
                "Selected treatment " +
                rowData.get(0) +
                ": " + rowData.get(1) +
                " (" + rowData.get(2) +
                ")");

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    private EwrScheduler loginToScheduler(String username, String password) {
        EwrScheduler scheduler = new EwrScheduler(LocalDate.now(), username, password);

        try {
            scheduler.testDbConnection();
        } catch (CommunicationsException e) {
            loginInstructions.setText("Error: Unable to connect to database");
            loginInstructions.setForeground(Color.red);
            return null;
        } catch (SQLException e) {
            loginInstructions.setText("Incorrect username or password.");
            loginInstructions.setForeground(Color.red);
            return null;
        }

        loginInstructions.setForeground(BACKGROUND_C);
        return scheduler;
    }

    public static void displayError(Interface oldGUI, String eMessage) {
        String message = eMessage + "\nReturning to login screen.";

        JOptionPane.showMessageDialog(
                null,
                message,
                "Big time uh-oh",
                JOptionPane.ERROR_MESSAGE
        );

        oldGUI.dispose();

        new Interface().setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Interface().setVisible(true);
        });
    }
}

class TaskTableModel implements TableModel {

    private final String[] COLUMN_NAMES = {
            "Treatment ID",
            "Task",
            "Animal Name",
            "Animal ID",
            "Window Start Hour",
            "Window Length",
            "Duration"};

    private final ArrayList<ArrayList<Object>> TASK_TABLE;

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

    public ArrayList<Object> getRow(int row) {
        return TASK_TABLE.get(row);
    }

    @Override
    public int getRowCount() {
        return TASK_TABLE.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex > 6 || columnIndex < 0 ) return null;
        if(columnIndex >= 3 || columnIndex == 0) return Integer.class;
        return String.class;
    }

    public void removeRow(int rowIndex) {
        TASK_TABLE.remove(rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return TASK_TABLE.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
