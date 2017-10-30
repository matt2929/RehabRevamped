package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.example.matthew.rehabrevamped.Utilities.GripAnalysis;
import com.example.matthew.rehabrevamped.Utilities.JerkScoreAnalysis;
import com.example.matthew.rehabrevamped.Utilities.SampleAverage;

import java.util.ArrayList;

/**
 * Created by research on 9/4/2017.
 */

public class HorizontalPickUpCount implements WorkoutSession{
    int getPickupCountMax = 10;
    SampleAverage sampleAverage = new SampleAverage();

    float countPickupLastVal = 10;
    float countAccuracyLastVal = 8;
    int pickupCount = 0;
    Time startTime = new Time();
    String whatToSay = "";
    boolean shouldITalk = false;
    double lastSlope = 0;
    double collisionNumber = 0;
    float a = 0;
    boolean imOnLowerSurface = true;
    long StartTime = System.currentTimeMillis();
    ArrayList<String> stringsIHaveSaid = new ArrayList<>();
    double totaldatas = 0;
    boolean startedWork = false;
    GripAnalysis gripAnalysis = new GripAnalysis();
    boolean inMotion = false;
    boolean hasStarted = false;
    long startOfWorkoutForGrade = System.currentTimeMillis();
    String name = "";
    //Jerk Stuff
    JerkScoreAnalysis jerkScoreAnalysis = new JerkScoreAnalysis(2);
    long jerkStartTime = System.currentTimeMillis();
    // sound stuff
   // MediaPlayer mediaPlayer = null;
    boolean mediaChecked = false;

    public HorizontalPickUpCount() {
        startTime.setToNow();
    }

    /**
     * The workout receives accelerometer, gyroscopic, magnetic data from the WorkoutSessionActivity and
     if the average acc data is greater then .035 and inMotion==false it will time and save data as long as that
     average stays above .03. When the average is below .03 it stops recording time and data and calls
     JerkScoreAnalysis class tho calculate the jerkscore for that one action. it then increments the count and
     reset the recording variables. This method also produces water pouring sound when the average gyro(X,Y and Z) is
     not between .1 and -.1 and the Xgyro is greater is not between .5 and -.5
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
    public void dataIn(float accX, float accY, float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, int walkingCount,float magX,float magY,float magZ, long magTime, Context context) {

        if(inMotion == false){
            jerkStartTime = System.currentTimeMillis();
        }else {
            jerkScoreAnalysis.jerkAdd(accX, accY, accZ, accTime, gyroX, gyroY, gyroZ, gyroTime, magX, magY, magZ, magTime);
        }
        float differenceVAL = Math.abs(accY - countPickupLastVal);
        a = differenceVAL;
        countPickupLastVal = accY;
        sampleAverage.addSmoothAverage(differenceVAL);
        Time nowTime = new Time();
        nowTime.setToNow();
        holdAccuracy(accX, accY, accZ);
        long differenceTime = Math.abs(nowTime.toMillis(true) - startTime.toMillis(true));
        Log.i("TTTT",""+sampleAverage.getMedianAverage());
        if (sampleAverage.getMedianAverage() < .03 && differenceTime > 1000 && inMotion) {
            startTime.setToNow();
            shouldITalk = true;
            pickupCount++;
            whatToSay = "" + pickupCount;
            jerkScoreAnalysis.jerkCompute(Math.abs(System.currentTimeMillis() - jerkStartTime));
            jerkStartTime = System.currentTimeMillis();
            inMotion = false;
            imOnLowerSurface = !imOnLowerSurface;
        } else if (sampleAverage.getMedianAverage() > .35 && !inMotion) {
            inMotion = true;
        }

        if (!mediaChecked) {
      //      mediaPlayer = MediaPlayer.create(context, R.raw.pouring_water);
            mediaChecked = true;
     //       mediaPlayer.start();
     //       mediaPlayer.setLooping(true);
        }


        double vector = Math.sqrt(Math.pow(gyroX,2)+Math.pow(gyroY,2)+Math.pow(gyroZ,2));
        boolean a = (vector>.1 || vector<-.1);
        boolean b = (gyroY>.2 || gyroY<-.2);

     //   if((vector>.1 || vector<-.1) && (gyroY>.5 || gyroY<-.5)){
   //         mediaPlayer.setVolume((float)1, (float)1);
     //       if (!mediaPlayer.isPlaying()) {
            //    Log.e("media", "started");
       //         mediaPlayer.start();
         //       mediaPlayer.setLooping(true);
          //  } else {
        //        Log.e("media", "continue");
      //      }
       // }
        //else{
           // if (mediaPlayer.isPlaying()) {
             //   mediaPlayer.pause();
          //  }
       // }
    }


    @Override
    public int getGrade() {
        return jerkScoreAnalysis.getJerkAverage().intValue();
    }

    @Override
    public void holdAccuracy(float accX, float accY, float accZ) {
        float accTotal = (float) Math.pow((Math.pow(accX, 2) + Math.pow(accY, 2) + Math.pow(accZ, 2)), .5);
        double slope = accTotal - countAccuracyLastVal;
        if (((slope > 0 && lastSlope < 0) || (lastSlope > 0 && slope < 0)) && countAccuracyLastVal <= 8.6) {
            collisionNumber++;
        }
        countAccuracyLastVal = accTotal;
        lastSlope = slope;
    }


    @Override
    public boolean workoutFinished() {
        return pickupCount == getPickupCountMax;
    }

    @Override
    public String result() {

        return "\n\n\nPut away " + pickupCount + " time(s).";
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
        if (name.equals(""))
            return "Pick Up Put Down";
        return name;
    }

    @Override
    public String stringOut() {
        return "Horizontal Pick Up Count: " + pickupCount;
    }

    @Override
    public Float dataOut() {
        return null;
    }

    @Override
    public void addTouche(float x, float y) {

    }

    @Override
    public String csvFormat() {
        return null;
    }

    @Override
    public ArrayList<String> saveArrayData() {
        return null;
    }

    @Override
    public float[][] getHoldParamaters() {
        return new float[][]{{0.7f,.3f,-5f,9.5f ,10f ,4.5f},{1f,10f ,-7.5f ,1.5f}};
    }



    @Override
    public String sayHowToHoldCup() {
        if (name.equals("")) {
            return "In this workout you will move the cup from side to side. Be sure to let it sit on the table when you bring it down. When I count, pick up the cup again.";
        } else {
            return "In this workout you will move the bowl from side to side. Be sure to let it sit on the table when you bring it down. When I count, pick up the bowl again.";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
