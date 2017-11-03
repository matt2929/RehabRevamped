package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.view.View;
import android.widget.Button;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.UserWorkoutViews.HorizontalPickUpCountView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.PhoneNumberView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.PickUpCountView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.PourCupView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.TwistCupView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.UnlockPhoneView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.WalkWithCupView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.viewabstract;
import com.example.matthew.rehabrevamped.UserWorkouts.HorizontalPickUpCount;
import com.example.matthew.rehabrevamped.UserWorkouts.PhoneNumber;
import com.example.matthew.rehabrevamped.UserWorkouts.PickUpCount;
import com.example.matthew.rehabrevamped.UserWorkouts.PourCup;
import com.example.matthew.rehabrevamped.UserWorkouts.TwistCup;
import com.example.matthew.rehabrevamped.UserWorkouts.UnlockPhone;
import com.example.matthew.rehabrevamped.UserWorkouts.WalkWithCup;

import java.util.ArrayList;

public class WorkoutSelectionScreenManual extends Activity {
    ArrayList<View> pickHandView = new ArrayList<>();
    ArrayList<View> selectWorkoutView = new ArrayList<>();
    Button pickUpButt, horPickUpButt, twistButt, phoneNumberButt, doorKnob, unlockButt, leftHand, rightHand, walkButt, pourLiquid, pickUpBowlV, pickUpBowlH;
    View fakeView;
    static com.example.matthew.rehabrevamped.UserWorkouts.WorkoutSession CurrentWorkout;
    static viewabstract CurrentWorkoutView;
    static boolean isLeftHand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pickUpBowlV = (Button) findViewById(R.id.pickupBowlV);
        pickUpBowlH = (Button) findViewById(R.id.pickupBowlHV);
        doorKnob = (Button) findViewById(R.id.doorKnob);
        pickUpButt = (Button) findViewById(R.id.pickupcupchoice);
        horPickUpButt = (Button) findViewById(R.id.horizontalpickupcupchoice);
        twistButt = (Button) findViewById(R.id.twistcupchoice);
        phoneNumberButt = (Button) findViewById(R.id.phonenumberchoice);
        leftHand = (Button) findViewById(R.id.workoutchoicelefthand);
        rightHand = (Button) findViewById(R.id.workoutchoicerighthand);
        walkButt = (Button) findViewById(R.id.walkchoice);
        unlockButt = (Button) findViewById(R.id.unlockchoice);
        fakeView = findViewById(R.id.fakeView);
        pourLiquid = (Button) findViewById(R.id.PourSelect);
        pickHandView.add(fakeView);
        pickHandView.add(leftHand);
        pickHandView.add(rightHand);
        selectWorkoutView.add(pourLiquid);
        selectWorkoutView.add(pickUpButt);
        selectWorkoutView.add(horPickUpButt);
        selectWorkoutView.add(doorKnob);
        selectWorkoutView.add(twistButt);
        selectWorkoutView.add(phoneNumberButt);
        selectWorkoutView.add(walkButt);
        selectWorkoutView.add(unlockButt);
        selectWorkoutView.add(pickUpBowlH);
        selectWorkoutView.add(pickUpBowlV);
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
        doorKnob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UnlockPhoneView unlockPhoneView = new UnlockPhoneView(getApplicationContext());
                        CurrentWorkoutView = unlockPhoneView;
                        UnlockPhone unlockPhone = new UnlockPhone((UnlockPhoneView) CurrentWorkoutView);
                        unlockPhone.setName("Door Unlock");
                        CurrentWorkout = unlockPhone;

                    }
                }).start();
                setViewHandSelection();

            }
        });
        pickUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PickUpCount pickUpCount = new PickUpCount();
                        pickUpCount.setName("Verticle Pick up Cup");
                        CurrentWorkout = pickUpCount;
                    }
                }).start();
                CurrentWorkoutView = new PickUpCountView(getApplicationContext());
            }
        });
        horPickUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HorizontalPickUpCount horizontalPickUpCount = new HorizontalPickUpCount();
                        horizontalPickUpCount.setName("Horizontal Pick up Cup");
                        CurrentWorkout = horizontalPickUpCount;
                    }
                }).start();
                CurrentWorkoutView = new HorizontalPickUpCountView(getApplicationContext());
            }
        });
        pourLiquid.setOnClickListener(new View.OnClickListener() {
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
        phoneNumberButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();
                final PhoneNumberView phoneNumberView = new PhoneNumberView(getApplicationContext());
                CurrentWorkoutView = phoneNumberView;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CurrentWorkout = new PhoneNumber(phoneNumberView);
                    }
                }).start();
            }
        });
        walkButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CurrentWorkout = new WalkWithCup();
                    }
                }).start();
                CurrentWorkoutView = new WalkWithCupView(getApplicationContext());
            }
        });
        unlockButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UnlockPhoneView unlockPhoneView = new UnlockPhoneView(getApplicationContext());
                        CurrentWorkoutView = unlockPhoneView;
                        UnlockPhone unlockPhone = new UnlockPhone((UnlockPhoneView) CurrentWorkoutView);
                        unlockPhone.setName("Key Unlock");
                        CurrentWorkout = unlockPhone;


                    }
                }).start();
                setViewHandSelection();

            }
        });
        pickUpBowlV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PickUpCountView pu = new PickUpCountView(getApplicationContext());
                        CurrentWorkoutView = pu;
                        final PickUpCount puc = new PickUpCount();
                        puc.setName("V.Bowl Pick Up");
                        CurrentWorkout = puc;
                    }
                }).start();
            }
        });
        pickUpBowlH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewHandSelection();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HorizontalPickUpCountView pu = new HorizontalPickUpCountView(getApplicationContext());
                        CurrentWorkoutView = pu;
                        final HorizontalPickUpCount puc = new HorizontalPickUpCount();
                        puc.setName("H.Bowl Pick Up");
                        CurrentWorkout = puc;
                    }
                }).start();
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
