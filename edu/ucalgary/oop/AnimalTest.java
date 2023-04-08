package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.beans.Transient;
import java.util.ArrayList;

public class AnimalTest {

    private Animal animal;

    @BeforeEach
    public void setUp() {
        animal = new Animal("Test Animal", 1, "Test Species") {
            @Override
            public int getFeedTime() {
                return 0;
            }

            @Override
            public int getPrepTime() {
                return 0;
            }
        };
    }

    @Test 
    public void testConstructor() {
        Assertions.assertEquals("Test Animal", animal.getName());
        Assertions.assertEquals(1, animal.getID());
        Assertions.assertEquals("Test Species", animal.getSpecies());
        Assertions.assertEquals(new ArrayList<Task>(), animal.getTasks());
    }

    @Test 
    public void testSetName() {
        animal.setName("New Name");
        Assertions.assertEquals("New Name", animal.getName());
    }

    @Test 
    public void testSetID() {
        animal.setID(2);
        Assertions.assertEquals(2, animal.getID());
    }

    @Test 
    public void testSetSpecies() {
        animal.setSpecies("New Species");
        Assertions.assertEquals("New Species", animal.getSpecies());
    }

    @Test 
    public void testSetTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Test Task", 0, 0, 0, 24));
        animal.setTasks(tasks);
        Assertions.assertEquals(tasks, animal.getTasks());
    }

    @Test 
    public void testAddTask() {
        Task task = new Task("Test Task", 0, 0, 0, 24);
        animal.addTask(task);
        Assertions.assertTrue(animal.getTasks().contains(task));
    }

    @Test 
    public void testRemoveTask() {
        Task task = new Task("Test Task", 0, 0, 0, 24);
        animal.addTask(task);
        animal.removeTask(0);
        Assertions.assertFalse(animal.getTasks().contains(task));
    }

    @Test 
    public void testRemoveTaskThrowsException() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> animal.removeTask(0));
    }

    @Test 
    public void testIsLitter() {
        Animal individual = new Animal("Individual", 2, "Species") {

            @Override 
            public int getFeedTime() {
                return 0;
            }

            @Override
            public int getPrepTime() {
                return 0;
            }
        };
        Assertions.assertFalse(individual.isLitter());

        Animal litter = new Animal("Litter 1 and Litter 2", 3, "Species") {

            @Override
            public int getFeedTime() {
                return 0;
            }

            @Override
            public int getPrepTime() {
                return 0;
            }
        };
        Assertions.assertTrue(litter.isLitter());
    }
}
