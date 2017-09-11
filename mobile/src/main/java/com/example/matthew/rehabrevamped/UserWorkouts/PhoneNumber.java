package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.util.Log;

import com.example.matthew.rehabrevamped.UserWorkoutViews.PhoneNumberView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.WorkoutView;
import com.example.matthew.rehabrevamped.Utilities.JerkScoreAnalysis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by research on 9/5/2017.
 */

public class PhoneNumber implements WorkoutSession{

    private JerkScoreAnalysis jerkScoreAnalysis= new JerkScoreAnalysis(0);
    private String origonalPhoneNumber=null;
    private String currentPhoneNumber="";
    private PhoneNumberView workoutView;
    private double lastTime= -1;
    private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();

    /**
     * assigns the view and add Arraylists
     * @param WV
     */
    public PhoneNumber(PhoneNumberView WV){
        workoutView=WV;
        ArrayList<Object> time = new ArrayList<Object>();
        data.add(time);
        ArrayList<Object> success = new ArrayList<Object>();
        data.add(success);
    }

    /**
     * if the currentPhoneNumber from the view changes it changes the currentPhoneNumber in the class
     * to equal to the one from the view, it then compares the last number to origonalPhoneNumber.
     * if false then it delete the last number from the string and then sends it to the view to be displayed.
     * if true then it sends the string to the view to be displayed without changes. It will also add the
     * time between the clicks and the isCorrect value to the data ArrayList.
     * @param accX
     * @param accY
     * @param accZ
     * @param accTime
     * @param gyroX
     * @param gyroY
     * @param gyroZ
     * @param gyroTime
     * @param walkingCount
     * @param magX
     * @param magY
     * @param magZ
     * @param magTime
     * @param context
     */
    @Override
    public void dataIn(float accX, float accY, float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, int walkingCount, float magX, float magY, float magZ, long magTime, Context context) {
        jerkScoreAnalysis.jerkAdd(accX,accY,accZ,accTime,gyroX,gyroY,gyroZ,gyroTime,magX,magY,magZ,magTime);
        if(origonalPhoneNumber==null){
            origonalPhoneNumber=workoutView.getOriginalPhoneNumber();
        }
        if(lastTime<0){
            lastTime = Calendar.getInstance().getTimeInMillis();
        }
        if(!currentPhoneNumber.equals(workoutView.getPhoneNumber()) &&
                currentPhoneNumber.length()<=origonalPhoneNumber.length()){
            currentPhoneNumber=workoutView.getPhoneNumber();
            double timeDif= lastTime-Calendar.getInstance().getTimeInMillis();
            boolean isCorrect = comparePhoneNumbers();
            data.get(0).add(timeDif);
            data.get(1).add(isCorrect);
            if(isCorrect){
                workoutView.setResultText(currentPhoneNumber);
            }else{
                if(currentPhoneNumber.length()==1) {
                    currentPhoneNumber="";
                }else{
                    currentPhoneNumber=currentPhoneNumber.substring(0,currentPhoneNumber.length()-1);
                }
                workoutView.setResultText(currentPhoneNumber);
            }
        }
    }

    /**
     * compare the last value in origonalPhoneNumber and currentPhoneNumber returns true if there
     * the same false otherwise
     * @return
     */
    private boolean comparePhoneNumbers() {
        String origonalValueToCompare = origonalPhoneNumber.substring(currentPhoneNumber.length()-1,currentPhoneNumber.length());
        String currentValueToCompare = currentPhoneNumber.substring(currentPhoneNumber.length()-1);
        if(currentValueToCompare.equals(origonalValueToCompare)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * when the origonalPhoneNumber and currentPhoneNumber lenght are equal the workout is completed
     * @return
     */
    @Override
    public boolean workoutFinished() {
        if(currentPhoneNumber==null ||origonalPhoneNumber==null){
            return false;
        }
        else if(currentPhoneNumber.length()==origonalPhoneNumber.length()){
            return true;
        }
        else {
            return false;
        }
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
        return jerkScoreAnalysis.getJerkAverage();
    }

    @Override
    public String whatToSay() {
        return null;
    }

    @Override
    public String saveData() {
        String returnMe = "\n" +data.toString();
        return returnMe;
    }

    @Override
    public String getWorkoutName() {
        return "Phone Number";
    }

    @Override
    public float[][] getHoldParamaters() {
        //TEMPERARY
        return new float[][]{{0.7f,.3f,-5f,9.5f ,10f ,4.5f},{1f,10f ,-7.5f ,1.5f}};
    }

    @Override
    public String sayHowToHoldCup() {
        return "Type in the phone number shown above as accurately and quickly as possible.";
    }

    @Override
    public int getGrade() {
        return jerkScoreAnalysis.getJerkAverage().intValue();
    }

    @Override
    public String result() {
        return "\n\n\npressed buttons " + data.get(0).size() + " time(s).";
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
}
