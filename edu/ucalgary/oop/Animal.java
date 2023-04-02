/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.3
 @since 0.1
 */

package edu.ucalgary.oop;

import java.util.ArrayList;

/*
The Animal class is the highest level of abstraction for the classes used to
store information about the animals being taken care of at Example Wildlife
Rescue (EWR). Objects contain information about animal(s) they represent, as
well as a list of Task objects containing data about tasks that must be
performed as part of the animal's care.
*/
public abstract class Animal {
    private String name;
    private int id;
    private String species;
    private ArrayList<Task> tasks;

    // Constructor:
    public Animal(String name, int id, String species) {
        this.name = name;
        this.id = id;
        // throw an exception if the constructor was passed an invalid ID
        if(id <= 0) {
            throw new IllegalArgumentException("Animal created with non-positive ID");
        }
        this.species = species;
        tasks = new ArrayList<>();
    }

    // Getters and setters:
    public String getName() { return name; }
    public int getID() { return id; }
    public String getSpecies() { return species; }
    public ArrayList<Task> getTasks() { return tasks; }
    public void setName(String name) { this.name = name; }
    public void setID(int id) { this.id = id; }
    public void setSpecies(String species) { this.species = species; }
    public void setTasks(ArrayList<Task> tasks) { this.tasks = tasks; }

    // Appends a Task object to the end of tasks
    public void addTask(Task treatment) { tasks.add(treatment); }

    // Takes an integer and removes the Task object at that index in tasks.
    // Throws an exception if given an invalid index.
    public void removeTask(int index) {
        try {
            tasks.remove(index);
        } catch(IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(
                "Attempted to remove a task from task-list with invalid index");
        }
    }

    // Returns true if the name field contains " and ", which signifies that the
    // object contains information for a litter of animals.
    boolean isLitter() {
        return this.name.contains(" and ");
    }

    // getters to be implemented by subclasses
    public abstract int getFeedTime();
    public abstract int getPrepTime();
}