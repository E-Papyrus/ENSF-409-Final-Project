/**
 @author Mariyah Malik
 @author Ethan Reed
 <a href="mailto:mariyah.malik@ucalgary.ca?cc=ethan.reed@ucalgary.ca">Email the authors</a>
 @version 0.3
 @since 0.1
 */

package edu.ucalgary.oop;

/*
Class to contain any relevant information about the tasks to be scheduled a
EWR. Contains logic to validate the state of any created objects.
*/
public class Task {
    private String description;
    private Animal patient;
    private int prepTime;
    private int duration;
    private int windowStartHour;
    private int windowEndHour;

    private boolean scheduled = false;

    // Constructor:
    public Task(String description, int prepTime, int duration, int windowStartHour, int windowEndHour) {
        this.description = description;
        this.prepTime = prepTime;
        this.duration = duration;
        this.windowStartHour = windowStartHour;
        this.windowEndHour = windowEndHour;
        // Throw an exception if Task was created with invalid data
        validateTask();
    }

    // Getters and Setters:
    public String getDescription() {
        return description;
    }
    public int getPrepTime() {
        return prepTime;
    }
    public int getDuration() {
        return duration;
    }
    public int getWindowStartHour() {
        return windowStartHour;
    }
    public int getWindowEndHour() {
        return windowEndHour;
    }
    public int getWindowLength() { return windowEndHour - windowStartHour; }
    public Animal getPatient() {
        return patient;
    }
    public void setPatient(Animal patient) {
        this.patient = patient;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
        validateTask();
    }
    public void setDuration(int duration) {
        this.duration = duration;
        validateTask();
    }
    public void setWindowStartHour(int windowStartHour) {
        this.windowStartHour = windowStartHour;
        validateTask();
    }
    public void setWindowEndHour(int windowEndHour) {
        this.windowEndHour = windowEndHour;
        validateTask();
    }
    public void setWindowHours(int windowStartHour, int windowEndHour) {
        this.windowStartHour = windowStartHour;
        this.windowEndHour = windowEndHour;
        validateTask();
    }

    // Return true if Task has been scheduled.
    public boolean isScheduled() {
        return scheduled;
    }

    // Sets scheduled.
    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    // A series of checks on the data members of a Task object. Throws an
    // IllegalArgumentException if the Task is in an invalid state.
    private void validateTask() {
        if(windowStartHour >= windowEndHour) {
            throw new IllegalArgumentException("Invalid task: window ends before it begins");
        }
        if((windowEndHour - windowStartHour) * 60 < duration + prepTime * (windowEndHour - windowStartHour)) {
            throw new IllegalArgumentException("Invalid task: too long for allotted window");
        }
        if(windowStartHour < 0 || windowStartHour > 23) {
            throw new IllegalArgumentException(
                    "Invalid task: window starts at invalid hour (" + windowStartHour + ")");
        }
        if(windowEndHour < 1 || windowEndHour > 24) {
            throw new IllegalArgumentException(
                    "Invalid task: window ends at invalid hour (" + windowEndHour + ")");
        }
        if(prepTime < 0 || duration <= 0) {
            throw new IllegalArgumentException("Invalid task: negative valued prep time and/or duration");
        }
    }
}
