package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class RaccoonTest {

    @Test
    public void testRaccoonConstructor() {
        String name = "Rocky";
        int ID = 1;
        Raccoon raccoon = new Raccoon(name, ID);

        assertEquals(name, raccoon.getName());
        assertEquals(ID, raccoon.getID());
        assertEquals("Racoon", raccoon.getSpecies());

        Task cleaning = raccoon.getTasks().get(0);
        assertEquals("Cage cleaning", cleaning.getDescription());
        assertEquals(0, cleaning.getPrepTime());
        assertEquals(5, cleaning.getDuration());
        assertEquals(0, cleaning.getWindowStartHour());
        assertEquals(24, cleaning.getWindowEndHour());
        assertEquals(raccoon, cleaning.getPatient());
    }

    @Test 
    public void testRaccoonFeedingTimes() {
        Raccoon raccoon = new Raccoon("Rocky", 1);

        assertEquals(5, raccoon.getFeedTime());
        assertEquals(0, raccoon.getPrepTime());
    }
    
}
