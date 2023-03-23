package edu.ucalgary.oop.edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class NocturnalAnimal extends Animal {
    public static final int FEEDING_START = 0;
    public static final int FEEDING_END = 3;

    public NocturnalAnimal(String name, int id, String species) {
        super(name, id, species);
    }
}