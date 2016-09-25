package com.example.matthew.rehabrevamped.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.matthew.rehabrevamped.R;

public class WorkoutOrHistory extends AppCompatActivity {
    Button buttonWorkout, buttonHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_or_history);
        buttonWorkout = (Button) findViewById(R.id.chooseworkout);
        buttonHistory = (Button) findViewById(R.id.choosehistory);
        buttonWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WorkoutSelectionScreen.class);
                startActivity(intent);

            }
        });
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryList.class);
                startActivity(intent);

            }
        });
    }
}
