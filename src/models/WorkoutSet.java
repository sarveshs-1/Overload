package com.example.gymprogressiontracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * WorkoutSet represents a single exercise set in the database.
 */
@Entity(tableName = "workout_sets")
public class WorkoutSet {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String dayOfWeek;
    public String exerciseName;
    public int weight;
    public int reps;
    public int sets;
    private boolean isCompleted;
    private long timestamp; // Saves the exact millisecond time the lift occurred
    private String exerciseDescription; // Stores catalog description if applicable

    // Optimized Constructor to auto-generate the timestamp and handle description
    public WorkoutSet(String dayOfWeek, String exerciseName, int weight, int reps, int sets, String exerciseDescription) {
        this.dayOfWeek = dayOfWeek;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.exerciseDescription = exerciseDescription;
        this.timestamp = System.currentTimeMillis(); // Automatically grabs current date/time!
        this.isCompleted = false;
    }

    // Getters and Setters
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public String getExerciseDescription() { return exerciseDescription; }
    public void setExerciseDescription(String exerciseDescription) { this.exerciseDescription = exerciseDescription; }
}
