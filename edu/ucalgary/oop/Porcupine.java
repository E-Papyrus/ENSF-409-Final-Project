/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.9
 @since 0.1
 */

package edu.ucalgary.oop;

/*
Porcupine is a concrete implementation of the abstract class CrepuscularAnimal.
All Porcupine objects contain identical cleaning Tasks, regardless of if the
object is representative of an individual or a litter.
There is also a default feeding Task, but whether a Porcupine object
contains this Task is decided by the superclass.
*/
public class Porcupine extends CrepuscularAnimal {
    // Default class values used in the creation of Task objects:
    public static final int FEEDING_TIME = 5;
    public static final int FEEDING_PREP_TIME = 0;
    public static final int CLEANING_TIME = 10;

    // Constructor:
    // Passes the arguments and species name to the superclass constructor, then
    // creates and adds the default cleaning Task.
    public Porcupine(String name, int ID) {
        super(name, ID, "Porcupine");
        Task cleaning = new Task("Porcupine cage cleaning", 0, CLEANING_TIME, 0, 24);
        cleaning.setPatient(this);
        this.addTask(cleaning);
    }

    // Getters used by the superclass to create a feeding Task
    public int getFeedTime() { return FEEDING_TIME; }
    public int getPrepTime() { return FEEDING_PREP_TIME; }
}