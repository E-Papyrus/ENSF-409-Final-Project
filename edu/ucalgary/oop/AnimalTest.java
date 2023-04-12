package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class AnimalTest {

    private Animal animal;

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
        setUp();
        assertEquals("Test Animal", animal.getName());
        assertEquals(1, animal.getID());
        assertEquals("Test Species", animal.getSpecies());
        assertEquals(new ArrayList<Task>(), animal.getTasks());
    }

    @Test 
    public void testSetName() {
        setUp();
        animal.setName("New Name");
        assertEquals("New Name", animal.getName());
    }

    @Test 
    public void testSetID() {
        setUp();
        animal.setID(2);
        assertEquals(2, animal.getID());
    }

    @Test 
    public void testSetTasks() {
        setUp();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Test Task", 0, 0, 0, 24));
        animal.setTasks(tasks);
        assertEquals(tasks, animal.getTasks());
    }

    @Test 
    public void testAddTask() {
        setUp();
        Task task = new Task("Test Task", 0, 0, 0, 24);
        animal.addTask(task);
        assertTrue(animal.getTasks().contains(task));
    }

    @Test 
    public void testRemoveTask() {
        setUp();
        Task task = new Task("Test Task", 0, 0, 0, 24);
        animal.addTask(task);
        animal.removeTask(0);
        assertFalse(animal.getTasks().contains(task));
    }

    @Test 
    public void testRemoveTaskThrowsException() {
        setUp();
        assertThrows(IndexOutOfBoundsException.class, () -> animal.removeTask(0));
    }

    @Test 
    public void testIsLitter() {
        setUp();
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
        assertFalse(individual.isLitter());

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
        assertTrue(litter.isLitter());
    }
}
