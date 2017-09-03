package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.UserWorkoutViews.PickUpCountView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.PourCupView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.TwistCupView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.UnlockPhoneView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.WorkoutView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.viewabstract;
import com.example.matthew.rehabrevamped.UserWorkouts.*;

import java.util.ArrayList;

public class WorkoutSelectionScreen extends
        Activity {
    ArrayList<View> pickHandView = new ArrayList<>();
    ArrayList<View> selectWorkoutView = new ArrayList<>();
    Button pickUpButt, twistButt, pourButt, unlockButt, leftHand, rightHand;
    View fakeView;
    static com.example.matthew.rehabrevamped.UserWorkouts.WorkoutSession CurrentWorkout;
    static viewabstract CurrentWorkoutView;
    static boolean isLeftHand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                CurrentWorkoutView.removeAllViews();
                isLeftHand=true;
                Intent intent = new Intent(getApplicationContext(), WorkoutSessionActivity.class).putExtra("hand", true);
                startActivity(intent);
            }
        });
        rightHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentWorkoutView.removeAllViews();
                isLeftHand=false;

                Intent intent = new Intent(getApplicationContext(), WorkoutSessionActivity.class).putExtra("hand", false);
                startActivity(intent);

            }
        });
        pickUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CurrentWorkout = new PickUpCount();
                    }
                }).start();
                CurrentWorkoutView = new PickUpCountView(getApplicationContext());
            }
        });
        twistButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CurrentWorkout = new TwistCup();
                    }
                }).start();
                CurrentWorkoutView = new TwistCupView(getApplicationContext());
            }
        });
        pourButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CurrentWorkout = new PourCup();
                    }
                }).start();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                CurrentWorkout = new UnlockPhone();
            }
        }).start();
        CurrentWorkoutView = new UnlockPhoneView(getApplicationContext());
    }

    public void setWorkoutPickView() {
        for (View v : pickHandView) {
            v.setVisibility(View.GONE);
        }
        for (View v : selectWorkoutView) {
            v.setVisibility(View.VISIBLE);
        }

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
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
