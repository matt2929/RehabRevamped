package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;

/**
 * Created by Matthew on 7/9/2016.
 */
public interface WorkoutSession {


    public void dataIn(float accX, float accY, float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, int walkingCount, float magX,float magY,float magZ, long magTime, Context context);

    public boolean workoutFinished();

    public void holdAccuracy(float accX, float accY, float accZ);

    public boolean shouldISaySomething();

   public float getJerkScore();

    public String whatToSay();

    public String saveData();

    public String getWorkoutName();

    public float[][] getHoldParamaters();

    public String sayHowToHoldCup();

    public int getGrade();

    public String result();

    public String stringOut();

    public Float dataOut();

    public void addTouche(float x, float y);

    /**
     * allows WorkoutClasses to modify the .csv file organization
     * @return
     */
    public String csvFormat();

}
