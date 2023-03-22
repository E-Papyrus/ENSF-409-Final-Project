package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class CrepuscularAnimal extends Animal {
    public static final int FEEDING_START = 19;
    public static final int FEEDING_END = 22;

    public CrepuscularAnimal(String name, int id, String species) {
        super(name, id, species);
    }
}