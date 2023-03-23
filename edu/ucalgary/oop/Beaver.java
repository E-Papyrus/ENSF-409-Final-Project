package edu.ucalgary.oop.edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Beaver extends DiurnalAnimal {
    private final int FEEDING_TIME = 5;
    private final int FEEDING_PREP_TIME = 0;
    private final int CLEANING_TIME = 5;

    public Beaver(String name, int ID) {
        super(name, ID, "Beaver");
    }
}