package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;

import com.example.matthew.rehabrevamped.UserWorkoutViews.PhoneNumberView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by research on 9/5/2017.
 */

public class PhoneNumber implements WorkoutSession{

    private String origonalPhoneNumber=null;
    private String currentPhoneNumber="";
    private PhoneNumberView workoutView;
    private double lastTime= -1;
    private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
    private ArrayList<Boolean> click = new ArrayList<Boolean>();
    private ArrayList<Integer> grades = new ArrayList<Integer>();
    private double origonalTime;
    private double finalTime;
    private int lineCounter=-1;
    private boolean isAction;
    private int timesDone=0;
    private int maxTimes = 3;
    private boolean isDone=false;
    private boolean goodClick = false;
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
            click.add(isCorrect);
            lineCounter++;
            isAction=true;
            if(isCorrect){
                currentPhoneNumber=workoutView.getPhoneNumber();
                workoutView.setResultText(currentPhoneNumber);
            }else{
                workoutView.setResultText(currentPhoneNumber);
            }
            workoutView.setLastCheck(true);
        }
        if(origonalPhoneNumber.length()>10&&currentPhoneNumber.length()==origonalPhoneNumber.length()){
            timesDone++;
            origonalPhoneNumber=null;
            currentPhoneNumber="";
            workoutView.newPhoneNumber();
            origonalPhoneNumber=workoutView.getOriginalPhoneNumber();
            grades.add(getAccuracy());
            click.clear();
        }
        if(timesDone==maxTimes){
            isDone=true;
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
        }
        else{
            origonalValueToCompare = origonalPhoneNumber.substring(sentPhoneNumber.length()-n,sentPhoneNumber.length());
            currentValueToCompare = sentPhoneNumber.substring(sentPhoneNumber.length()-n);
        }
        return currentValueToCompare.equals(origonalValueToCompare);
    }

    /**
     * when the origonalPhoneNumber and currentPhoneNumber lenght are equal the workout is completed
     * @return
     */
    @Override
    public boolean workoutFinished() {
        return isDone;
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
        return "Type in the phone number shown above as accurately and quickly as possible. You can hold the phone in your hand.";
    }

    @Override
    public int getGrade() {
        int grade=0;
        for(Integer i :grades){
            grade=grade+i;
        }

        return grade/grades.size();
    }

    /**
     * get the accuracy of the user by averaging the time the user clicked the correct button vs the time the
     * us click the incorrect button.
     * @return
     */
    private int getAccuracy() {
        int numberOfIsCorrect = 0;
        for(int i =0;i<click.size();i++){
            if (click.get(i) == true) {
                numberOfIsCorrect=numberOfIsCorrect+1;
            }
        }
        double n =(numberOfIsCorrect*100/(click.size()));
        return (int)(n);
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
        return ""+((finalTime-origonalTime)/maxTimes)/1000;
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
            result = "," + data.get(0).get(lineCounter) + "," + data.get(1).get(lineCounter) + ";";
            isAction=false;
        }
        else{
            return "no Action;";
        }
        return result;
    }

    @Override
    public ArrayList<String> saveArrayData() {
        return null;
    }

}
