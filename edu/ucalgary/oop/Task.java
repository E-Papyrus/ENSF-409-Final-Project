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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setWindowStartHour(int windowStartHour) {
        this.windowStartHour = windowStartHour;
    }

    public void setWindowEndHour(int windowEndHour) {
        this.windowEndHour = windowEndHour;
    }

    private boolean validateTask() {
        return windowEndHour - windowStartHour >= duration + prepTime;
    }
}
