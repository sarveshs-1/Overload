package com.example.gymprogressiontracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment {

    private RecyclerView rvWorkoutsIndex;
    private List<ExerciseCatalogItem> exerciseList;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        rvWorkoutsIndex = view.findViewById(R.id.rvWorkoutsIndex);
        rvWorkoutsIndex.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        db = AppDatabase.getInstance(requireContext());

        loadExerciseCatalog();

        WorkoutsAdapter adapter = new WorkoutsAdapter(exerciseList, exercise -> showExerciseDetailPopup(exercise));
        rvWorkoutsIndex.setAdapter(adapter);

        return view;
    }

    private void showExerciseDetailPopup(ExerciseCatalogItem item) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View popupView = getLayoutInflater().inflate(R.layout.dialog_exercise_details, null);
        dialog.setContentView(popupView);

        TextView tvName = popupView.findViewById(R.id.popupExerciseName);
        EditText etCustomName = new EditText(requireContext()); // Fallback for custom name
        TextView tvDesc = popupView.findViewById(R.id.popupDescription);
        ImageView ivGraphic = popupView.findViewById(R.id.popupImage);
        Spinner spinnerDay = popupView.findViewById(R.id.popupSpinnerDay);
        TextInputEditText etWeight = popupView.findViewById(R.id.popupEtWeight);
        TextInputEditText etReps = popupView.findViewById(R.id.popupEtReps);
        TextInputEditText etSets = popupView.findViewById(R.id.popupEtSets);
        Button btnAddSet = popupView.findViewById(R.id.btnPopupLog);

        if (item.getName().equals("Custom Workout")) {
            // Transform the Name TextView into an editable field if it's a custom workout
            tvName.setVisibility(View.GONE);
            ViewGroup parent = (ViewGroup) tvName.getParent();
            int index = parent.indexOfChild(tvName);
            
            etCustomName.setHint("Enter Exercise Name");
            etCustomName.setTextSize(22);
            etCustomName.setTypeface(null, android.graphics.Typeface.BOLD);
            parent.addView(etCustomName, index);
            
            tvDesc.setText("Enter your custom exercise details below.");
        } else {
            tvName.setText(item.getName());
            tvDesc.setText(item.getDescription());
        }

        if (item.getImageResourceId() != 0 && !item.getName().equals("Custom Workout")) {
            ivGraphic.setImageResource(item.getImageResourceId());
            ivGraphic.setVisibility(View.VISIBLE);
        } else {
            ivGraphic.setVisibility(View.GONE);
        }

        // Setup Spinner for Days
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);

        btnAddSet.setOnClickListener(v -> {
            String day = spinnerDay.getSelectedItem().toString();
            String weightStr = etWeight.getText().toString().trim();
            String repsStr = etReps.getText().toString().trim();
            String setsStr = etSets.getText().toString().trim();

            if (weightStr.isEmpty() || repsStr.isEmpty() || setsStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter weight, reps, and sets", Toast.LENGTH_SHORT).show();
                return;
            }

            int weight = Integer.parseInt(weightStr);
            int reps = Integer.parseInt(repsStr);
            int sets = Integer.parseInt(setsStr);
            
            String finalName = item.getName();
            if (item.getName().equals("Custom Workout")) {
                finalName = etCustomName.getText().toString().trim();
                if (finalName.isEmpty()) finalName = "Custom Exercise";
            }

            WorkoutSet newSet = new WorkoutSet(day, finalName, weight, reps, sets, item.getDescription());
            
            final String toastName = finalName;
            new Thread(() -> {
                db.workoutDao().insertWorkout(newSet);
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), toastName + " added to " + day + "!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }).start();
        });

        dialog.show();
    }

    private void loadExerciseCatalog() {
        exerciseList = new ArrayList<>();
        int icon = 0;
        int customIcon = 0;

        // --- CUSTOM ---
        exerciseList.add(new ExerciseCatalogItem("Custom Workout", "Tap to create your own unique exercise and log it immediately.", customIcon));

        // --- CHEST ---
        exerciseList.add(new ExerciseCatalogItem("Bench Press", "1. Lie flat on a bench.\n2. Grip the bar slightly wider than shoulder-width.\n3. Lower the bar to your chest control.\n4. Press back up.", 0));
        exerciseList.add(new ExerciseCatalogItem("Incline Bench Press", "1. Set bench to a 30–45° incline.\n2. Lower the weight to your upper chest.\n3. Press upward explosively.", icon));
        exerciseList.add(new ExerciseCatalogItem("Dumbbell Chest Press", "1. Lie flat on a bench holding dumbbells.\n2. Lower dumbbells beside your chest.\n3. Press upward together.", icon));
        exerciseList.add(new ExerciseCatalogItem("Chest Fly", "1. Hold dumbbells above your chest.\n2. Lower arms out wide with a slight bend.\n3. Bring weights back together.", icon));
        exerciseList.add(new ExerciseCatalogItem("Push-Up", "1. Place hands wider than shoulder width.\n2. Lower chest toward the floor.\n3. Push back up.", icon));

        // --- BACK ---
        exerciseList.add(new ExerciseCatalogItem("Pull-Up", "1. Hang from a bar with palms facing away.\n2. Pull yourself up until chin clears the bar.\n3. Lower down slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Lat Pulldown", "1. Sit at machine and grip bar wide.\n2. Pull bar to upper chest.\n3. Return slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Barbell Row", "1. Bend forward at hips with a flat back.\n2. Pull bar toward lower chest.\n3. Lower under control.", icon));
        exerciseList.add(new ExerciseCatalogItem("Seated Cable Row", "1. Sit with feet braced.\n2. Pull handle toward stomach.\n3. Squeeze shoulder blades.", icon));
        exerciseList.add(new ExerciseCatalogItem("Deadlift", "1. Stand with bar over midfoot.\n2. Bend hips and knees to grip bar.\n3. Stand up driving through feet.", icon));

        // --- SHOULDERS ---
        exerciseList.add(new ExerciseCatalogItem("Overhead Press", "1. Hold bar at upper shoulder level.\n2. Press bar straight overhead.\n3. Lower back to shoulders.", icon));
        exerciseList.add(new ExerciseCatalogItem("Dumbbell Shoulder Press", "1. Hold dumbbells at sides of shoulders.\n2. Press overhead together.\n3. Lower slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Lateral Raise", "1. Hold dumbbells at sides.\n2. Raise arms laterally to shoulder height.\n3. Lower slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Front Raise", "1. Hold dumbbells in front of thighs.\n2. Raise directly to shoulder height.\n3. Lower under control.", icon));
        exerciseList.add(new ExerciseCatalogItem("Rear Delt Fly", "1. Bend forward slightly at hips.\n2. Raise dumbbells out wide to sides.\n3. Squeeze rear shoulder blades.", icon));

        // --- LEGS ---
        exerciseList.add(new ExerciseCatalogItem("Squat", "1. Place bar across upper back traps.\n2. Sit back and down breaking hips.\n3. Lower until thighs are parallel.\n4. Stand up.", icon));
        exerciseList.add(new ExerciseCatalogItem("Leg Press", "1. Place feet shoulder-width on plate.\n2. Lower platform until knees reach 90°.\n3. Press back up.", icon));
        exerciseList.add(new ExerciseCatalogItem("Lunges", "1. Step forward.\n2. Lower until both knees reach 90°.\n3. Push back to starting position.", icon));
        exerciseList.add(new ExerciseCatalogItem("Bulgarian Split Squat", "1. Place rear foot on a bench.\n2. Lower into deep lunge.\n3. Drive through front heel to stand.", icon));
        exerciseList.add(new ExerciseCatalogItem("Leg Extension", "1. Sit in machine.\n2. Extend knees fully.\n3. Lower slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Leg Curl", "1. Lay or sit in machine.\n2. Curl weight toward glutes.\n3. Release down slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Calf Raise", "1. Stand on edge of platform.\n2. Raise heels as high as possible.\n3. Lower below platform level.", icon));

        // --- BICEPS ---
        exerciseList.add(new ExerciseCatalogItem("Barbell Curl", "1. Hold bar with palms up.\n2. Curl bar toward shoulders.\n3. Lower slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Dumbbell Curl", "1. Hold dumbbells at sides.\n2. Curl upward rotating palms up.\n3. Lower under control.", icon));
        exerciseList.add(new ExerciseCatalogItem("Hammer Curl", "1. Hold dumbbells with neutral grip.\n2. Curl upward keeping palms facing.\n3. Lower slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Preacher Curl", "1. Rest arms on preacher bench.\n2. Curl weight up isolated.\n3. Lower all the way down.", icon));

        // --- TRICEPS ---
        exerciseList.add(new ExerciseCatalogItem("Tricep Pushdown", "1. Grip cable handle.\n2. Push downward until arms are straight.\n3. Return slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Skull Crushers", "1. Lie on bench holding barbell up.\n2. Lower weight toward forehead.\n3. Extend elbows back up.", icon));
        exerciseList.add(new ExerciseCatalogItem("Dips", "1. Support yourself on dip bars.\n2. Lower until elbows reach 90°.\n3. Push back up.", icon));
        exerciseList.add(new ExerciseCatalogItem("Overhead Tricep Extension", "1. Hold weight overhead with both hands.\n2. Lower slowly behind head.\n3. Extend arms straight up.", icon));

        // --- CORE ---
        exerciseList.add(new ExerciseCatalogItem("Plank", "1. Support weight on forearms and toes.\n2. Keep body completely straight.\n3. Hold static position.", icon));
        exerciseList.add(new ExerciseCatalogItem("Crunch", "1. Lie on back with knees bent.\n2. Lift shoulders off floor flexing abs.\n3. Lower slowly.", icon));
        exerciseList.add(new ExerciseCatalogItem("Hanging Leg Raise", "1. Hang from bar.\n2. Raise straight legs until parallel to floor.\n3. Lower back down.", icon));
        exerciseList.add(new ExerciseCatalogItem("Russian Twist", "1. Sit with feet elevated.\n2. Rotate torso side to side.", icon));
        exerciseList.add(new ExerciseCatalogItem("Cable Crunch", "1. Kneel facing cable machine.\n2. Curl torso downward.\n3. Return slowly.", icon));
    }
}
