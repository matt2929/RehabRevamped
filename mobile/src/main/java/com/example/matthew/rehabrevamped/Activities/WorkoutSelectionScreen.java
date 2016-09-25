package com.example.matthew.rehabrevamped.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.UserWorkoutViews.PickUpCountView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.PourCupView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.TwistCupView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.WorkoutView;
import com.example.matthew.rehabrevamped.UserWorkouts.*;

import java.util.ArrayList;

public class WorkoutSelectionScreen extends AppCompatActivity {
    ArrayList<View> pickHandView = new ArrayList<>();
    ArrayList<View> selectWorkoutView = new ArrayList<>();
    Button pickUpButt, twistButt, pourButt, unlockButt, leftHand, rightHand;
    View fakeView;
    static com.example.matthew.rehabrevamped.UserWorkouts.WorkoutSession CurrentWorkout;
    static WorkoutView CurrentWorkoutView;
    static boolean isLeftHand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection_screen);

        pickUpButt = (Button) findViewById(R.id.pickupcupchoice);
        twistButt = (Button) findViewById(R.id.twistcupchoice);
        pourButt = (Button) findViewById(R.id.pourwaterchoice);
        unlockButt = (Button) findViewById(R.id.unlockchoice);
        pickUpButt = (Button) findViewById(R.id.pickupcupchoice);
        leftHand = (Button) findViewById(R.id.workoutchoicelefthand);
        rightHand = (Button) findViewById(R.id.workoutchoicerighthand);
        fakeView = (View) findViewById(R.id.fakeView);

        //

        pickHandView.add(fakeView);
        pickHandView.add(leftHand);
        pickHandView.add(rightHand);

        selectWorkoutView.add(pickUpButt);
        selectWorkoutView.add(twistButt);
        selectWorkoutView.add(pourButt);
        selectWorkoutView.add(pickUpButt);
        selectWorkoutView.add(unlockButt);


        leftHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLeftHand=true;
                Intent intent = new Intent(getApplicationContext(), WorkoutSessionActivity.class);
                startActivity(intent);

            }
        });
        rightHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLeftHand=false;
                Intent intent = new Intent(getApplicationContext(), WorkoutSessionActivity.class);
                startActivity(intent);

            }
        });
        pickUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                CurrentWorkout = new PickUpCount();
                CurrentWorkoutView = new PickUpCountView(getApplicationContext());
            }
        });
        twistButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                CurrentWorkout = new TwistCup();
                CurrentWorkoutView = new TwistCupView(getApplicationContext());
            }
        });
        pourButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                CurrentWorkout = new PourCup();
                CurrentWorkoutView = new PourCupView(getApplicationContext());
            }
        });
        unlockButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
            }
        });
        setWorkoutPickView();
    }

    public void setWorkoutPickView() {
        for (View v : pickHandView) {
            v.setVisibility(View.GONE);
        }
        for (View v : selectWorkoutView) {
            v.setVisibility(View.VISIBLE);
        }

    }

    public void setViewHandSelection() {
        for (View v : pickHandView) {
            v.setVisibility(View.VISIBLE);
        }
        for (View v : selectWorkoutView) {
            v.setVisibility(View.GONE);
        }
    }
}