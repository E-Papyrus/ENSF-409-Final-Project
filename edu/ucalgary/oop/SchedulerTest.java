package edu.ucalgary.oop;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SchedulerTest {

    private Task testTaskGood = new Task("testTask", 10, 20, 0, 3);
    private ArrayList<Task> testTasksGoodData = new ArrayList<>(List.of(new Task[]{
            testTaskGood,
            new Task("Tasktest", 0, 60, 10, 23)}));
    private Porcupine testAnimal = new Porcupine("Spike", 2);

    public SchedulerTest() { testAnimal.addTask(testTaskGood); }

    @Test
    public void testFoxConstructor() {
        Fox fox = new Fox("Tod", 1);
        assertNotNull("Fox constructor did not create an object when given a valid name and ID.", fox);
    }

    @Test
    public void testBeaverConstructor() {
        Beaver beaver = new Beaver("Beaver Dan", 2);
        assertNotNull("Beaver constructor did not create an object when given a valid name and ID.", beaver);
    }

    @Test
    public void testRaccoonConstructor() {
        Raccoon raccoon = new Raccoon("Bandit", 3);
        assertNotNull("Raccoon constructor did not create an object when given a valid name and ID.", raccoon);
    }

    @Test
    public void testCoyoteConstructor() {
        Coyote coyote = new Coyote("Wile E. Coyote", 4);
        assertNotNull("Coyote constructor did not create an object when given a valid name and ID.", coyote);
    }

    @Test
    public void testPorcupineConstructor() {
        Porcupine porcupine = new Porcupine("Poker", 5);
        assertNotNull("Porcupine constructor did not create an object when given a valid name and ID.", porcupine);
    }

    @Test
    public void testAnimalConstructorInvalidID() {
        boolean correctExceptionZero = false;
        boolean correctExceptionNegative = false;
        try {
            Fox fox = new Fox("Tod1", 0);
        } catch(IllegalArgumentException e) {
            correctExceptionZero = true;
        } catch(Exception e) {}

        try {
            Beaver beaver = new Beaver("Tod2", -1);
        } catch(IllegalArgumentException e) {
            correctExceptionNegative = true;
        } catch(Exception e) {}
        assertTrue("Animal constructor did not throw an IllegalArgumentException when given zero valued id.",
                correctExceptionZero);
        assertTrue("Animal constructor did not throw an IllegalArgumentException when given negative valued id.",
                correctExceptionNegative);
    }

    @Test
    public void testAnimalContainsFeedingTask() {
        Fox individual = new Fox("Tod", 1);
        boolean containsFeedingTask = false;

        for(Task task : individual.getTasks()) {
            if(task.getDescription().equals("Feeding")) {
                containsFeedingTask = true;
                break;
            }
        }

        assertTrue("Fox did not contain a Feeding task when created with a non-litter name.",
                containsFeedingTask);
    }

    @Test
    public void testAnimalContainsFeedingTaskLitterName() {
        Fox group = new Fox("Fox, Fox, and Fox", 1);
        boolean containsFeedingTask = false;

        for(Task task : group.getTasks()) {
            if(task.getDescription().equals("Feeding")) {
                containsFeedingTask = true;
                break;
            }
        }

        assertFalse("Fox did contain a Feeding task when created with a litter name.", containsFeedingTask);
    }

    @Test
    public void testCoyoteStaticValues() {
        // TODO:
        fail("unimplemented test");
    }

    @Test
    public void testBeaverStaticValues() {
        // TODO:
        fail("unimplemented test");
    }

    @Test
    public void testRaccoonStaticValues() {
        // TODO:
        fail("unimplemented test");
    }

    @Test
    public void testPorcupineStaticValues() {
        // TODO:
        fail("unimplemented test");
    }

    @Test
    public void testFoxStaticValues() {
        // TODO:
        fail("unimplemented test");
    }

    @Test
    public void testNocturnalAnimalStaticValues() {
        assertEquals("Class NocturnalAnimal does not contain correct hard-coded feeding start time.",
                0, NocturnalAnimal.FEEDING_START);
        assertEquals("Class NocturnalAnimal does not contain correct hard-coded feeding end time.",
                3, NocturnalAnimal.FEEDING_END);
    }

    @Test
    public void testDiurnalAnimalStaticValues() {
        assertEquals("Class DiurnalAnimal does not contain correct hard-coded feeding start time.",
                8, DiurnalAnimal.FEEDING_START);
        assertEquals("Class DiurnalAnimal does not contain correct hard-coded feeding end time.",
                11, DiurnalAnimal.FEEDING_END);
    }

    @Test
    public void testCrepuscularAnimalStaticValues() {
        assertEquals("Class CrepuscularAnimal does not contain correct hard-coded feeding start time.",
                19, CrepuscularAnimal.FEEDING_START);
        assertEquals("Class CrepuscularAnimal does not contain correct hard-coded feeding end time.",
                22, CrepuscularAnimal.FEEDING_END);
    }

    @Test
    public void testAnimalSubclassInheritance() { // TODO: Break apart into one test per subclass
        assertTrue("DiurnalAnimal does not extend Animal.",
                (Animal.class.isAssignableFrom(DiurnalAnimal.class)));
        assertTrue("NocturnalAnimal does not extend Animal.",
                (Animal.class.isAssignableFrom(NocturnalAnimal.class)));
        assertTrue("CrepuscularAnimal does not extend Animal.",
                (Animal.class.isAssignableFrom(CrepuscularAnimal.class)));
        assertTrue("Class Coyote does not extend CrepuscularAnimal.",
                (CrepuscularAnimal.class.isAssignableFrom(Coyote.class)));
        assertTrue("Class Porcupine does not extend CrepuscularAnimal.",
                (CrepuscularAnimal.class.isAssignableFrom(Porcupine.class)));
        assertTrue("Class Raccoon does not extend NocturnalAnimal.",
                (NocturnalAnimal.class.isAssignableFrom(Raccoon.class)));
        assertTrue("Class Fox does not extend NocturnalAnimal.",
                (NocturnalAnimal.class.isAssignableFrom(Fox.class)));
        assertTrue("Class Beaver does not extend DiurnalAnimal.",
                (DiurnalAnimal.class.isAssignableFrom(Beaver.class)));
    }

    @Test
    public void testGetName() {
        Fox fox = new Fox("Tod", 3);
        assertEquals("Method getName did not return the expected String.", "Tod", fox.getName());
    }

    @Test
    public void testGetID() {
        Fox fox = new Fox("Tod", 3);
        assertEquals("Method getID did not return the expected value.", 3, fox.getID());
    }

    @Test
    public void testGetSpecies() { // TODO: Break apart into one test per subclass
        Fox fox = new Fox("Tod", 3);
        assertEquals("Method getSpecies did not return the expected String for object of subclass Fox.",
                "fox", fox.getSpecies());
        Beaver beaver = new Beaver("Tod", 3);
        assertEquals("Method getSpecies did not return the expected String for object of subclass Beaver.",
                "beaver", beaver.getSpecies());
        Raccoon raccoon = new Raccoon("Tod", 3);
        assertEquals("Method getSpecies did not return the expected String for object of subclass Raccoon.",
                "raccoon", raccoon.getSpecies());
        Coyote coyote = new Coyote("Tod", 3);
        assertEquals("Method getSpecies did not return the expected String for object of subclass Coyote.",
                "coyote", coyote.getSpecies());
        Porcupine porcupine = new Porcupine("Tod", 3);
        assertEquals("Method getSpecies did not return the expected String for object of subclass Porcupine.",
                "porcupine", porcupine.getSpecies());
    }

    @Test
    public void testGetTasks() {
        assertTrue("", testAnimal.getTasks().contains(testTaskGood));
    }

    @Test
    public void testGetFeedTime() { // TODO: Should probably test getFeedTime for all classes that implement it
        int expectedValue = 5;
        int actualValue = testAnimal.getFeedTime();
        assertEquals("Method getFeedTime returned the incorrect value.", expectedValue, actualValue);
    }

    @Test
    public void testGetPrepTime() { // TODO: Should probably test getPrepTime for all classes that implement it
        int expectedValue = 0;
        int actualValue = testAnimal.getPrepTime();
        assertEquals("Method getPrepTime returned the incorrect value.", expectedValue, actualValue);
    }

    @Test
    public void testAddTask() {
        Coyote coyote = new Coyote("Mr.Tester", 5);
        coyote.addTask(testTaskGood);
        assertTrue("Method addTask did not add Task object to tasks list.",
                coyote.getTasks().contains(testTaskGood));
    }

    @Test
    public void testTaskConstructorGoodData() {
        Task task = null;
        try {
            task = new Task("Give the dog a bone", 10, 20, 10, 13);
        } catch(Exception e) { }

        assertNotNull("Task constructor did not create an object when given valid data.", task);
    }

    @Test
    public void testTaskConstructorDurationTooLong() {
        boolean correctException = false;
        try {
            Task task = new Task("Give the dog a bone", 10, 500, 10, 13);
        } catch(IllegalArgumentException e) {
            correctException = true;
        } catch(Exception e) { }

        assertTrue("Task constructor did not throw an IllegalArgumentException when given too long of a duration."
                , correctException);
    }

    @Test
    public void testTaskConstructorInvalidWindow() {
        boolean correctException = false;
        try {
            Task task = new Task("Give dog", 10, 20, 12, 5);
        } catch(IllegalArgumentException e) {
            correctException = true;
        } catch(Exception e) { }

        assertTrue("Task constructor did not throw an IllegalArgumentException when given an invalid window."
                , correctException);
    }

    @Test
    public void testTaskConstructorInvalidValues() {
        boolean correctException = false;
        try {
            Task task = new Task("Give dog", -1, 0, -1, 25);
        } catch(IllegalArgumentException e) {
            correctException = true;
        } catch(Exception e) { }

        assertTrue("Task constructor did not throw an IllegalArgumentException when given invalid values."
                , correctException);

    }

    @Test
    public void testRemoveTaskValidIndex() {
        boolean throwsException = false;
        Raccoon raccoon = new Raccoon("Simon N. Garfunkel", 7);
        try {
           raccoon.removeTask(1);
        } catch(Exception e) {
            throwsException = true;
        }

        assertFalse("Method removeTask threw an exception when given a valid index", throwsException);
    }

    @Test
    public void testRemoveTaskInvalidIndex() {
        boolean correctException = false;
        Raccoon raccoon = new Raccoon("Simon and Garfunkel", 7);
        try {
            raccoon.removeTask(1);
        } catch(IndexOutOfBoundsException e) {
            correctException = true;
        } catch(Exception e) { }

        assertTrue("Method removeTask did not throw an IndexOutOfBoundsException when given an invalid index",
                correctException);
    }
}
