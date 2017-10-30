package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.Utilities.JerkScoreAnalysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by Matthew on 7/12/2016.
 */
public class WalkWithCup implements WorkoutSession {
    int goodHoldMax = 0;
    int numGoodHolds = 0;
    //
    JerkScoreAnalysis jerkScoreAnalysis = new JerkScoreAnalysis(1);
    long jerkStartTime = System.currentTimeMillis();

    @Override
    public int getGrade() {
        return spillageEvents*-1;
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
    Long totalTime = 10000L;
    Float totalAccT = 0f;
    float lastX = 9, lastY = 9, lastZ = 9;
    float averageTotal = 0;
    int averageCount = 0;
    int spillageEvents = 0;
    //SFX
    MediaPlayer mediaPlayer = null;
    boolean mediaChecked = false;
    @Override
    public void dataIn(float accX, float accY, float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, int walkingCount, float magX, float magY, float magZ, long magTime, Context context) {
        float tempX = accX, tempY = accY, tempZ = accZ;
        accX = Math.abs(lastX - accX);
        accY = Math.abs(lastY - accY);
        accZ = Math.abs(lastZ - accZ);
        lastX = tempX;
        lastY = tempY;
        lastZ = tempZ;
        //This is the magnitude of acceleration among all axes
        float accT = (float) Math.pow((Math.pow(accX, 2) + Math.pow(accY, 2) + Math.pow(accZ, 2)), .5);
        averageTotal = (((averageCount) * averageTotal) + accT) / (averageCount + 1);
        averageCount++;
        //if we started we will continually check for a 'spllash' event
        if (haveIStarted) {
            isAnEvent(averageTotal,lastValue, accT, 1.5);
        }
        lastValue = accT;
        long currentTime = System.currentTimeMillis();
        // delay over begin workout
        if ((currentTime - startTime) > 6000 && !haveIStarted) {
                haveIStarted = true;
                startTime = System.currentTimeMillis();
        }
        if (!mediaChecked) {
            mediaPlayer = MediaPlayer.create(context, R.raw.pouring_water);
            mediaChecked = true;
            mediaPlayer.setLooping(true);
        }
    }
    //if current acceleration is greater than averageAcceleration * thresehold make splash sound
    public void isAnEvent(float average, float prior, float present, double thresehold) {

        if (Math.abs(prior-present)>average*thresehold) {
            Log.e("Event", "T");
            spillageEvents++;
            triggerNoise();
        } else {
            Log.e("Event", "F");
        }
    }
    //splash sound
    public void triggerNoise(){
        if (mediaPlayer.isPlaying()) {
        }else{
            mediaPlayer.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.pause();
                }
            }, 750);
        }
    }
    //ends after given time
    @Override
    public boolean workoutFinished() {
        return Math.abs(System.currentTimeMillis() - startTime) > totalTime;
    }

    @Override
    public String result() {
        return "";
    }

    @Override
    public String stringOut() {
        double timeElapsed = Math.abs(System.currentTimeMillis() - startTime);
        return "Time: " + (int) timeElapsed / 1000 + "s"
                + "\nPercentage: " + formatSignificant((timeElapsed / totalTime) * 100, 2) + "%";

    }

    public static String formatSignificant(double value, int significant) {
        MathContext mathContext = new MathContext(significant, RoundingMode.DOWN);
        BigDecimal bigDecimal = new BigDecimal(value, mathContext);
        return bigDecimal.toPlainString();
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
    public void holdAccuracy(float accX, float accY, float accZ) {

    }

    @Override
    public boolean shouldISaySomething() {
        return shouldItalk;

    }

    @Override
    public float[][] getHoldParamaters() {
        return new float[][]{{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};
    }

    @Override
    public float getJerkScore() {
        return totalAccT / total;
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
        return "Hold the bowl infront of you. Walk fowards at a comfortable pace.";
    }
}
