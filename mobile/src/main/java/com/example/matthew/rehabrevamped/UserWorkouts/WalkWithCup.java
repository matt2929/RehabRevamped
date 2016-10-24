package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;

/**
 * Created by Matthew on 7/12/2016.
 */
public class WalkWithCup implements WorkoutSession {
    int goodHoldMax = 0;
    int numGoodHolds = 0;

    @Override
    public int getGrade() {
        return 0;
    }

    int startCount;
    boolean notStarted = true;
    int totalSteps = 0;
    float lastSlope = 0;
    float lastValue = 0;
    boolean shouldItalk = false;
    long startTime = System.currentTimeMillis();
    boolean haveIStarted = false;
    int total = 0;
    String whatShouldISay = "";

    @Override
    public void dataIn(float accX, float accY, float accZ, float gravX, float gravY, float gravZ, int walkingCount, Context context) {
        float accT = (float) Math.pow((Math.pow(accX, 2) + Math.pow(accY, 2) + Math.pow(accZ, 2)), .5);
        float slope = (accT - lastValue);
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) > 8000) {
            if (!haveIStarted) {
                haveIStarted = true;
                shouldItalk = true;
                whatShouldISay = "Start Walking";
            }
            if (lastSlope > 0 && slope < 0 && lastValue >= 10 && lastValue > 10) {
                totalSteps++;
            }
            lastSlope = slope;
            lastValue = accT;
        } else {

        }
    }

    @Override
    public boolean workoutFinished() {
        if (totalSteps == 100) {
            return true;
        }
        return false;
    }

    @Override
    public String result() {
        return "Steps: " + totalSteps;
    }

    @Override
    public String stringOut() {
        return null;
    }

    @Override
    public Float dataOut() {
        return null;
    }

    @Override
    public void addTouche(float x, float y) {

    }


    @Override
    public void holdAccuracy(float accX, float accY, float accZ) {

    }

    @Override
    public boolean shouldISaySomething() {
        return shouldItalk;

    }

    @Override
    public float getJerkScore() {
        return 0f;
    }

    @Override
    public String whatToSay() {
        shouldItalk = false;
        return whatShouldISay;

    }

    @Override
    public String saveData() {
        return null;
    }

    @Override
    public String getWorkoutName() {
        return "Walk With Cup";
    }


    @Override
    public String sayHowToHoldCup() {
        return null;
    }
}
