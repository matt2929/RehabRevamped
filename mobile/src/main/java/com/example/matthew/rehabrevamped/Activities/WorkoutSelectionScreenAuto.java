package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.Utilities.WorkoutParameters;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkoutSelectionScreenAuto extends Activity {
    ArrayList<WorkoutParameters> workoutParameters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection_screen_auto);
        Bundle bundle = getIntent().getExtras();
        String previousWorkout = "n/a";
        if (bundle != null) {
            workoutParameters = (ArrayList<WorkoutParameters>) bundle.getSerializable("wokoutParameters");
            previousWorkout = bundle.getString("previous"); 
            Toast.makeText(getApplicationContext(), "Data Loaded", Toast.LENGTH_SHORT).show();
        }
        if (workoutParameters != null) {

        }
        boolean found = false,set=false;
        ArrayList<String> paramStrings = new ArrayList<>();
        WorkoutParameters currentWorkout = workoutParameters.get(0);
        for (int i = 0; i < workoutParameters.size(); i++) {
            WorkoutParameters wp = workoutParameters.get(i);
            if (found&&!set) {
                currentWorkout = wp;
                set=true;
            }
            if (wp.getName().equals(previousWorkout)) {
                found = true;
            }

            String s = "Workout Name:      " + wp.getName();
            s += "\nRepititions Left:      " + wp.getRepetitions();
            s += "\nObject To Be Used: " + wp.getObject();
            paramStrings.add(s);
        }

        ListView listView = (ListView) findViewById(R.id.AutoWorkoutList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                paramStrings);
        listView.setAdapter(arrayAdapter);
        TextView textView = (TextView) findViewById(R.id.next_workout_display);
        textView.setText(Html.fromHtml("Next Workout:" + "<font color='red'> " + currentWorkout.getName() + "</font>"), TextView.BufferType.SPANNABLE);
        final WorkoutParameters finalCurrentWorkout = currentWorkout;
        ImageButton button = (ImageButton) findViewById(R.id.autoStart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WorkoutSessionActivity.class);
                intent.putExtra("wokoutParameters", workoutParameters);
                intent.putExtra("Auto", finalCurrentWorkout.getName());
                startActivity(intent);

            }
        });
    }
}
