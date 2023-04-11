package edu.ucalgary.oop;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Class responsible for the main functionality of the EWR scheduling application.
 * Contains lists of Task and Animal objects currently contained within the system.
 * Also contains an array of 24 lists which hold the Tasks scheduled for each hour,
 * the final schedule is built by gradually adding tasks to these lists. Tasks with
 * smaller windows are added first, as they are less flexible.
 *
 * @author      Mariyah Malik
 * @author      Ethan Reed
 * @version     1.0
 * @since       0.1
 */
public class EwrScheduler {
    private HashMap<Integer, Animal> patientMap; // Map of patients used in task building
    private ArrayList<Task> taskList; // Holds tasks not yet assigned to hours
    private ArrayList<Task>[] hourlyTasks; // Potential hourly schedules
    private ArrayList<Task>[] hourlyTasksLocked; // Actual hourly schedules
    private HashMap<String, Boolean>[] hourlyFeedingTasks; // Used to manage prep times
    private boolean[] lockedHours; // Stores locked status of hours
    private int[] hourlyMaxTime; // Stores total duration of tasks in hourly task lists
    private int[] hourlyScheduledTime; // Stores total duration of tasks in hourly locked task lists
    private final LocalDate DATE; // Date the schedule is being made for
    private boolean backupScheduled = false; // Set if backup is scheduled
    private int backupHour = 24; // Hour in which backup is scheduled

    // Credentials for super secure database:
    private Connection dbConnect;
    public final static String DB_URL = "jdbc:mysql://localhost:3306/ewr";
    private final String USERNAME;
    public final String PASSWORD;

    /**
     * Creates a new, uninitialized EwrScheduler with the provided database
     * credentials and date.
     *
     * @param  date
     *         The date that the schedule is being created for
     *
     * @param  username
     *         Name of database user
     *
     * @param  password
     *         Login password for database user
     */
    public EwrScheduler(LocalDate date, String username, String password) {
        this.DATE = date;
        this.PASSWORD = password;
        this.USERNAME = username;
    }

    /**
     * Builds the schedule one group of tasks at time, where tasks are grouped
     * by the length of their windows. Starts with the smallest windows length,
     * and moves up a length when the schedule is solved for the current group.
     *
     * @throws  SQLException
     *          If an SQL error occurs
     *
     * @throws  IllegalStateException
     *          Might be thrown when attempting to schedule backup
     *
     * @throws  IllegalArgumentException
     *          Might be thrown while building task list
     */
    public boolean buildSchedule() throws IllegalStateException, SQLException, IllegalArgumentException {
        // Reset data members and populate lists
        initialize();
        buildPatientMap();
        buildTaskLists();

        // Move first layer of tasks into hourly lists
        addNextLayer();

        boolean hoursUpdated; // Flag set if changes are made during a cycle
        int i;
        int hoursInDay = 24;

        while(true) {
            hoursUpdated = false; // Reset flag for new cycle
            countDurations(); // Reset hourly total durations for new cycle

            // Lock in any hours with 60 minutes or less assigned to them
            for (i = 0; i < hoursInDay; i++) {
                if (lockedHours[i]) continue;
                if (hourlyMaxTime[i] <= 60) {
                    hoursUpdated = true; // Changes made, so flag set
                    lockHourIn(i);
                }
            }
            if (hoursUpdated) continue; // Recount and go through hours again

            // Force an open hour to take some tasks if none were easily assigned
            for (i = 0; i < hoursInDay; i++) {
                if (!lockedHours[i]) {
                    lockHourIn(i);
                    break;
                }
            }
            // If all hours are locked, the group is either done or overbooked
            if (i == hoursInDay) {
                // If the hour lists still have task remaining, schedule backup
                if(scheduleBackup()) {
                    hoursInDay = 25; // More hours to loop through with backup
                    continue;
                }
                // This group is done, finish sorting if no more groups are left
                if (taskList.isEmpty()) break;
                // Or add the next group
                addNextLayer();
            }
        }
        return backupScheduled;
    }

    /**
     * Removes the treatment with the given id from the TREATMENTS table in
     * the EWR database.
     *
     * @param  treatmentID
     *         ID of treatment to be removed
     *
     * @throws  SQLException
     *          If an SQL error occurs
     */
    public void removeFromDB(int treatmentID) throws SQLException{
        connectToDataBase();

        String deleteQuery = String.format("DELETE FROM TREATMENTS WHERE TreatmentID=%d", treatmentID);
        PreparedStatement deleteStatement = dbConnect.prepareStatement(deleteQuery);
        deleteStatement.executeUpdate();

        disconnectFromDatabase();
    }

    /**
     * Updates the row with the given id in the TREATMENTS table in the EWR
     * database. Replaces old window start hour with newStartHour.
     *
     * @param  treatmentID
     *         ID of treatment to be moved
     *
     * @param  newStartHour
     *         New window start hour for treatment
     *
     * @throws  SQLException
     *          If an SQL error occurs
     */
    public void editTreatmentDB(int treatmentID, int newStartHour) throws  SQLException {
        connectToDataBase();

        String updateQuery = String.format("UPDATE TREATMENTS SET StartHour=%d WHERE TreatmentID=%d",newStartHour, treatmentID);
        PreparedStatement updateStatement = dbConnect.prepareStatement(updateQuery);
        updateStatement.executeUpdate();

        disconnectFromDatabase();
    }

    /**
     * Creates a text file containing the finalized schedule for the day.
     *
     * @param  filename
     *         Name of file to be created and/or written to
     *
     * @throws  IOException
     *          If an I/O error occurs
     */
    public void printSchedule(String filename) throws  IOException {
        BufferedWriter textFileOut;
        textFileOut = new BufferedWriter(new FileWriter(filename));

        String line;

        textFileOut.write(
                "|--------------------------------------------------------------|\n" +
                "|            Example Wildlife Rescue Daily Schedule            |\n" +
                "|--------------------------------------------------------------|\n");

        line = String.format("| Date: %-55s|\n", DATE);
        textFileOut.write(line);

        ArrayList<Task> hour;
        for (int i = 0; i < 24; i++) {
            hour = hourlyTasksLocked[i];
            line = String.format("|%63s\n|   %02d:00", "|", i);
            if(backupHour == i) {
                line += " [Backup volunteer scheduled] <------------------ !   |\n";
                hour.addAll(hourlyTasksLocked[24]);
            } else {
                line += "                                                      |\n";
            }
            textFileOut.write(line);
            if(hour.isEmpty()) {
                line = String.format("|   - %-57s|\n",
                        "Nothing scheduled for hour");
                textFileOut.write(line);
            }
            for (Task task : hour) {
                line = String.format("|   - %-57s|\n",
                        task.getDescription() + " (" +
                        task.getPatient().getName() + ")");
                textFileOut.write(line);
            }
        }
        textFileOut.write(
                "|                                                              |\n" +
                "|--------------------------------------------------------------|");
        textFileOut.close();
    }

    /**
     * Simply connects and disconnects to the database. Throws an exception if
     * any problems arise.
     *
     * @throws  SQLException
     *          If an SQL error occurs
     */
    public void testDbConnection() throws SQLException {
            dbConnect = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            dbConnect.close();
    }

    /**
     * Returns value of backupScheduled flag.
     */
    public boolean isBackupScheduled() {
        return backupScheduled;
    }

    /**
     * Returns value of field backupHour.
     */
    public int getBackupHour() { return this.backupHour; }

    /**
     * Returns object in field date.
     */
    public LocalDate getDate() { return this.DATE; }

    /**
     * Returns a list of all tasks that are in over-scheduled hours, including
     * the tasks scheduled in the backup hour. List does not include feeding
     * and cleaning tasks, which are not defined in the database and therefore
     * cannot be edited in the database.
     */
    public ArrayList<Task> getExcessiveList() {
        // Keep track of over-scheduled hours
        ArrayList<Integer> problemHours = new ArrayList<>();
        // Ensure that the tasks from the hour with backup are always added
        problemHours.add(24);
        problemHours.add(backupHour);
        // Add all hours that still have unscheduled tasks
        for(int i = 0; i < 24; i++) {
            if(i == backupHour) continue; // Already added
            lockHourIn(i); // Removes scheduled tasks from hourly lists
            if(!hourlyTasks[i].isEmpty()) {
                problemHours.add(i);
            }
        }

        ArrayList<Task> excessiveTasks = new ArrayList<>();
        // Add Tasks from all over-scheduled hours to list
        for(Integer hour : problemHours) {
            for(Task task : hourlyTasks[hour]) {
                if(excessiveTasks.contains(task) || task.getTreatmentID() == -1) continue;
                excessiveTasks.add(task);
            }
            for(Task task : hourlyTasksLocked[hour]) {
                if(excessiveTasks.contains(task) || task.getTreatmentID() == -1) continue;
                excessiveTasks.add(task);
            }
        }

        return excessiveTasks;
    }

    /**
     * Returns a copy of hourlyScheduledTime, with the minutes in the backup
     * hour added to the backup hour's real position.
     */
    public int[] getHourlyScheduledTime() {
        int[] copy = Arrays.copyOf(hourlyScheduledTime, 24);
        if(backupHour >= 0 && backupHour < 24) copy[backupHour] += hourlyScheduledTime[24];
        return copy;
    }

    /**
     * Returns a copy of hourlyTasksLocked, with the Tasks in the backup
     * hour added to the backup hour's real position.
     */
    public ArrayList<Task>[] getSchedule() {
        ArrayList<Task>[] copySchedule = Arrays.copyOf(hourlyTasksLocked, 24);
        if(backupHour >= 0 && backupHour < 24) copySchedule[backupHour].addAll(hourlyTasksLocked[24]);
        return copySchedule;
    }

    /**
     * Resets/initializes all list, maps, and arrays.
     * Resets all data members.
     */
    private void initialize() {
        // Initialize Lists and Maps:
        patientMap = new HashMap<>();
        taskList = new ArrayList<>();
        hourlyTasks = new ArrayList[25];
        for (int i = 0; i < 25; i++) {
            hourlyTasks[i] = new ArrayList<>();
        }
        hourlyFeedingTasks = new HashMap[25];
        for (int i = 0; i < 25; i++) {
            hourlyFeedingTasks[i] = new HashMap<>();
        }
        hourlyTasksLocked = new ArrayList[25];
        for (int i = 0; i < 25; i++) {
            hourlyTasksLocked[i] = new ArrayList<>();
        }
        // Initialize arrays:
        hourlyMaxTime = new int[25];
        Arrays.fill(hourlyMaxTime, 0);
        hourlyScheduledTime = new int[25];
        Arrays.fill(hourlyScheduledTime, 0);
        lockedHours = new boolean[25];
        Arrays.fill(lockedHours, false);

        // Reset all other data members/flags
        backupScheduled = false;
        backupHour = 24;
    }

    /**
     * Adds the next group of tasks from the main task list to the hour's lists.
     */
    private void addNextLayer() {
        // Find the shortest window length of the tasks remaining in taskList
        int shortestWindow = 25;
        for(Task task : taskList) {
            if(task.getWindowLength() < shortestWindow) {
                shortestWindow = task.getWindowLength();
            }
        }

        ListIterator<Task> iter = taskList.listIterator();
        Task task;
        int i;
        // Add all tasks with the shortest window length to hourly lists
        while(iter.hasNext()) {
            task = iter.next();
            if(task.getWindowLength() > shortestWindow) continue;
            for(i = task.getWindowStartHour(); i < task.getWindowEndHour(); i++) {
                hourlyTasks[i].add(task);
                // Unlock any hours with an updated task list and free time
                if(hourlyScheduledTime[i] < 60) {
                    lockedHours[i] = false;
                }
                // Add task to back up list if backupHour is within its window
                if(backupHour != i) continue;
                hourlyTasks[24].add(task);
                // Unlock back up list if it isn't full
                if(hourlyScheduledTime[24] < 60) {
                    lockedHours[24] = false;
                }
            }
            // Remove tasks added to hourly list from taskList
            iter.remove();
        }
    }

    /**
     * Populates taskList with the Tasks contained in each Animal object, and
     * the tasks in the database.
     *
     * @throws  SQLException
     *          If an SQL error occurs
     *
     * @throws  IllegalArgumentException
     *          If a task is created with invalid data
     */
    private void buildTaskLists() throws SQLException, IllegalArgumentException {
        // Create Hashmap to sort animals by species (to group like feeding tasks)
        HashMap<String, ArrayList<Animal>> aniMap = new HashMap<>();
        for(Animal patient : patientMap.values()) {
            if(!aniMap.containsKey(patient.getSpecies())) {
                aniMap.put(patient.getSpecies(), new ArrayList<>());
            }
            aniMap.get(patient.getSpecies()).add(patient);
        }

        // Add the tasks from the animals in aniMap
        for(ArrayList<Animal> row : aniMap.values()) {
            for(Animal patient : row){
                taskList.addAll(patient.getTasks());
            }
        }

        connectToDataBase();

        // Populate taskMap with incomplete Tasks
        Statement treatStmt = dbConnect.createStatement();
        Statement taskStmt = dbConnect.createStatement();
        ResultSet treatments = treatStmt.executeQuery("SELECT * FROM TREATMENTS");
        ResultSet taskRow;
        Task newTask;

        // Create tasks from data in table TREATMENTS
        while(!treatments.isLast()) {
            treatments.next();
            // Fetch row from TASKS with TaskID from TREATMENTS
            taskRow = taskStmt.executeQuery(
                    "SELECT * FROM TASKS WHERE TaskID=" +
                            treatments.getInt("TaskID"));
            taskRow.next();
            // Create new Task from row in TASKS
            newTask = new Task(
                    treatments.getInt("TreatmentID"),
                    taskRow.getString("Description"),
                    0,
                    taskRow.getInt("Duration"),
                    treatments.getInt("StartHour"),
                    24
            );
            // Set windowEndHour if it is less than 24
            if(newTask.getWindowStartHour() + taskRow.getInt("MaxWindow") < 24) {
                newTask.setWindowEndHour(
                        newTask.getWindowStartHour() + taskRow.getInt("MaxWindow")
                );
            }
            // Assign a patient to the Task
            newTask.setPatient(patientMap.get(treatments.getInt("AnimalID")));

            taskList.add(newTask);
        }
        treatStmt.close();
        taskStmt.close();

        disconnectFromDatabase();
    }

    /**
     * Populates patientMap with the data in the database.
     *
     * @throws  SQLException
     *          If an SQL error occurs
     */
    private void buildPatientMap() throws SQLException {
        connectToDataBase();
        Animal currentRow;


        // Fill resultSet with animal table
        Statement myStmt = dbConnect.createStatement();
        ResultSet results = myStmt.executeQuery("SELECT * FROM ANIMALS");

        // Convert each row of database to an Animal object and add to
        // patientMap
        while(!results.isLast()) {
            results.next();
            // Get species of entry and use the right constructor
            switch(results.getString("AnimalSpecies")) {
                case "fox":
                    currentRow = new Fox(
                            results.getString("AnimalNickname"),
                            results.getInt("AnimalID")
                    );
                    break;
                case "porcupine":
                    currentRow = new Porcupine(
                            results.getString("AnimalNickname"),
                            results.getInt("AnimalID")
                    );
                    break;
                case "beaver":
                    currentRow = new Beaver(
                            results.getString("AnimalNickname"),
                            results.getInt("AnimalID")
                    );
                    break;
                case "coyote":
                    currentRow = new Coyote(
                            results.getString("AnimalNickname"),
                            results.getInt("AnimalID")
                    );
                    break;
                case "raccoon":
                    currentRow = new Raccoon(
                            results.getString("AnimalNickname"),
                            results.getInt("AnimalID")
                    );
                    break;
                default:
                    throw new SQLDataException("Table ANIMALS contains row with invalid species");
            }
            patientMap.put(currentRow.getID(), currentRow);
        }
        myStmt.close();
        disconnectFromDatabase();
    }

    /**
     * Attempts to connect to the database.
     *
     * @throws  SQLException
     *          If an SQL error occurs
     */
    private void connectToDataBase() throws SQLException {
        dbConnect = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Attempts to disconnect from database
     *
     * @throws  SQLException
     *          If an SQL error occurs
     */
    private void disconnectFromDatabase() throws SQLException {
        if (dbConnect != null) dbConnect.close();
    }

    /**
     * Takes an integer hour as an argument, and schedules tasks to that hour's
     * locked task list. The locked task list holds tasks that are actually
     * scheduled to that hour.
     *
     * @param  hour
     *         Hour to be locked-in
     */
    private void lockHourIn(int hour) {
        int minutesScheduled = hourlyScheduledTime[hour];
        ListIterator<Task> iter = hourlyTasks[hour].listIterator();
        Task task;
        int prepTime;
        // Iterate through tasks in hourly list and lock some in
        while(iter.hasNext()) {
            task = iter.next();
            if(task.isScheduled()) { // Remove tasks scheduled to other hours
                iter.remove();
                continue;
            }
            // add prep time if no equivalent tasks have been added yet
            prepTime = 0;
            if(task.getPrepTime() > 0
                    && !hourlyFeedingTasks[hour].get(task.getDescription())) {
                prepTime = task.getPrepTime();
            }
            // Don't lock in tasks that will push scheduled minutes over 60
            if(minutesScheduled + task.getDuration() + prepTime > 60) continue;

            // Move tasks from hourly list to locked hourly list
            minutesScheduled += task.getDuration() + prepTime;
            task.setScheduled(true);

            // If adding task with prep time for first time, mark in HashMap
            if(task.getPrepTime() > 0
                    && !hourlyFeedingTasks[hour].get(task.getDescription())) {
                hourlyFeedingTasks[hour].replace(task.getDescription(), true);
            }
            // add task to locked list and remove from unlocked list
            hourlyTasksLocked[hour].add(task);
            iter.remove();
        }
        // Update minutes scheduled for hour, and lock hour in
        hourlyScheduledTime[hour] = minutesScheduled;
        lockedHours[hour] = true;
    }

    /**
     * Adds the durations of each task that can be slotted into each hour,
     * and stores them in hourlyMaxTime.
     */
    private void countDurations() {
        ArrayList<Task> hour;
        int hoursInDay = 24;
        if(backupScheduled) hoursInDay = 25;
        for(int i = 0; i < hoursInDay; i++) {
            hour = hourlyTasks[i];
            hourlyMaxTime[i] = hourlyScheduledTime[i];
            // Remove key-value pairs of unscheduled feeding tasks
            for(String feedingKey : hourlyFeedingTasks[i].keySet()) {
                if(hourlyFeedingTasks[i].get(feedingKey)) continue;
                hourlyFeedingTasks[i].remove(feedingKey);
            }
            for(Task task : hour) {
                if(task.getPrepTime() > 0) {
                    if(!hourlyFeedingTasks[i].containsKey(task.getDescription())) {
                        hourlyFeedingTasks[i].put(task.getDescription(), false);
                        hourlyMaxTime[i] += task.getPrepTime();
                    }
                }
                if(task.isScheduled()) continue;

                hourlyMaxTime[i] += task.getDuration();
            }
        }
    }

    /**
     * Decides which hour is most needing of a backup volunteer, then schedules
     * extra tasks to that hour. Returns false if nothing is scheduled, true
     * otherwise.
     *
     * @throws  IllegalStateException
     *          If attempting to schedule backup when backupScheduled flag is set
     */
    private boolean scheduleBackup() {
        int busiestHour = -1;
        int extraMinutes;
        int mostMinutes = 0;
        ArrayList<Task> hourList;
        // Look through hourly lists and find hour with the largest duration of
        // unscheduled tasks
        for(int i = 0; i < 24; i++) {
            hourList = hourlyTasks[i];
            extraMinutes = 0;
            for(Task task : hourList) {
                if(task.isScheduled()) continue;
                extraMinutes += task.getDuration();
            }
            if(extraMinutes <= mostMinutes) continue;
            busiestHour = i;
            mostMinutes = extraMinutes;
        }
        // Return false if no hours have additional tasks
        if(busiestHour == -1) return false;

        if(backupScheduled) {
            // Backup can only be scheduled once
            throw new IllegalStateException("Cannot fit all tasks");
        }
        backupScheduled = true; // Set flag if not already set

        // Assign back-up volunteer to decided hour
        backupHour = busiestHour;
        // Populate back-up hourly task list with list from decided hour
        hourlyTasks[24].addAll(hourlyTasks[busiestHour]);
        return true;
    }
}
