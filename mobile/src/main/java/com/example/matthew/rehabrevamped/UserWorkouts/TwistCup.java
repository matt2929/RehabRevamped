package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.util.Log;

import com.example.matthew.rehabrevamped.Utilities.GripAnalysis;
import com.example.matthew.rehabrevamped.Utilities.JerkScoreAnalysis;

import java.util.ArrayList;

/**
 * Created by Matthew on 7/27/2016.
 */
public class TwistCup implements WorkoutSession {
    @Override
    public String csvFormat() {
        return null;
    }

    @Override
    public ArrayList<String> saveArrayData() {
        return null;
    }

    int getPickupCountMax = 10;
    float lastDifference = 0;
    float countAccuracyLastVal = 8;
    int pickupCount = 0;
    long startTime = System.currentTimeMillis();
    String whatToSay = "";
    boolean shouldITalk = false;
    double lastSlope = 0;
    double collisionNumber = 0;
    boolean iAnnouced = false;
    long startSpeak = System.currentTimeMillis();
    float lastGyroZ = 0;
    GripAnalysis gripAnalysis = new GripAnalysis();
    JerkScoreAnalysis jerkScoreAnalysis = new JerkScoreAnalysis(1);

    public TwistCup() {

    }


    @Override
    public float[][] getHoldParamaters() {
        return new float[][]{{0,0,0,0,0,0},{0,0,0,0,0,0}};
    }
    @Override
    public void dataIn(float accX, float accY, float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, int walkingCount,float magX,float magY,float magZ, long magTime, Context context) {
                jerkScoreAnalysis.jerkAdd(accX, accY, accZ,accTime, gyroX, gyroY, gyroZ, gyroTime, magX, magY, magZ, magTime);
     //           workoutShakeTrack.analyseData(accX, accY, accZ);
                float differenceVAL = gyroX - lastGyroZ;
                lastGyroZ = gyroX;
                Log.e("gyro: ", "" + differenceVAL);
                long nowTime = System.currentTimeMillis();
                //  holdAccuracy(accX, accY, accZ);

                if (differenceVAL < 3 && lastDifference > 3 && (Math.abs(nowTime - startTime)) > 1000) {
                    shouldITalk = true;
                    pickupCount++;
                    whatToSay = "" + pickupCount;
                    startTime = System.currentTimeMillis();
                }
                lastDifference = differenceVAL;
            }


    @Override
    public boolean workoutFinished() {
        if (pickupCount == getPickupCountMax) {
            return true;
        }
        return false;
    }

    @Override
    public String result() {
        return "\n\n\nTwisted up " + pickupCount + " time(s).";
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

        float accTotal = (float) Math.pow((Math.pow(accX, 2) + Math.pow(accY, 2) + Math.pow(accZ, 2)), .5);


        double slope = accTotal - countAccuracyLastVal;

        if (((lastSlope > 0 && slope < 0)) && countAccuracyLastVal >= 10) {
            collisionNumber++;
        }
        countAccuracyLastVal = accTotal;
        lastSlope = slope;
    }

    @Override
    public boolean shouldISaySomething() {
        return shouldITalk;
    }

    @Override
    public float getJerkScore() {
        return jerkScoreAnalysis.getJerkAverage();
    }

    @Override
    public String whatToSay() {

        shouldITalk = false;
        return whatToSay;
    }

    @Override
    public int getGrade() {
        return jerkScoreAnalysis.getJerkAverage().intValue();
    }

    @Override
    public String saveData() {
        String returnMe = "\n" +
                "---Watch Positional Average---\n" +
                "-X: " + gripAnalysis.getGripXGravMean() + "-\n" +
                "-Y: " + gripAnalysis.getGripYGravMean() + "-\n" +
                "-Z: " + gripAnalysis.getGripZGravMean() + "-\n" +
                "-------------------------------";
        return returnMe;
    }

    @Override
    public String getWorkoutName() {
        return "Twist Count";
    }


    @Override
    public String sayHowToHoldCup() {
        return "Hold the cup in your hand. Hold the cup out in front of you. Start with the cup in the upright position. Twist your wrist clockwise as far as possible than back to the upright position. Please Start when I say begin.";

    }
}
