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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

/*
Class responsible for the main functionality of the EWR scheduling application.
Contains lists of Task and Animal objects currently contained within the system.
Also contains an array of 24 lists which hold the Tasks scheduled for each hour,
and an array of 24 integers that keep count of the remaining unscheduled minutes
in each hour.
*/
public class Scheduler {
    private HashMap<Integer, Animal> animalList;
    private ArrayList<Task> taskList;
    private ArrayList<Task>[] hourlyTasks;
    private int[] hourlyFreeTime;
    private LocalDate date;

    private Connection dbConnect;
    private ResultSet results;
    private PreparedStatement statement;
    public final static String DBURL = "jdbc:mysql://localhost/EWR";
    public final static String USERNAME = "student";
    public final static String PASSWORD = "ensf";

    // Constructor:
    public Scheduler(LocalDate date) {
        this.date = date;
        animalList = new HashMap<>();
        taskList = new ArrayList<>();
        hourlyTasks = new ArrayList[24];
        for (int i = 0; i < 24; i++) {
            hourlyTasks[i] = new ArrayList<>();
        }
        hourlyFreeTime = new int[24];
    }

    // Attempts to place each of the Tasks in taskList in one of the hourly task
    // lists, while keeping the amount of scheduled time balanced between each
    // hour.
    public void buildSchedule() {
        buildAnimalList();
        buildTaskList();
        connectToDataBase();
        for (Task task : taskList) {
            if (hourlyFreeTime[task.getWindowStartHour()] >= task.getDuration() + task.getPrepTime()) {
                hourlyTasks[task.getWindowStartHour()].add(task);
                hourlyFreeTime[task.getWindowStartHour()] -= task.getDuration() + task.getPrepTime();
            }
        }
    }

    // Creates a text file of containing the finalized schedule for the day.
    public void printSchedule() {
        for (int i = 0; i < 24; i++) {
            System.out.println("Hour " + i + ":");
            for (Task task : hourlyTasks[i]) {
                System.out.println("\t" + task.getDescription());
            }
            System.out.println("\tFree time: " + hourlyFreeTime[i] + " minutes");
        }
    }

    // Populates taskList with the Tasks contained in each Animal object, and
    // the tasks in the database.
    private void buildTaskList() {
        // add complete Tasks from Animal objects to taskList
        for(Animal patient : animalList.values()) {
            taskList.addAll(patient.getTasks());
        }

        connectToDataBase();
        HashMap<Integer, Task> taskMap = new HashMap<>();
        Task currentRow;

        // Populate taskMap with incomplete Tasks
        try {
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM TASKS");

            // Fill taskMap with data from TASKS table
            while(!results.isLast()) {
                results.next();
                currentRow = new Task(
                        results.getString("Description"),
                        0,
                        results.getInt("Duration"),
                        0,
                        results.getInt("MaxWindow")
                );

                taskMap.put(results.getInt("TaskID"), currentRow);
            }
            myStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Task unfinished;
        // Using table TREATMENTS, complete Tasks in taskMap
        try {
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM TREATMENTS");

            //
            while(!results.isLast()) {
                results.next();
                if(!animalList.containsKey(results.getInt("AnimalID"))) {
                    throw new NoSuchElementException(
                            "Table TREATMENTS contains an AnimalID not in animalList");
                }
                if(!taskMap.containsKey(results.getInt("TaskID"))) {
                    throw new NoSuchElementException(
                            "Table TREATMENTS contains a TaskID not present in taskMap");
                }

                unfinished = taskMap.get(results.getInt("TaskID"));
                unfinished.setPatient(animalList.get(results.getInt("AnimalID")));
                unfinished.setWindowStartHour(results.getInt("StartHour"));

                taskList.add(unfinished);
            }
            myStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();
    }

    // Populates animalList with the data in the database.
    private void buildAnimalList() {
        connectToDataBase();
        Animal currentRow;

        try {
            // Fill resultSet with animal table
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM ANIMALS");

            // Convert each row of database to an Animal object and add to
            // animalList
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
                animalList.put(currentRow.getID(), currentRow);
            }
            myStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();
    }

    // Attempts to connect to the database.
    private void connectToDataBase() {
        try{
            dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void disconnectFromDatabase() {
        try { if (dbConnect != null) dbConnect.close(); } catch (SQLException e) {e.printStackTrace();}
    }

    // If any hour in the day has a negative amount of unscheduled time, returns
    // true. This shows that a backup volunteer needs to be scheduled for that
    // hour.
    private boolean backupRequired() {
        return false;
    }

    public LocalDate getDate() { return this.date; }
}
