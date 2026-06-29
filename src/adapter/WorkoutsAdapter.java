package com.example.gymprogressiontracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutViewHolder> {

    private final List<ExerciseCatalogItem> exerciseList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ExerciseCatalogItem exercise);
    }

    public WorkoutsAdapter(List<ExerciseCatalogItem> exerciseList, OnItemClickListener listener) {
        this.exerciseList = exerciseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_catalog, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        ExerciseCatalogItem exercise = exerciseList.get(position);
        holder.tvName.setText(exercise.getName());
        holder.tvCategory.setText(exercise.getDescription()); // Or a category if added to ExerciseCatalogItem
        holder.itemView.setOnClickListener(v -> listener.onItemClick(exercise));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExerciseName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}
