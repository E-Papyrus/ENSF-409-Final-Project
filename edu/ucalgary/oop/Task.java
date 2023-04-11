package edu.ucalgary.oop;

/**
 * Class to contain any relevant information about the tasks to be scheduled at
 * EWR. Contains logic to validate the state of any created objects.
 *
 * @author      Mariyah Malik
 * @author      Ethan Reed
 * @version     1.0
 * @since       0.1
 */
public class Task {
    private int treatmentID = -1;
    private String description;
    private Animal patient;
    private int prepTime;
    private int duration;
    private int windowStartHour;
    private int windowEndHour;

    private boolean scheduled = false;

    /**
     * Creates a new Task object with the default treatment ID.
     *
     * @param  description
     *         Description of task
     *
     * @param  prepTime
     *         Required prep time for task
     *
     * @param  duration
     *         Minutes required to complete task
     *
     * @param  windowStartHour
     *         First hour in which task can be scheduled
     *
     * @param  windowEndHour
     *         Hour after last in which task can be scheduled
     *
     * @throws IllegalArgumentException
     *         Thrown if task validation fails
     */
    public Task(String description, int prepTime, int duration, int windowStartHour, int windowEndHour)
    throws IllegalArgumentException {
        this.description = description;
        this.prepTime = prepTime;
        this.duration = duration;
        this.windowStartHour = windowStartHour;
        this.windowEndHour = windowEndHour;
        // Throw an exception if Task was created with invalid data
        validateTask();
    }

    /**
     * Creates a new Task object with custom TreatmentID.
     *
     * @param  treatmentID
     *         ID number of task
     *
     * @param  description
     *         Description of task
     *
     * @param  prepTime
     *         Required prep time for task
     *
     * @param  duration
     *         Minutes required to complete task
     *
     * @param  windowStartHour
     *         First hour in which task can be scheduled
     *
     * @param  windowEndHour
     *         Hour after last in which task can be scheduled
     *
     * @throws IllegalArgumentException
     *         Thrown if task validation fails
     */
    public Task(int treatmentID, String description, int prepTime, int duration, int windowStartHour, int windowEndHour) {
        this(description, prepTime, duration, windowStartHour, windowEndHour);
        this.treatmentID = treatmentID;
    }

    // Getters and Setters:
    public int getTreatmentID() { return treatmentID; }
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

    /**
     * Runs a series of checks on the data members of this Task object to check
     * the Task's validity.
     *
     * @throws  IllegalArgumentException
     *          If any of the checks fail
     */
    private void validateTask() {
        if(windowStartHour >= windowEndHour) {
            throw new IllegalArgumentException("Invalid task: window ends before it begins");
        }
        if((windowEndHour - windowStartHour) * 60 < duration + prepTime * (windowEndHour - windowStartHour)) {
            throw new IllegalArgumentException("Invalid task: too long for allotted window");
        }
        if(windowStartHour < 0) {
            throw new IllegalArgumentException(
                    "Invalid task: window starts at invalid hour (" + windowStartHour + ")");
        }
        if(windowEndHour > 24) {
            throw new IllegalArgumentException(
                    "Invalid task: window ends at invalid hour (" + windowEndHour + ")");
        }
        if(prepTime < 0 || duration <= 0) {
            throw new IllegalArgumentException("Invalid task: negative valued prep time and/or duration");
        }
    }
}
