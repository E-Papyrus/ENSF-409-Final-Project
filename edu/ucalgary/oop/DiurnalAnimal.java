/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.3
 @since 0.1
 */

package edu.ucalgary.oop;

/*
DiurnalAnimal is an abstract subclass of the abstract class Animal.
The class is intended to be extended by a number of concrete subclasses that
implement the abstract getter methods declared in Animal. Objects of this class
will contain a feeding Task if they are representative of an individual animal.
The feeding Task is created using default values from this class, as well as
values retrieved from its concrete subclass.
*/
public abstract class DiurnalAnimal extends Animal {
    // Default class values used in the creation of feeding Task objects:
    public static final int FEEDING_START = 8;
    public static final int FEEDING_END = 11;

    // Constructor:
    // Uses logic from the superclass to determine if the object is
    // representative of an individual or a litter. If it's an individual,
    // a feeding Task is created and added. Litters have custom feeding Tasks
    // defined in the database, so no default feeding Task is added during the
    // creation of the object.
    public DiurnalAnimal(String name, int id, String species) {
        super(name, id, species);
        if(this.isLitter()) {
            return;
        }
        Task feeding = new Task("Feeding", this.getPrepTime(), this.getFeedTime(), FEEDING_START, FEEDING_END);
        feeding.setPatient(this);
        this.addTask(feeding);
    }
}