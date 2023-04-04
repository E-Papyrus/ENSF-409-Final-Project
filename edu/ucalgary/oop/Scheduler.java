/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.3
 @since 0.1
 */

package edu.ucalgary.oop;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/*
Class responsible for the main functionality of the EWR scheduling application.
Contains lists of Task and Animal objects currently contained within the system.
Also contains an array of 24 lists which hold the Tasks scheduled for each hour,
the final schedule is built by gradually adding tasks to these lists. Tasks with
smaller windows are added first, as they are less flexible.
*/
public class Scheduler {
    private HashMap<Integer, Animal> patientMap; // Map of patients used in task building
    private ArrayList<Task> taskList; // Holds tasks not yet assigned to hours
    private ArrayList<Task>[] hourlyTasks; // Potential hourly schedules
    private ArrayList<Task>[] hourlyTasksLocked; // Actual hourly schedules
    private HashMap<String, Boolean>[] hourlyFeedingTasks; // Used to manage prep times
    private boolean[] lockedHours; // Stores locked status of hours
    private int[] hourlyMaxTime; // Stores total duration of tasks in hourly task lists
    private int[] hourlyScheduledTime; // Stores total duration of tasks in hourly locked task lists
    private LocalDate date; // date schedule is being made for

    // Credentials for super secure database:
    private Connection dbConnect;
    public final static String DBURL = "jdbc:mysql://localhost:3306/ewr";
    public final static String USERNAME = "student";
    public final static String PASSWORD = "ensf";

    // Constructor:
    public Scheduler(LocalDate date) {
        this.date = date;
        // Initialize Lists and Maps:
        patientMap = new HashMap<>();
        taskList = new ArrayList<>();
        hourlyTasks = new ArrayList[24];
        for (int i = 0; i < 24; i++) {
            hourlyTasks[i] = new ArrayList<>();
        }
        hourlyFeedingTasks = new HashMap[24];
        for (int i = 0; i < 24; i++) {
            hourlyFeedingTasks[i] = new HashMap<>();
        }
        hourlyTasksLocked = new ArrayList[24];
        for (int i = 0; i < 24; i++) {
            hourlyTasksLocked[i] = new ArrayList<>();
        }
        // Initialize arrays:
        hourlyMaxTime = new int[24];
        Arrays.fill(hourlyMaxTime, 0);
        hourlyScheduledTime = new int[24];
        Arrays.fill(hourlyScheduledTime, 0);
        lockedHours = new boolean[24];
        Arrays.fill(lockedHours, false);
    }

    // Builds the schedule one group of tasks at time, where tasks are grouped
    // by the length of their windows. Starts with the smallest windows length,
    // and moves up a length when the schedule is solved for the current group.
    public void buildSchedule() {
        // Populate lists
        buildPatientMap();
        buildTaskLists();
        // Move first layer of tasks into hourly lists
        addNextLayer();

        boolean hoursUpdated; // Flag set if changes are made during a cycle
        int i;

        while(true) {
            hoursUpdated = false; // Reset flag for new cycle
            countDurations(); // Reset hourly total durations for new cycle

            // Lock in any hours with 60 minutes or less assigned to them
            for (i = 0; i < 24; i++) {
                if (lockedHours[i]) continue;
                if (hourlyMaxTime[i] <= 60) {
                    hoursUpdated = true; // Changes made, so flag set
                    lockHourIn(i);
                }
            }
            if (hoursUpdated) continue; // Recount and go through hours again

            // Force an open hour to take some tasks if none were easily assigned
            for (i = 0; i < 24; i++) {
                if (!lockedHours[i]) {
                    lockHourIn(i);
                    break;
                }
            }
            // If all hours are locked, add next task group or overfill an hour
            if (i == 24) {
                // TODO: if i == 24 (find where to get backup)
                if (taskList.isEmpty()) break;
                addNextLayer();
            }
        }
        printSchedule();
    }

    // Adds the next group of tasks from the main task list to the hour's lists.
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
                // Unlock any hours with an updated task list
                lockedHours[i] = false;
            }
            // Remove tasks added to hourly list from taskList
            iter.remove();
        }
    }

    // Takes an integer hour as an argument, and schedules tasks to that hour's
    // locked task list. The locked task list holds tasks that are actually
    // scheduled to that hour.
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

    // Adds the durations of each task that can be slotted into each hour,
    // and stores them in hourlyMaxTime.
    private void countDurations() { // TODO: Change to consider prep
        ArrayList<Task> hour;
        for(int i = 0; i < 24; i++) {
            hour = hourlyTasks[i];
            hourlyMaxTime[i] = hourlyScheduledTime[i];
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

    // Creates a text file containing the finalized schedule for the day.
    public void printSchedule() { // TODO: temporarily prints to command line
        System.out.println("Schedule for " + date.toString() + "\n");
        ArrayList<Task> hour;
        for(int i = 0; i < 24; i++) {
            hour = hourlyTasksLocked[i];
            System.out.println("\t" + i + ":00");
            for(Task task : hour) {
                System.out.println("\t- " + task.getDescription() + " (" + task.getPatient().getName() + ")");
            }
            System.out.println();
        }
    }

    // Populates taskList with the Tasks contained in each Animal object, and
    // the tasks in the database.
    private void buildTaskLists() {
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
        try {
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
                    "SELECT * FROM TASKS WHERE TaskID=" + treatments.getInt("TaskID"));
                taskRow.next();
                // Create new Task from row in TASKS
                newTask = new Task(
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
        } catch (SQLException e) {
            e.printStackTrace();  // TODO: proper error handling
        }
        disconnectFromDatabase();
    }

    // Populates patientMap with the data in the database.
    private void buildPatientMap() {
        connectToDataBase();
        Animal currentRow;

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();  // TODO: proper error handling
        }
        disconnectFromDatabase();
    }

    // Attempts to connect to the database.
    private void connectToDataBase() {
        try{
            dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();  // TODO: proper error handling
        }
    }

    private void disconnectFromDatabase() {
        try { if (dbConnect != null) dbConnect.close(); } catch (SQLException e) {e.printStackTrace();}  // TODO: proper error handling
    }

    // If any hour in the day has a negative amount of unscheduled time, returns
    // true. This shows that a backup volunteer needs to be scheduled for that
    // hour.
    private boolean backupRequired() {
        return false;
    }

    public LocalDate getDate() { return this.date; }
}
