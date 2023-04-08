package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class BeaverTest {

    @Test 
    public void testConstructor() {
        Beaver beaver = new Beaver("Bucky", 1);
        assertEquals("Bucky", beaver.getName());
        assertEquals(1, beaver.getID());
        assertEquals("Beaver", beaver.getSpecies());
        assertEquals(1, beaver.getTasks().size());

        Task cleaningTask = beaver.getTasks().get(0);
        assertEquals("Beaver cage cleaning", cleaningTask.getDescription());
        assertEquals(0, cleaningTask.getPrepTime());
        assertEquals(5, cleaningTask.getDuration());
        assertEquals(0, cleaningTask.getStartHour());
        assertEquals(24, cleaningTask.getEndHour());
        assertEquals(beaver, cleaningTask.getPatient());
    }

    @Test 
    public void testGetFeedTime() {
        Beaver beaver = new Beaver("Bucky", 1);
        assertEquals(5, beaver.getFeedTime());
    }

    @Test 
    public void testGetPrepTime() {
        Beaver beaver = new Beaver("Bucky", 1);
        assertEquals(0, beaver.getPrepTime());
    }
}
