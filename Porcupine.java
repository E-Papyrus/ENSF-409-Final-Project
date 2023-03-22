package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Porcupine extends CrepuscularAnimal {
    private final int FEEDING_TIME = 5;
    private final int FEEDING_PREP_TIME = 0;
    private final int CLEANING_TIME = 10;

    public Porcupine(String name, int ID) {
        super(name, ID, "Porcupine");
    }
}