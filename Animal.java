package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Animal {
    private String name;
    private int id;
    private String species;
    private ArrayList<Task> tasks;

    public Animal(String name, int id, String species) {
        this.name = name;
        this.id = id;
        this.species = species;
        tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getSpecies() {
        return species;
    }

}