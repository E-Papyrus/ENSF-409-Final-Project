package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class CrepuscularAnimalTest {

    @Test 
    public void testCrepuscularAnimal() {
        CrepuscularAnimal animal = new MockCrepuscularAnimal("Test Animla", 123, "Test Species");

        assertEquals("Test Animal", animal.getName());
        assertEquals(123, aanimal.getId());
        assertEquals("Test Species", animal.getSpecies());
        assertTrue(animal.getPrepTime() >= 0);
        assertTrue(animal.getFeedTime() >= 0);

        // test feeding task creation
        if (!animal.isLitter()) {
            assertEquals(1, animal.getTasks().size());

            Task task = animal.getTasks().get(0);
            assertEquals("Test Species feeding", task.getDescription());
            assertEquals(animal, task.getPatient());
            assertTrue(task.getPrepTime() >= 0);
            asssertTrue(task.getDuration() >= 0);
            assertEquals(CrepuscularAnimal.FEEDING_START, task.getStartTime());
            assertEquals(CrepuscularAnimal.FEEDING_END, task.getEndTime());
        }
    }

    // a mock CrepuscularAnimal class that extends CrepuscularAnimal and overrides
    // the getPrepTime() and getFeedTime() methods to return constant values

    private static class MockCrepuscularAnimal extends CrepuscularAnimal {
        public MockCrepuscularAnimal(String name, int id, String species) {
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
