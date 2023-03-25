package edu.ucalgary.oop;

import java.util.ArrayList;

public abstract class Animal {
    private String name;
    private int id;
    private String species;
    private ArrayList<Task> tasks;

    public Animal(String name, int id, String species) {
        this.name = name;
        this.id = id;
        if(id <= 0) {
            throw new IllegalArgumentException("Animal created with non-positive ID");
        }
        this.species = species;
        tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public String getSpecies() {
        return species;
    }

    public ArrayList<Task> getTasks() { return tasks; }

    public void addTask(Task treatment) {
        tasks.add(treatment);
    }

    public void removeTask(int index) {
        try {
            tasks.remove(index);
        } catch(IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Attempted to remove a task from task-list with invalid index");
        }
    }

    boolean isLitter() {
        return this.name.contains(" and ");
    }

    public abstract int getFeedTime();
    public abstract int getPrepTime();
}