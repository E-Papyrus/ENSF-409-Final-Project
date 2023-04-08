/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.3
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Interface extends JFrame implements ActionListener, MouseListener {

    private EwrScheduler ewrScheduler;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel loginInstructions;
    private JButton loginButton;

    private JButton generateButton;
    private JButton printButton;
    private JTextField filenameField;

    private JTable taskTable;
    private JScrollPane taskPane;
    private JLabel modifyLabel;
    private JButton moveButton;
    private JButton removeButton;
    private JButton doneEditingButton;
    private JTextField newHourField;
    private JTable scheduledTimesTable;

    public final static Color BACKGROUND_C = new Color(0xF0EEE8);
    public final static Color FOREGROUND_C = new Color(0x4C8C5C);

    public Interface() {
        super("EWR Schedule Generator");
        loginGUI();
        //loginInstructions = new JLabel(); // TODO: remove line
        //ewrScheduler = loginToScheduler("oop", "password"); //TODO: remove line
        //createScheduleGUI(); // TODO: remove line
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void loginGUI() {
        loginInstructions = new JLabel("Enter username and password");
        loginInstructions.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        this.getContentPane().setBackground(BACKGROUND_C);

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
        Border fieldBorder = BorderFactory.createLineBorder(FOREGROUND_C);

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

        Container contentPane = this.getContentPane();

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.add(Box.createVerticalGlue());
        contentPane.add(logoPanel);
        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(loginPanel);
        contentPane.add(Box.createVerticalGlue());
    }

    public void createScheduleGUI() {
        this.setVisible(false);
        this.getContentPane().removeAll();

        this.getContentPane().setLayout(new FlowLayout());

        generateButton = new JButton("Build Schedule");
        generateButton.addActionListener(this);

        printButton = new JButton("Print");
        printButton.addActionListener(this);

        filenameField = new JTextField();
        this.getContentPane().add(generateButton);
        this.getContentPane().add(printButton);

        this.setVisible(true);
    }

    public void modifyTasksGUI() {
        this.setVisible(false);
        this.getContentPane().removeAll();
        this.setSize(825, 465);

        ArrayList<Task> movableTasks = ewrScheduler.getExcessiveList();

        taskTable = new JTable(new TaskTableModel(movableTasks));
        taskTable.setGridColor(Color.black);
        taskTable.setBackground(BACKGROUND_C.brighter());
        taskTable.getTableHeader().setBackground(BACKGROUND_C);
        taskTable.addMouseListener(this);

        String[] timeTableHeader = {"Hour", "Scheduled Minutes"};
        timeTableHeader[0] = "Hour:";
        scheduledTimesTable = new JTable(new DefaultTableModel(timeTableHeader, 24) {
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

        taskPane = new JScrollPane(taskTable);
        taskPane.setForeground(FOREGROUND_C.darker());
        taskPane.setMaximumSize(taskPaneSize);
        taskPane.setPreferredSize(taskPaneSize);
        taskPane.setMinimumSize(taskPaneSize);

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

        GridBagConstraints scheduledTimeConstraints = new GridBagConstraints();
        scheduledTimeConstraints.gridx = 0;
        scheduledTimeConstraints.gridy = 0;
        scheduledTimeConstraints.gridheight = 2;
        scheduledTimeConstraints.insets = new Insets(0, 0, 0, 10);

        modifyLabel = new JLabel("Click on a row of the table to modify it");
        JLabel orLabel = new JLabel("or");

        newHourField = new JTextField();
        newHourField.setPreferredSize(new Dimension(30, 25));

        moveButton = new JButton("Move window start");
        moveButton.addActionListener(this);

        removeButton = new JButton("Remove task");
        removeButton.addActionListener(this);

        doneEditingButton = new JButton("Done Editing");
        doneEditingButton.addActionListener(this);

        Dimension modifyPanelSize = new Dimension(600, 220);

        JPanel modifyPanel = new JPanel();
        modifyPanel.setLayout(new FlowLayout());
        modifyPanel.setMaximumSize(modifyPanelSize);
        modifyPanel.setMinimumSize(modifyPanelSize);
        modifyPanel.setBackground(BACKGROUND_C);
        modifyPanel.setBorder(BorderFactory.createLineBorder(FOREGROUND_C, 5));
        modifyPanel.add(modifyLabel);
        modifyPanel.add(newHourField);
        modifyPanel.add(moveButton);
        modifyPanel.add(removeButton);
        modifyPanel.add(doneEditingButton);

        GridBagConstraints modifyConstraints = new GridBagConstraints();
        modifyConstraints.gridx = 1;
        modifyConstraints.gridy = 1;
        modifyConstraints.fill = GridBagConstraints.BOTH;
        modifyConstraints.insets = new Insets(10, 10, 0, 0);

        Dimension interactionPanelSize = new Dimension(640, 405);

        JPanel interactionPanel = new JPanel(new GridBagLayout());
        interactionPanel.setBackground(BACKGROUND_C.darker());
        interactionPanel.add(taskPane, taskConstraints);
        interactionPanel.add(scheduledTimesPane, scheduledTimeConstraints);
        interactionPanel.add(modifyPanel, modifyConstraints);
        interactionPanel.setBorder(BorderFactory.createLineBorder(BACKGROUND_C.darker(), 20));

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.setBackground(BACKGROUND_C);
        contentPane.add(interactionPanel);

        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        Component source = (JButton) event.getSource();
        if(source.equals(loginButton)) {
            ewrScheduler = loginToScheduler(
                    usernameField.getText(),
                    new String(passwordField.getPassword())
            );

            if(ewrScheduler == null) return;
            createScheduleGUI();

        } else if(source.equals(generateButton)) {
            boolean backupRequired = false;
            boolean impossibleSchedule = false;
            try {
                backupRequired = ewrScheduler.buildSchedule();
            } catch(IllegalStateException e) {
                impossibleSchedule = true;
            }
            if(!(backupRequired || impossibleSchedule)) return;
            if(backupRequired) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Schedule is not possible\n" +
                        "without a backup volunteer.\n\n" +
                        "Press OK once the backup\n" +
                        "volunteer has been called in.",
                        "Schedule Backup", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION) return;
            }
            JOptionPane.showMessageDialog(null,
                    "Unable to schedule all\n" +
                    "treatments.\n" +
                    "Moving to treatment editor.",
                    "Treatments not scheduled",
                    JOptionPane.INFORMATION_MESSAGE);
            modifyTasksGUI();

        } else if(source.equals(moveButton)) {
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

            int row = taskTable.getSelectedRow();
            ArrayList<Object> rowData = ((TaskTableModel)taskTable.getModel()).getRow(row);
            int treatmentID = (Integer) rowData.get(0);
            String taskDescription = (String) rowData.get(1);
            int result = JOptionPane.showConfirmDialog(null, "Confirm rescheduling of task \"" + taskDescription + "\"", "Confirm Reschedule", JOptionPane.OK_CANCEL_OPTION);
            switch(result) {
                case JOptionPane.OK_OPTION:
                    ewrScheduler.editTreatmentDB(treatmentID, Integer.parseInt(newHourField.getText()));
                    rowData.set(4, newHourField.getText());
                    break;
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        } else if(source.equals(removeButton)) {
            int row = taskTable.getSelectedRow();
            ArrayList<Object> rowData = ((TaskTableModel)taskTable.getModel()).getRow(row);
            int treatmentID = (Integer) rowData.get(0);
            String taskDescription = (String) rowData.get(1);
            int result = JOptionPane.showConfirmDialog(null, "Confirm removal of task \"" + taskDescription + "\"", "Confirm Removal", JOptionPane.OK_CANCEL_OPTION);
            switch(result) {
                case JOptionPane.OK_OPTION:
                    ewrScheduler.removeFromDB(treatmentID);
                    ((TaskTableModel)taskTable.getModel()).removeRow(row);
                    break;
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        } else if(source.equals(doneEditingButton)) {
            createScheduleGUI();
        } else if(source.equals(printButton)) {
            ewrScheduler.printSchedule();
            JOptionPane.showMessageDialog(null, "Schedule printed.", "Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        int row = taskTable.getSelectedRow();
        ArrayList<Object> rowData = ((TaskTableModel)taskTable.getModel()).getRow(row);
        modifyLabel.setText("Selected treatment " + rowData.get(0));

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

    private ArrayList<ArrayList<Object>> taskTable;

    public TaskTableModel(ArrayList<Task> taskList) {
        taskTable = new ArrayList<>();

        for(Task task : taskList) {
            ArrayList<Object> row = new ArrayList<>();
            row.add(task.getTreatmentID());
            row.add(task.getDescription());
            row.add(task.getPatient().getName());
            row.add(task.getPatient().getID());
            row.add(task.getWindowStartHour());
            row.add(task.getWindowLength());
            row.add(task.getDuration());
            taskTable.add(row);
        }
    }

    public ArrayList<Object> getRow(int row) {
        return taskTable.get(row);
    }

    @Override
    public int getRowCount() {
        return taskTable.size();
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
        taskTable.remove(rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return taskTable.get(rowIndex).get(columnIndex);
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
