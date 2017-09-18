package com.example.matthew.rehabrevamped.UserWorkouts;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.Utilities.JerkScoreAnalysis;

/**
 * Created by Matthew on 8/10/2016.
 */
public class PourCup implements WorkoutSession {
    int sizeOfQuarters = 40;
    int[] pitcher = {sizeOfQuarters, sizeOfQuarters, sizeOfQuarters, sizeOfQuarters};
    float deadzone = (float) 9.2, firstMark = (float) 7.275, secondMark = (float) 4.85, thirdMark = (float) 2.425;
    long StartTime = System.currentTimeMillis();
    MediaPlayer mediaPlayer = null;
    boolean mediaChecked = false;
    boolean startedWork = false;
    boolean outOfSpace = false;
    float GravX=-1;
    float GravY=-1;
    double AccX = 0, AccY = 0;
    boolean shouldISpeak = false;
    String whatToSay = "";
    JerkScoreAnalysis jerkScoreAnalysis= new JerkScoreAnalysis(5);
    long startTime = System.currentTimeMillis();
    @Override
    public int getGrade() {
        long time=Math.abs(startTime- System.currentTimeMillis());
        jerkScoreAnalysis.jerkCompute(time/10);
        return jerkScoreAnalysis.getJerkAverage().intValue();
    }

    @Override
    public void dataIn(float accX, float accY, float accZ, long accTime, float gyroX, float gyroY, float gyroZ, long gyroTime, int walkingCount,float magX,float magY,float magZ, long magTime, Context context) {
                jerkScoreAnalysis.jerkAdd(accX, accY, accZ,accTime, gyroX, gyroY, gyroZ, gyroTime, magX, magY, magZ, magTime);
                GravX=accX;
                GravY=accY;
                AccX = accX;
                AccY = accY;
                //has mediaplayer been instantiated
                if (!mediaChecked) {
                    mediaPlayer = MediaPlayer.create(context, R.raw.pouring_water);
                    mediaChecked = true;
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
                int positionCup;
                //determine how far cup is tipped  based on Y Grav  []-> to []V
                if (accY > deadzone) {
                    positionCup = -1;
                } else if (accY > firstMark) {
                    positionCup = 1;

                } else if (accY > secondMark) {
                    positionCup = 2;
                } else if (accY > thirdMark) {
                    positionCup = 3;
                } else if (accY <= thirdMark) {
                    positionCup = 4;
                } else {
                    positionCup = -1;
                }

                if (positionCup == -1) {
                    if (mediaPlayer.isPlaying()) {

                        mediaPlayer.pause();
                    }
                } else {

                    for (int cupSearch = 0; cupSearch < positionCup; cupSearch++) {
                        if (pitcher[cupSearch] != 0) {
                            pitcher[cupSearch] = pitcher[cupSearch] - 1;
                            int totalLiquidCanBePoured = 0;
                            int totalLiquidArea = 0;
                            for (int sum = 0; sum < positionCup; sum++) {
                                totalLiquidCanBePoured += pitcher[sum];
                                totalLiquidArea += sizeOfQuarters;
                            }
                            if (totalLiquidCanBePoured >= sizeOfQuarters * 2) {
                                if (!outOfSpace) {
                                    outOfSpace = true;
                                    outOfSpace = false;
                                    pitcher[cupSearch] = pitcher[cupSearch] + 1;
                                    whatToSay = "Pouring too quick! Beer is everywhere!";
                                    shouldISpeak = true;
                                    mediaPlayer.pause();
                                    outOfSpace = true;

                                } else {

                                    pitcher[cupSearch] = pitcher[cupSearch] + 1;
                                }
                            } else {
                                outOfSpace = false;
                                float poportionWater = (float) totalLiquidCanBePoured / (float) totalLiquidArea;
                                Log.e("Volume", "[" + poportionWater + "]");
                                mediaPlayer.setVolume(poportionWater, poportionWater);
                                if (!mediaPlayer.isPlaying()) {
                                    Log.e("media", "started");
                                    mediaPlayer.start();
                                    mediaPlayer.setLooping(true);
                                } else {
                                    Log.e("media", "continue");

                                }
                            }
                            break;

                        } else {

                            mediaPlayer.pause();
                        }
                    }
                }

            }

    @Override
    public boolean workoutFinished() {

        for (int i = 0; i < pitcher.length; i++) {
            if (pitcher[i] != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String result() {
        return (((((double) (pitcher[0] + pitcher[1] + pitcher[2] + pitcher[3]) / (sizeOfQuarters * 4)) * 100.0) + "%"));
    }

    @Override
    public String stringOut() {
        return GravX+","+GravY;
    }

    @Override
    public Float dataOut() {
        return ((float) (pitcher[0] + pitcher[1] + pitcher[2] + pitcher[3]) / (sizeOfQuarters * 4));
    }

    @Override
    public void addTouche(float x, float y) {

    }

    @Override
    public String csvFormat() {
        return null;
    }


    @Override
    public void holdAccuracy(float accX, float accY, float accZ) {

    }

    @Override
    public boolean shouldISaySomething() {
        return shouldISpeak;
    }

    @Override
    public float getJerkScore() {
        return 0f;
    }

    @Override
    public String whatToSay() {
        shouldISpeak = false;
        return whatToSay;
    }

    @Override
    public String saveData() {
        return null;
    }

    @Override
    public float[][] getHoldParamaters() {
        return new float[][]{{0,0,0,0,0,0},{0,0,0,0,0,0}};
    }

    @Override
    public String getWorkoutName() {
        return "Pour Water";
    }



    @Override
    public String sayHowToHoldCup() {
        long StartTime = System.currentTimeMillis();
        return "In this workout you will hold the cup up and pretend to pour out water in front of you. Do not pour too quick or to slow.";
    }
}
