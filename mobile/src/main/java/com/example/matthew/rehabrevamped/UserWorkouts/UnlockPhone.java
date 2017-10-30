package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.graphics.PointF;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.matthew.rehabrevamped.UserWorkoutViews.UnlockPhoneView;

import java.util.ArrayList;
import java.util.Calendar;

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
    private String name = "";
    private  ArrayList<PointF> points = new ArrayList<PointF>();
    private int height;
    private boolean isDone=false;
    private int time=0;
    private int maxTimes=10;
    private  UnlockPhoneView unlockPhoneView;
    private int targetValue;
    private double distance=0;
    final TextToSpeech tts;
    private ArrayList<Integer> runs = new ArrayList<Integer>();
    ArrayList<String> dataArray=new ArrayList<String>();
    private float GyroX;
    private float GyroY;
    private float GyroZ;
    private int baseline;
    private int buffer = 250;

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
        GyroX=gyroX;
        GyroY=gyroY;
        GyroZ=gyroZ;
        baseline = getBaseline();

        distance=unlockPhoneView.getHorizontalMax();
        if(targetValue==0){
            targetValue = (unlockPhoneView.getFocalPoint().x) - buffer;
        }
        if (targetValue < buffer) {
            if (x <= targetValue + buffer) {
                time++;
                tts.speak(time + "", TextToSpeech.QUEUE_ADD, null);
                targetValue = (unlockPhoneView.getFocalPoint().x) - buffer;
                runs.add(analyzeRun()-baseline);
                points.clear();
            }
        }
        else{
            if (x >= targetValue - buffer) {
                time++;
                tts.speak(time + "", TextToSpeech.QUEUE_ADD, null);
                targetValue = -(unlockPhoneView.getFocalPoint().x) + buffer;
                runs.add(analyzeRun());
                points.clear();
            }
        }
        if(time==maxTimes){
            isDone=true;
        }
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
        return name;
    }

    @Override
    public String sayHowToHoldCup() {
        return "With the key in contact with the phone screen, rotate the key clockwise and counterclockwise. Try to follow the arc as accurately as possible. Rotate back and forth" + maxTimes + " times.";
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
        //get abs of pos change add togeter
        double totalDistance=0;
        for(int i =1;i<points.size();i++){

            double tempDistance = Math.pow(Math.pow(points.get(i).x-points.get(i-1).x,2)+Math.pow(points.get(i).y-points.get(i-1).y,2),.5);
            totalDistance=tempDistance+totalDistance;
            Log.i("PointsZ1",""+(points.get(i).x-points.get(i-1).x));
            Log.i("PointsZ2",""+(points.get(i).y-points.get(i-1).y));
        }
        Log.i("Points",totalDistance+"");
        return (int)totalDistance;
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
        Log.i("points",x+" "+y);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        dataArray.add( hour + ":" + minute + ":" + second + ","+x+","+y+","+GyroX+","+GyroY+","+GyroZ+";");
        Log.i("dataArray",dataArray.toString());
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String csvFormat() {
        return (x+", "+y+", "+GyroX+", "+GyroY+", "+GyroZ+";");
    }
    public int getBaseline(){
        double point1x=-1*(unlockPhoneView.getFocalPoint().x);
        double point1y= Math.pow(Math.pow(unlockPhoneView.getHorizontalMax(),2)-Math.pow(point1x,2),.5);
        double point2x=(unlockPhoneView.getFocalPoint().x);
        double point2y= Math.pow(Math.pow(unlockPhoneView.getHorizontalMax(),2)-Math.pow(point1x,2),.5);
        double angle1=(Math.atan(point1y/point1x));
        double angle2=(Math.atan(point2y/point2x));
        double angleTotal=Math.toDegrees(Math.abs(angle2-angle1));
        double arcLength=2*Math.PI*(unlockPhoneView.getHorizontalMax())*(angleTotal/360);
        return (int)arcLength;
    }
    @Override
    public ArrayList<String> saveArrayData(){
        Log.i("arrayData",dataArray.toString());
        return dataArray;
    }
}
