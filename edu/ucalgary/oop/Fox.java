package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Fox extends NocturnalAnimal {
    public static final int FEEDING_TIME = 5;
    public static final int FEEDING_PREP_TIME = 5;
    public static final int CLEANING_TIME = 5;

    public Fox(String name, int ID) {
        super(name, ID, "Fox");
    }

    public int getFeedTime() { return this.FEEDING_TIME; }

    public int getPrepTime() { return this.FEEDING_PREP_TIME; }
}