/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.3
 @since 0.1
 */

package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class TaskTest {

    @Test 
    public void testValidTask() {
     
        // Create a valid Task object
        Task task = new Task("Task description", 30, 60, 9, 12);
        
        // Check that the getters return the correct values 
        assertEquals("Task descriptions", task.getDescription());
        assertEquals(30, task.getPrepTime());
        assertEquals(60, task.getDuration());
        assertEquals(9, task.getWindowStartHour());
        assertEquals(12, task.getWWindowEndHour());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTaskEndsBeforeBegins() {
     
        //Create a Task with an invalid window
        Task task = new Task("Task description", 30, 60, 12, 9);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTaskTooLongForWindow() {
     
        // Create a Task with an invalid duration
        Task task = new Task ("Task description", 30, 120, 9, 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTaskWindowStartsAtInvalidHour() {
     
        // Create a Task with an invalid start hour
        Task task = new Task("Task description", 30, 60, -1, 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tesetInvalidTaskWindowEndsAtInvalidHour() {
     
        // Create a Task with an invalid end hour
        Task task = new Task ("Task description", 30, 60, 9, 25);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTaskNegativePrepTimeOrDuration() {
     
        // Create a Task with negative prep time
        Task task = new Task ("Task description", -10, 60, 9, 12);

        //Create a Task with negative duration
        task.setPrepTime(30);
        task.setDuration(-60);
    }
}
