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

    private String origonalPhoneNumber=null;
    private String currentPhoneNumber="";
    private PhoneNumberView workoutView;
    private double lastTime= -1;
    private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
    private double origonalTime;
    private double finalTime;
    private int lineCounter=-1;
    private boolean isAction;

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
        if(origonalPhoneNumber==null){
            origonalPhoneNumber=workoutView.getOriginalPhoneNumber();
        }
        if(lastTime<0){
            lastTime = Calendar.getInstance().getTimeInMillis();
            origonalTime=lastTime;
        }
        String sentPhoneNumber=workoutView.getPhoneNumber();
        if(!currentPhoneNumber.equals(sentPhoneNumber) &&
                currentPhoneNumber.length()<=origonalPhoneNumber.length()){


            double currentTime = Calendar.getInstance().getTimeInMillis();
            double timeDif= (currentTime-lastTime)/1000;
            finalTime = currentTime;
            boolean isCorrect = comparePhoneNumbers(sentPhoneNumber);
            data.get(0).add(timeDif);
            data.get(1).add(isCorrect);
            lineCounter++;
            isAction=true;
            if(isCorrect){
                currentPhoneNumber=workoutView.getPhoneNumber();
                workoutView.setResultText(currentPhoneNumber);
            }else{
                workoutView.setResultText(currentPhoneNumber);
            }
        }
    }

    /**
     * compare the last value in origonalPhoneNumber and currentPhoneNumber returns true if there
     * the same false otherwise
     * @return boolean
     */
    private boolean comparePhoneNumbers(String sentPhoneNumber) {
        int n=sentPhoneNumber.length()-currentPhoneNumber.length();
        String origonalValueToCompare;
        String currentValueToCompare;
        if(n>1){
            origonalValueToCompare = origonalPhoneNumber.substring(sentPhoneNumber.length()-n,sentPhoneNumber.length()+1-n);
            currentValueToCompare = sentPhoneNumber.substring(sentPhoneNumber.length()-n,sentPhoneNumber.length()+1-n);
            Log.i("PhoneTest",(sentPhoneNumber.substring(sentPhoneNumber.length()-n,sentPhoneNumber.length()+1-n))+" "+origonalPhoneNumber.substring(sentPhoneNumber.length()-n,sentPhoneNumber.length()+1-n));
        }
        else{
            origonalValueToCompare = origonalPhoneNumber.substring(sentPhoneNumber.length()-n,sentPhoneNumber.length());
            currentValueToCompare = sentPhoneNumber.substring(sentPhoneNumber.length()-n);
        }
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
        return 0;
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
//NO
    @Override
    public float[][] getHoldParamaters() {
        //TEMPERARY
        return new float[][]{{0.7f,.3f,-5f,9.5f ,10f ,4.5f},{1f,10f ,-7.5f ,1.5f}};
    }

    @Override
    public String sayHowToHoldCup() {
        return "";
        //return "Type in the phone number shown above as accurately and quickly as possible.";
    }

    @Override
    public int getGrade() {
        return getAccuracy();
    }

    /**
     * get the accuracy of the user by averaging the time the user clicked the correct button vs the time the
     * us click the incorrect button.
     * @return
     */
    private int getAccuracy() {
        int numberOfIsCorrect = 0;
        for(int i =0;i<data.get(1).size();i++){
            Log.i("phoneTest",""+data.get(1).get(i));
            if((Boolean) data.get(1).get(i)==true){
                numberOfIsCorrect=numberOfIsCorrect+1;
            }
        }
        return (numberOfIsCorrect/(data.get(1).size()))*100;
    }

    @Override
    public String result() {
        return "\n\n\npressed buttons " + data.get(0).size() + " time(s).";
    }

    @Override
    public String stringOut() {
        return getDuration();
    }

    private String getDuration() {
        return ""+(finalTime-origonalTime)/1000;
    }

    @Override
    public Float dataOut() {
        return null;
    }

    @Override
    public void addTouche(float x, float y) {

    }

    /**
     * sets a new format to store the data in the csv file and is used
     * by the WorkoutSessionActivity to be save the data in the csv file.
     * @return
     */
    @Override
    public String csvFormat() {
        String result="";
        if(isAction){
            result="timeDiff:,"+data.get(0).get(lineCounter)+", isCorrect:,"+data.get(1).get(lineCounter)+";";
            isAction=false;
        }
        else{
            return "no Action;";
        }
        return result;
    }

}
