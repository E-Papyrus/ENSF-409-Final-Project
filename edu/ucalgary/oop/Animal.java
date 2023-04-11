package edu.ucalgary.oop;

import java.util.ArrayList;

/**
 * The Animal class is the highest level of abstraction for the classes used to
 * store information about the animals being taken care of at Example Wildlife
 * Rescue (EWR). Objects contain information about animal(s) they represent, as
 * well as a list of Task objects containing data about tasks that must be
 * performed as part of the animal's care.
 *
 * @author      Mariyah Malik
 * @author      Ethan Reed
 * @version     1.0
 * @since       0.1
 */
public abstract class Animal {
    private String name;
    private int id;
    private String species;
    private ArrayList<Task> tasks;

    /**
     * Creates a new Animal object.
     *
     * @param  name
     *         Name of patient animal
     *
     * @param  id
     *         The animal's id in the database
     *
     * @param  species
     *         The species of the animal
     */
    public Animal(String name, int id, String species) {
        this.name = name;
        this.id = id;
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

    /**
     * Appends a Task object to the end of tasks.
     *
     * @param  treatment
     *         Task to be added
     */
    public void addTask(Task treatment) { tasks.add(treatment); }

    /**
     * Takes an integer and removes the Task object at that index in tasks.
     *
     * @param  index
     *         Index of task to be removed
     *
     * @throws  IndexOutOfBoundsException
     *          If given an out-of-bounds index
     */
    public void removeTask(int index) throws IndexOutOfBoundsException {
        try {
            tasks.remove(index);
        } catch(IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(
                "Attempted to remove a task from task-list with invalid index");
        }
    }

    /**
     * Returns true if the name field contains " and ", which signifies that the
     * object contains information for a litter of animals.
     */
    boolean isLitter() {
        return this.name.contains(" and ");
    }

    // getters to be implemented by subclasses
    public abstract int getFeedTime();
    public abstract int getPrepTime();
}