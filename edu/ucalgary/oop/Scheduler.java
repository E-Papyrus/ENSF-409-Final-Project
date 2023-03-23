package edu.ucalgary.oop.edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Animal> animalList;
    private ArrayList<Task> taskList;
    private ArrayList<Task>[] hourlyTasks;
    private int[] hourlyFreeTime;

    public Scheduler(LocalDate date) {
        animalList = new ArrayList<>();
        taskList = new ArrayList<>();
        hourlyTasks = new ArrayList[24];
        for (int i = 0; i < 24; i++) {
            hourlyTasks[i] = new ArrayList<>();
        }
        hourlyFreeTime = new int[24];
    }

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

    public void printSchedule() {
        for (int i = 0; i < 24; i++) {
            System.out.println("Hour " + i + ":");
            for (Task task : hourlyTasks[i]) {
                System.out.println("\t" + task.getDescription());
            }
            System.out.println("\tFree time: " + hourlyFreeTime[i] + " minutes");
        }
    }

    private void buildTaskList() {

    }

    private void buildAnimalList() {

    }

    private void connectToDataBase() {

    }
}
