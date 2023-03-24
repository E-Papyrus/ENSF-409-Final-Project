package edu.ucalgary.oop.edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Raccoon extends NocturnalAnimal {
    public static final int FEEDING_TIME = 5;
    public static final int FEEDING_PREP_TIME = 0;
    public static final int CLEANING_TIME = 5;

    public Raccoon(String name, int ID) {
        super(name, ID, "Racoon");
    }

    public int getFeedTime() { return this.FEEDING_TIME; }

    public int getPrepTime() { return this.FEEDING_PREP_TIME; }
}