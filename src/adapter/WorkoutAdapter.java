package com.example.gymprogressiontracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * WorkoutAdapter handles displaying a list of WorkoutSet objects in a RecyclerView.
 */
public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<WorkoutSet> workoutList;

    // Constructor to pass the data list to the adapter
    public WorkoutAdapter(List<WorkoutSet> workoutList) {
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate (create) our custom row layout (item_workout_row.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_row, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        // Get the specific workout set for this position in the list
        WorkoutSet currentSet = workoutList.get(position);

        // Bind (set) the data to the UI elements in the row
        holder.tvExerciseName.setText(currentSet.exerciseName);
        holder.tvDetails.setText(currentSet.weight + " lbs x " + currentSet.reps + " reps");

        // Set the checkbox state based on the data
        holder.cbComplete.setChecked(currentSet.isCompleted());

        // Handle when a user taps the checkbox
        holder.cbComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the data object
                currentSet.setCompleted(holder.cbComplete.isChecked());
                // NOTE: In a full app, you'd update the database here too!
            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size(); // Total number of items to show
    }

    // ViewHolder holds references to the views in our row layout
    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName;
        TextView tvDetails;
        CheckBox cbComplete;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvDetails = itemView.findViewById(R.id.tvWorkoutDetails);
            cbComplete = itemView.findViewById(R.id.cbComplete);
        }
    }
}
