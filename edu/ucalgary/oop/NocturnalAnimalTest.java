package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.beans.Transient;

public class NocturnalAnimalTest {

    private NocturnalAnimal individualAnimal;
    private NocturnalAnimal litterAnimal;

    @Before
    public void setUp() {
        individualAnimal = new NocturnalAnimal("Bob", 1, "Bat") {
            @Override
            public int getFeedTime() {
                return 2;
            }

            @Override
            public int getPrepTime() {
                return 1;
            }
        };

        litterAnimal = new NocturnalAnimal("Litter", 2, "Bat") {

            @Override
            public boolean isLitter() {
                return true;
            }

            @Override
            public int getFeedTime() {
                return 4;
            }

            @Override
            public int getPrepTime() {
                return 2;
            }
        };
    }

    @Test 
    public void individualAnimalHasFeedingTask() {
        assertEquals(1, individualAnimal.getTasks().size());

        Task feedingTask = individualAnimal.getTasks().get(0);
        assertEquals("Feeding", feedingTask.getDescription());
        assertEquals(1, feedingTask.getPrepTime());
        assertEquals(2, feedingTask.getDuration());
        assertEquals(0, feedingTask.getStartHour());
        assertEquals(3, feedingTask.getEndHour());
        assertEquals(individualAnimal, feedingTask.getPatient());
    }

    @Test
    public void litterAnimalHasNoFeedingTask() {
        assertEquals(0, litterAnimal.getTasks().size());
    }
}
