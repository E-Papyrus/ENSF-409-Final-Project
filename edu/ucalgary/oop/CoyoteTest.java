package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.beans.Transient;

public class CoyoteTest {
    private Coyote coyote;

    @Before
    public void setUp() {
        coyote = new Coyote("Wiley", 123);
    }

    @Test 
    public void testCoyoteCleaningTask() {
        Task cleaning = new Task("Coyote cage cleaning", 0, 5, 0, 24);
        cleaning.setPatient(coyote);
        assertTrue(coyote.getTasks().contains(cleaning));
    }

    @Test
    public void testCoyoteFeedingTask() {
        Task feeding = new Task("Coyote feeding", 10, 5, 19, 22);
        feeding.setPatient(coyote);
        assertTrue(coyote.getTasks().contains(feeding));
    }

    @Test
    public void testCoyoteGetFeedTime() {
        assertEquals(5, coyote.getFeedTime());
    }

    @Test 
    public void testCoyoteGetPrepTime() {
        assertEquals(10, coyote.getPrepTime());
    }

    @Test 
    public void testCoyoteGetName() {
        assertEquals("Wiley", coyote.getName());
    }

    @Test 
    public void testCoyoteGetID() {
        assertEquals(123, coyote.getID());
    }

    @Test 
    public void testCoyoteGetSpecies() {
        assertEquals("Coyote", coyote.getSpecies());
    }
}
