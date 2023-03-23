package edu.ucalgary.oop.edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class DiurnalAnimal extends Animal {
    public static final int FEEDING_START = 8;
    public static final int FEEDING_END = 11;

    public DiurnalAnimal(String name, int id, String species) {
        super(name, id, species);
    }
}