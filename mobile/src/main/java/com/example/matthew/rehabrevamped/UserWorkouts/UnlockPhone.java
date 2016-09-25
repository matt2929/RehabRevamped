package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;

/**
 * Created by Matthew on 9/25/2016.
 */

public class UnlockPhone implements WorkoutSession {

    float x = -1, y = -1;
    boolean firstSpot = false;
    float startX = 0, startY = 0;
    boolean peaked = false;
    boolean returnedToStart = false;

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
                returnedToStart = true;
            }
        }
    }

    @Override
    public boolean workoutFinished() {
        return returnedToStart;
    }

    @Override
    public void holdAccuracy(float accX, float accY, float accZ) {

    }

    @Override
    public boolean shouldISaySomething() {
        return false;
    }

    @Override
    public int[] ShakeNum() {
        return new int[0];
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
        return null;
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
        return x+","+y;
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
