package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class Task {
    private String description;
    private String patientName;
    private int prepTime;
    private int duration;
    private int windowStartHour;
    private int windowEndHour;

    public Task(String description, int prepTime, int duration, int windowStartHour, int windowEndHour) {
        this.description = description;
        this.prepTime = prepTime;
        this.duration = duration;
        this.windowStartHour = windowStartHour;
        this.windowEndHour = windowEndHour;
        validateTask();
    }

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String name) {
        this.patientName = name;
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

    public void setWindowHours(int windowStartHour, int windowEndHour) {
        this.windowStartHour = windowStartHour;
        this.windowEndHour = windowEndHour;
        validateTask();
    }

    private void validateTask() {
        if(windowStartHour >= windowEndHour) {
            throw new IllegalArgumentException("Invalid task: window ends before it begins");
        }
        if((windowEndHour - windowStartHour) * 60 < duration + prepTime * (windowEndHour - windowStartHour)) {
            throw new IllegalArgumentException("Invalid task: too long for allotted window");
        }
        if(windowStartHour < 0 || windowStartHour > 23) {
            throw new IllegalArgumentException("Invalid task: window starts at invalid hour");
        }
        if(windowEndHour < 1 || windowEndHour > 24) {
            throw new IllegalArgumentException("Invalid task: window ends at invalid hour");
        }
        if(prepTime < 0 || duration <= 0) {
            throw new IllegalArgumentException("Invalid task: negative valued prep time and/or duration");
        }
    }
}
