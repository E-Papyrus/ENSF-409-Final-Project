package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Coyote extends CrepuscularAnimal {
    public static final int FEEDING_TIME = 5;
    public static final int FEEDING_PREP_TIME = 10;
    public static final int CLEANING_TIME = 5;

    public Coyote(String name, int ID) {
        super(name, ID, "Coyote");
        this.addTask(new Task("Cage cleaning", 0, CLEANING_TIME, 0, 24));
    }

    public int getFeedTime() { return this.FEEDING_TIME; }

    public int getPrepTime() { return this.FEEDING_PREP_TIME; }
}