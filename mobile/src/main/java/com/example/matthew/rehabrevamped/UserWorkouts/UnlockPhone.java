package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.example.matthew.rehabrevamped.Activities.WorkoutSessionActivity;

/**
 * Created by Matthew on 9/25/2016.
 */

public class UnlockPhone implements WorkoutSession {

    float x = -1, y = -1;
    boolean firstSpot = false;
    float startX = 0, startY = 0;
    boolean peaked = false;
    boolean returnedToStart = false;
    int count = 0;
    int countChecks = 0;
    boolean[] checkpointsCheck = new boolean[11];
    float[] checkpointsNum = new float[11];
    boolean firstTime = true;


    @Override
    public void dataIn(float accX, float accY, float accZ, float gravX, float gravY, float gravZ, int walkingCount, Context context) {
        if (firstTime) {
            float width = WorkoutSessionActivity.width;
            for (int i = 0; i < checkpointsNum.length - 1; i++) {
                checkpointsCheck[i] = false;
                checkpointsNum[i] = (width * (i / 10f));
                Log.e("checkpoint", "" + checkpointsNum[i]);

            }
            if (WorkoutSessionActivity.width != 0) {
                firstTime = false;
            }
        } else {

            for (int i = 0; i < checkpointsNum.length - 1; i++) {
                if (x < checkpointsNum[i + 1] && x > checkpointsNum[i]) {
                     if (checkpointsCheck[i] == false) {
                         Log.e("it happened","i:"+i+": "+checkpointsCheck[i]);
                         checkpointsCheck[i] = true;

                         countChecks++;
                    }
                   }
            }
            if (countChecks == 8) {
                count++;
                countChecks = 0;
                for (int i = 0; i < checkpointsNum.length; i++) {
                    checkpointsCheck[i] = false;
                }
            }
              Log.e("count: ", ""+countChecks+" x:"+x+" y:"+y);
        }
    }

    @Override
    public boolean workoutFinished() {
        return count == 5;
    }

    @Override
    public void holdAccuracy(float accX, float accY, float accZ) {

    }

    @Override
    public boolean shouldISaySomething() {
        return false;
    }

    @Override
    public float getJerkScore() {
        return 0f;
    }

    @Override
    public String whatToSay() {
        return null;
    }

    @Override
    public String saveData() {
        return null;
    }

    @Override
    public String getWorkoutName() {
        return "Unlock Phone";
    }

    @Override
    public String sayHowToHoldCup() {
        return "Drag the dot from one side to the other.";
    }

    @Override
    public int getGrade() {
        return 0;
    }

    @Override
    public String result() {
        return null;
    }

    @Override
    public String stringOut() {
        return x + "," + y + ",count: " + count;
    }

    @Override
    public Float dataOut() {
        return null;
    }

    @Override
    public void addTouche(float X, float Y) {
        x = X;
        y = Y;
    }
}
