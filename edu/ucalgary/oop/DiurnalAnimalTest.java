package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.beans.Transient;

public class DiurnalAnimalTest {

    @Test
    public void testDiurnalAnimal() {
        DiurnalAnimal animal = new MockDiurnalAnimal("Test Animal", 123, "Test Species");

        assertEquals("Test Animal", animal.getName());
        assertEquals(123, animal.getId());
        assertEquals("Test Species", animal.getSpecies());
        assertTrue(animal.getPrepTime() >= 0);
        assertTrue(animal.getFeedTime() >= 0);

        // Test feeding task creation
        if (!animla.isLitter()) {
            assertEquals(1, animal.getTasks().size());

            Task task = animal.getTasks().get(0);
            assertEquals("Test Species feeding", task.getDescription());
            assertEquals(animal, task.getPatient());
            assertTrue(task.getPrepTime() >= 0);
            assertTrue(task.getDuration() >= 0);
            assertEquals(DiurnalAnimal.FEEDING_START, task.getStartTime());
            assertEquals(DiurnalAnimal.FEEDING_END, task.getEndTime());
        }
    }

    // a mock DiurnalAnimal class that extends DiurnalAnimal and overrides
    // the getPrepTime() and getFeedTime() methods return constant values

    private static class MockDiurnalAnimal extends DiurnalAnimal {
        public MockDiurnalAnimal(String name, int id, String species) {
            super(name, id, species);
        }

        @Override
        public int getPrepTime() {
            return 10;
        }

        @Override
        public int getFeedTime() {
            return 20;
        }
    }
}
