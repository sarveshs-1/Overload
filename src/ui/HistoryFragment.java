package com.example.gymprogressiontracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistoryLogs;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistoryLogs = view.findViewById(R.id.rvHistoryLogs);
        rvHistoryLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        db = AppDatabase.getInstance(requireContext());
        
        loadHistory();

        return view;
    }

    private void loadHistory() {
        new Thread(() -> {
            List<WorkoutSet> sets = db.workoutDao().getAllWorkoutsSorted();
            requireActivity().runOnUiThread(() -> {
                WorkoutAdapter adapter = new WorkoutAdapter(sets, this::loadHistory);
                rvHistoryLogs.setAdapter(adapter);
            });
        }).start();
    }
}
}
