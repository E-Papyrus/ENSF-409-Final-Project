package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Fox extends NocturnalAnimal {
    private final int FEEDING_TIME = 5;
    private final int FEEDING_PREP_TIME = 5;
    private final int CLEANING_TIME = 5;

    public Fox(String name, int ID) {
        super(name, ID, "Fox");
    }
}