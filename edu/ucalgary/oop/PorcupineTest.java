package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class PorcupineTest {

    @Test 
    public void testPorcupineConstructor() {
        String name = "Percy";
        int ID = 1;
        Porcupine porcupine = new Porcupine(namee, ID);

        assertEquals(name, porcupine.getName());
        assertEquals(ID, porcupine.getID());
        assertEquals("Porcupine", porcupine.getSpecies());

        Task cleaning = porcupine.getTask(0);
        assertEquals("Cage cleaning", cleaning.getName());
        assertEquals(0, cleaning.getPrepTime());
        assertEquals(10, cleaning.getDuration());
        assertEquals(0, cleaning.getFrequency());
        assertEquals(24, cleaning.getStartTime());
        assertEquals(porcupine, cleaning.getPatient());
    }

    @Test 
    public void testPorcupineFeedingTimes() {
        Porcupine porcupine = new Porcupine("Percy", 1);

        assertEquals(5, porcupine.getFeedTime());
        assertEquals(0, porcupine.getPrepTime());
    }
}
