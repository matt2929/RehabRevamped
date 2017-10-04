package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.matthew.rehabrevamped.Activities.WorkoutSessionActivity;
import com.example.matthew.rehabrevamped.UserWorkoutViews.UnlockPhoneView;
import com.example.matthew.rehabrevamped.Utilities.JerkScoreAnalysis;

import java.util.ArrayList;

/**
 * Created by Matthew on 9/25/2016.
 */

public class UnlockPhone implements WorkoutSession {

    float x = 0, y = 0;
    boolean firstSpot = false;
    float startX = 0, startY = 0;
    boolean peaked = false;
    boolean returnedToStart = false;
    int count = 0;
    int countChecks = 0;
    boolean[] checkpointsCheck = new boolean[11];
    float[] checkpointsNum = new float[11];
    boolean firstTime = true;
    long startTime = System.currentTimeMillis();

    ArrayList<PointF> points = new ArrayList<PointF>();
    int height;
    boolean isDone=false;
    int time=0;
    int maxTimes=2;
    UnlockPhoneView unlockPhoneView;
    int targetValue;
    double distance=0;
    final TextToSpeech tts;
    ArrayList<Integer> runs = new ArrayList<Integer>();
    public UnlockPhone(UnlockPhoneView UPV){
        unlockPhoneView=UPV;
        targetValue=0;

        tts = new TextToSpeech(unlockPhoneView.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
    }

    /**
     * if the x is at the edge then it counts it, analyzes it and then adds a tally to time, then it
     * set the edge to the opposite side.
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
    public void dataIn(float accX, float accY, float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, int walkingCount,float magX,float magY,float magZ, long magTime, Context context) {
        Log.i("unlockTest1",x+" "+targetValue);
        Log.i("unlockTest1",""+Math.sqrt(Math.pow(x,2)+Math.pow(y,2)));
        distance=unlockPhoneView.getHorizontalMax();
        if(targetValue==0){
            targetValue=(unlockPhoneView.getFocalPoint().x)-10;
        }
        if (targetValue<10){
            if(x<=targetValue){
                time++;
                tts.speak(time + "", TextToSpeech.QUEUE_ADD, null);
                targetValue=(unlockPhoneView.getFocalPoint().x)-10;
                runs.add(analyzeRun());
                points.clear();
            }
        }
        else{
            if(x>=targetValue){
                time++;
                tts.speak(time + "", TextToSpeech.QUEUE_ADD, null);
                targetValue=-(unlockPhoneView.getFocalPoint().x)+10;
                runs.add(analyzeRun());
                points.clear();
            }
        }
        if(time==maxTimes){
            isDone=true;
        }
        /*
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
        */
    }

    @Override
    public boolean workoutFinished() {
        return isDone;
    }

    @Override
    public void holdAccuracy(float accX, float accY, float accZ) {

    }

    @Override
    public float[][] getHoldParamaters() {
        return new float[][]{{0,0,0,0,0,0},{0,0,0,0,0,0}};
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
        return null;
    }

    @Override
    public String getWorkoutName() {
        return "Unlock Phone";
    }

    @Override
    public String sayHowToHoldCup() {
        return "turn key following the line.";
    }

    /**
     * gets an average of the runs and uses it as a grade
     * @return
     */
    @Override
    public int getGrade() {
        int res=0;
        for(Integer i:runs) {
            res=res+i;
        }
        res=res/runs.size();
        Log.i("unlockTest",res+" "+runs.size());
        return res;
    }

    /**
     * compares the point in polor form to what the user should get and give the average accuracy
     * @return
     */
    public int analyzeRun(){
        ArrayList<Double> acc = new ArrayList<Double>();
        for(PointF p:points){
            double r = Math.sqrt(Math.pow(p.x,2)+Math.pow(p.y,2));
            double angle = 1/(Math.tan(p.y/p.x));
            acc.add(r-distance);
            Log.i("unlockTest",""+(r-distance));
        }
        double total=0;
        for(Double d:acc){
            total=total+d;
        }
        double res=total/(acc.size());
        return (int)res;
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

    /**
     * when ever the touch changes it relays and remembers the point.
     * @param X
     * @param Y
     */
    @Override
    public void addTouche(float X, float Y) {
        x = -(unlockPhoneView.getFocalPoint().x-X);
        y = unlockPhoneView.getFocalPoint().y-Y;
        PointF point = new PointF(x,y);
        points.add(point);
    }

    @Override
    public String csvFormat() {
        return (x+", "+y+";");
    }
}
