package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.beans.Transient;

public class RaccoonTest {

    @Test
    public void testRaccoonConstructor() {
        String name = "Rocky";
        int ID = 1;
        Raccoon raccoon = new Raccoon(name, ID);

        assertEquals(name, raccoon.getName());
        assertEquals(ID, raccoon.getID());
        assertEquals("Racoon", raccoon.getSpecies());

        Task cleaning = raccoon.getTask(0);
        assertEquals("Cage cleaning", cleaning.getName());
        assertEquals(0, cleaning.PrepTime());
        assertEquals(5, cleaning.getDuration());
        assertEquals(0, cleaning.getFrequency());
        assertEquals(24, cleaning.getStartTime());
        assertEquals(raccoon, cleaning.getPatient());
    }

    @Test 
    public void testRaccoonFeedingTimes() {
        Raccoon raccoon = new Raccoon("Rocky", 1);

        assertEquals(5, raccoon.getFeedTime());
        assertEquals(0, raccoon.getPrepTime());
    }
    
}
