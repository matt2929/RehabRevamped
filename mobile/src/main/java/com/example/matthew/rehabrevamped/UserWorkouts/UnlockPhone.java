package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Matthew on 9/25/2016.
 */

public class UnlockPhone implements WorkoutSession {

    float x = -1, y = -1;
    boolean firstSpot = false;
    float startX = 0, startY = 0;
    boolean peaked = false;
    boolean returnedToStart = false;
    int count=0;

    @Override
    public void dataIn(float accX, float accY, float accZ, float gravX, float gravY, float gravZ, int walkingCount, Context context) {
        if (firstSpot == false) {
            if (x != -1) {
                startX = x;
                startY = y;
                firstSpot = true;
            }
        } else {
            if (Math.abs(y - startY) > 600) {
                peaked = true;
            }
            if (peaked && (x >= startX)) {
                count++;
                firstSpot=false;
                peaked=false;
            }
        }
    }

    @Override
    public boolean workoutFinished() {
        return count==10;
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
        return null;
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
        return x+","+y+",count: "+count;
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
