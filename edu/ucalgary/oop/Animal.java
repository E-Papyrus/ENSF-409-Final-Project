package edu.ucalgary.oop;

import java.util.ArrayList;

public abstract class Animal {
    private String name;
    private int id;
    private String species;
    private ArrayList<Task> tasks;

    private int feedingStart;
    private int feedingEnd;

    private int feedingTime;
    private int feedingPrepTime;
    private int cleaningTime;

    public Animal(String name, int id, String species) {
        this.name = name;
        this.id = id;
        this.species = species;
        tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getSpecies() {
        return species;
    }

    public ArrayList<Task> getTasks() { return tasks; }

    public void addTask(Task Treatment) {

    }

    public void removeTask(int index) {

    }

    private boolean isLitter() {
        return true;
    }

    public abstract int getFeedTime();
    public abstract int getPrepTime();
}