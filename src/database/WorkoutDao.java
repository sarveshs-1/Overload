package com.example.gymprogressiontracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * DAO stands for Data Access Object. 
 * This is where we define the commands (SQL) to talk to the database.
 */
@Dao
public interface WorkoutDao {

    @Insert
    void insertWorkout(WorkoutSet workoutSet); // Adds a new log to the database

    @Query("SELECT * FROM workout_sets WHERE dayOfWeek = :day")
    List<WorkoutSet> getWorkoutsForDay(String day); // Gets all logs for a specific day
}
