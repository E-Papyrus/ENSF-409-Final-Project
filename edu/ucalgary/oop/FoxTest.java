package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxTest {
    private Fox fox;

    @Before
    public void setUp() {
        fox = new Fox("Foxy", 123);
    }

    @Test
    public void testConstructor() {
        assertEquals("Foxy", fox.getName());
        assertEquals(123, fox.getID());
        assertEquals("fox", fox.getSpecies());
        assertEquals(1, fox.getTasks().size());

    }

    @Test
    public void testCleaningTask() {
        Task task = fox.getTasks().get(0);
        assertEquals("Cage cleaning", task.getDescription());
        assertEquals(0, task.getPrepTime());
        assertEquals(5, task.getDuration());
        assertEquals(0, task.getWindowStartHour());
        assertEquals(24, task.getWindowEndHour());
        assertEquals(fox, task.getPatient());
    }

    @Test
    public void testFeedingTime() {
        assertEquals(5, fox.getFeedTime());
    }

    @Test
    public void testPrepTime() {
        assertEquals(5, fox.getPrepTime());
    }
}
