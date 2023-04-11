package edu.ucalgary.oop;

/**
 * CrepuscularAnimal is an abstract subclass of the abstract class Animal.
 * The class is intended to be extended by a number of concrete subclasses that
 * implement the abstract getter methods declared in Animal. Objects of this class
 * will contain a feeding Task if they are representative of an individual animal.
 * The feeding Task is created using default values from this class, as well as
 * values retrieved from its concrete subclass
 *
 * @author      Mariyah Malik
 * @author      Ethan Reed
 * @version     1.0
 * @since       0.1
 */
public abstract class CrepuscularAnimal extends Animal {
    // Default class values used in the creation of feeding Task objects:
    public static final int FEEDING_START = 19;
    public static final int FEEDING_END = 22;

    /**
     * Used in creation of new Animal object. Uses logic from the superclass
     * to determine if the object is representative of an individual or a
     * litter. If it's an individual, a feeding Task is created and added.
     * Litters have custom feeding Tasks defined in the database, so no default
     * feeding Task is added during the creation of the object.
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
    public CrepuscularAnimal(String name, int id, String species) {
        super(name, id, species);
        if(this.isLitter()) {
          return;
        }
        Task feeding = new Task(species + " feeding", this.getPrepTime(), this.getFeedTime(), FEEDING_START, FEEDING_END);
        feeding.setPatient(this);
        this.addTask(feeding);
    }
}