package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthew.rehabrevamped.UserWorkoutViews.WorkoutView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.viewabstract;
import com.example.matthew.rehabrevamped.UserWorkouts.WorkoutSession;
import com.example.matthew.rehabrevamped.Utilities.CalculateAverages;
import com.example.matthew.rehabrevamped.Utilities.SampleAverage;
import com.example.matthew.rehabrevamped.Utilities.SaveData;
import com.example.matthew.rehabrevamped.Utilities.Serialize;
import com.example.matthew.rehabrevamped.Utilities.WorkoutHistoricalData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

public class WorkoutSessionActivity extends Activity implements SensorEventListener, TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    SaveData saveData;
    GoogleApiClient mGoogleApiClient;
    private Node mNode;
    private SensorManager mSensorManager;
    private float walkCount = 0;
    private float accX = 0, accY = 0, accZ = 0;
    private float gyroX = 0, gyroY = 0, gyroZ = 0;
    private float gravX = 0, gravY = 0, gravZ = 0;
    private float magX = 0, magY = 0, magZ = 0;
    private long timeAcc=0,timeGyro=0,timeMag=0;
    private Sensor mAcc, mGyro, mStep,mGrav,mMag;
    AudioManager mgr = null;
    public static float width = 0, height = 0;
    private WorkoutSession currentWorkout;
    public boolean workoutInProgress = false;
    private int sendWorkoutStringToWatchCount = 0, getSendWorkoutStringToWatchMax = 30;
    private float lastValue = 10;
    private SampleAverage sampleAverage = new SampleAverage();
    TextToSpeech tts;
    String saveString = "";
    String begin = "Ready,Begin";
    float delayToStart = 3000;
    boolean sent = false;
    viewabstract currentView;
    boolean noConformation = true;
    boolean hand;

    protected PowerManager.WakeLock mWakeLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentView = WorkoutSelectionScreen.CurrentWorkoutView;
        currentView.removeAllViews();

        setContentView(currentView);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        hand = getIntent().getBooleanExtra("hand", true);
        currentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                currentWorkout.addTouche(motionEvent.getX(), motionEvent.getY());
                return true;
            }
        });

        width = getWindow().getDecorView().getWidth();
        height = getWindow().getDecorView().getHeight();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, 10, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        tts = new TextToSpeech(getApplicationContext(), this);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if (utteranceId.equals("Begin")) {
                    workoutInProgress = true;

                }
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        currentWorkout = WorkoutSelectionScreen.CurrentWorkout;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mSensorManager.registerListener(this, mGyro, Sensor.TYPE_GYROSCOPE);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            mStep = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorManager.registerListener(this, mStep, Sensor.TYPE_STEP_COUNTER);
        }
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            mGrav = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            mSensorManager.registerListener(this, mGrav, Sensor.TYPE_GRAVITY);
        }
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            mMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, mMag, Sensor.TYPE_MAGNETIC_FIELD);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(mConnectionCallbacks)
                .build();
        mGoogleApiClient.connect();
    }

    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new
            GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NodeApi.GetConnectedNodesResult result = Wearable.NodeApi
                                    .getConnectedNodes(mGoogleApiClient).await();
                            List<Node> nodes = result.getNodes();
                            if (nodes.size() > 0) {
                                mNode = nodes.get(0);
                            }
                        }
                    }).start();
                    Wearable.MessageApi.addListener(mGoogleApiClient, mMessageListener);

                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            };

    MessageApi.MessageListener mMessageListener = new MessageApi.MessageListener() {
        @Override
        public void onMessageReceived(MessageEvent messageEvent) {

            if (messageEvent.getPath().equals("/rehabwatchtophone")) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(messageEvent.getData());
                messageEvent.getData();
                String dataRaw = new String(byteBuffer.array());
                if (dataRaw.equals("Bad Data")) {
                    Log.e("LOUD AND CLEAR", "The phone detected bad data");
                    Toast.makeText(getApplicationContext(), "Bad Hold", Toast.LENGTH_SHORT).show();
                }
                if (dataRaw.equals("Conformation")) {
                    noConformation = false;
                }
                if (dataRaw.equals("Error")) {

                    Toast.makeText(getApplicationContext(),"Bad Hold",Toast.LENGTH_SHORT).cancel();
                    Toast.makeText(getApplicationContext(),"Bad Hold",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    private void sendAMessageToWatch(final String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mNode != null) {
                    byte[] bytes = (message.getBytes());

                    Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(),
                            "/rehabphonetowatch", bytes).await();
                } else {

                }
            }
        }).start();
    }
    /**
     * Constantly checks the sensors and sends the data to the workout class that has been selected.
     As long as currentWorkout.workoutFinished() == false, it will keep doing so. If its true then
     it will verbally tell and send a toast of the users average jerk score. It will also send the
     data to a .csv file and store it in SDCard/Downloads/rehabrevamped. If there is enough data
     (currently if the current activity has been done at least twice with both hands) then it will
     also display a toast stating the difference between the score the user got and the red line
     jerk score.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (noConformation == true) {
            int left = 0;
            if (WorkoutSelectionScreen.isLeftHand) {
                sendAMessageToWatch(
                        "Start," + currentWorkout.getHoldParamaters()[left][0]
                                + "," + currentWorkout.getHoldParamaters()[left][1]
                                + "," + currentWorkout.getHoldParamaters()[left][2]
                                + "," + currentWorkout.getHoldParamaters()[left][3]
                                + "," + currentWorkout.getHoldParamaters()[left][4]
                                + "," + currentWorkout.getHoldParamaters()[left][5]
                );
            } else {
                left = 1;
                sendAMessageToWatch(
                        "Start," + currentWorkout.getHoldParamaters()[left][0]
                                + "," + currentWorkout.getHoldParamaters()[left][1]
                                + "," + currentWorkout.getHoldParamaters()[left][2]
                                + "," + currentWorkout.getHoldParamaters()[left][3]
                );
            }

        }
        Sensor sensor = event.sensor;
        width = getWindow().getDecorView().getWidth();
        height = getWindow().getDecorView().getHeight();
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accX = event.values[0];
            accY = event.values[1];
            accZ = event.values[2];
            timeAcc = event.timestamp;
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroX = event.values[0];
            gyroY = event.values[1];
            gyroZ = event.values[2];
            timeGyro = event.timestamp;
        } else if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            walkCount = event.values[0];

        }else if (sensor.getType() == Sensor.TYPE_GRAVITY){
            gravX=event.values[0];
            gravY=event.values[1];
            gravZ=event.values[2];
        }else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magX=event.values[0];
            magY=event.values[1];
            magZ=event.values[2];
            timeMag = event.timestamp;
        }
        if (workoutInProgress) {
            Calendar cal = Calendar.getInstance();
            sendWorkoutStringToWatchCount++;
            //Send Textual information to watch
            if (sendWorkoutStringToWatchCount == getSendWorkoutStringToWatchMax) {

                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                float magval = (float) Math.pow(Math.pow(accX, 2) + Math.pow(accY, 2) + Math.pow(accZ, 2), .5);
                float magDiff = Math.abs(accY - lastValue);
                lastValue = accY;

                currentWorkout.dataIn(accX, accY, accZ, timeAcc,gyroX, gyroY, gyroZ, timeGyro,(int) walkCount, magX, magY, magZ, timeMag,getApplicationContext());
                saveData = new SaveData(getApplicationContext(), currentWorkout.getWorkoutName(),".csv");
                //Should App Talk?
                if (currentWorkout.shouldISaySomething()) {
                    tts.speak(currentWorkout.whatToSay(), TextToSpeech.QUEUE_ADD, null);
                }


                sampleAverage.addSmoothAverage(magDiff);


                saveString +=
                        +hour + ":" + minute + ":" + second
                                + "," + accX + "," + accY + "," + accZ + "," + sampleAverage.getMedianAverage() + ","
                                + gyroX + "," + gyroY + "," + gyroZ + ";";
                if (currentWorkout.dataOut() != null) {
                    currentView.dataInput(currentWorkout.dataOut());
                }

                currentView.stringInput(currentWorkout.stringOut());
                currentView.invalidate();

                //sendAMessageToWatch(MessagingValues.WORKOUTDISPLAYDATA + ",\n\n\n\n" + currentWorkout.result());
                sendWorkoutStringToWatchCount = 0;
            }
            //Save Game
            if (currentWorkout.workoutFinished()) {
                tts.speak("Workout Complete.", TextToSpeech.QUEUE_ADD, null);
                tts.speak(currentWorkout.getGrade() + "%", TextToSpeech.QUEUE_ADD, null);
                Toast.makeText(getApplicationContext(), "" + currentWorkout.getGrade() + "%", Toast.LENGTH_LONG).show();

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"RehabRevamped");
                if(!file.exists()){
                    file.mkdir();
                }
                saveData.saveData(saveString,file);
                Serialize serialize = null;
                try {
                    serialize = new Serialize(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<Integer> left = new ArrayList<Integer>();
                ArrayList<Integer> right = new ArrayList<Integer>();

                ArrayList<WorkoutHistoricalData.WorkoutSession> AllWorkOuts = serialize.getUsers(getApplicationContext());
                for (WorkoutHistoricalData.WorkoutSession s : AllWorkOuts) {
                    Log.i("serialize", s.getWorkoutName()+" "+s.getJerkScore());
                    if (s.getWorkoutName().equals(currentWorkout.getWorkoutName())) {
                        if(s.isLeftHand()==hand){
                            left.add(s.getGrade());
                        }
                        else {
                            right.add(s.getGrade());
                        }
                    }
                }

                Log.i("ser1",left.toString());
                Log.i("ser2",right.toString());
                ArrayList<Integer>  lower = CalculateAverages.compareLines(left,right);
                if(right.size()>1 && left.size()>1){

                    int offValue = currentWorkout.getGrade() - CalculateAverages.calculateAverage(lower);
                    Toast.makeText(getApplicationContext(), offValue + "off from the target", Toast.LENGTH_LONG).show();
                }
                /*
                SaveData average = new SaveData(getApplicationContext(), currentWorkout.getWorkoutName()+"_"+hand,".csv");
                average.addData(currentWorkout.getGrade()+"",file);

                int difference;
                average.saveData(saveString,file);
                try {
                    Serialize serialize = new Serialize(getApplicationContext());
                    serialize.Save(getApplicationContext(), currentWorkout.getWorkoutName(), currentWorkout.getGrade(), currentWorkout.getGrade(), WorkoutSelectionScreen.isLeftHand, currentWorkout.saveData());
                    Toast.makeText(getApplicationContext(), "" + currentWorkout.getGrade() + "%", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                saveString = "";
                //   sendAMessageToWatch(MessagingValues.WORKOUTOVER);
                workoutInProgress = false;
                sendAMessageToWatch("Reset");
                serialize.Save(getApplicationContext(), currentWorkout.getWorkoutName(), currentWorkout.getGrade(), currentWorkout.getGrade(), WorkoutSelectionScreen.isLeftHand, currentWorkout.saveData());
                Intent intent = new Intent(getApplicationContext(), WorkoutOrHistory.class);
                startActivity(intent);
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGrav, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onInit(int i) {

        if (i == TextToSpeech.SUCCESS) {
            // Setting speech language
            int result = tts.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Cook simple toast message with message
                Toast.makeText(getApplicationContext(), "Language not supported",
                        Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            }
            // Enable the button - It was disabled in main.xml (Go back and
            // Check it)
            else {

            }
            // TTS is not initialized properly
        } else {
            Toast.makeText(this, "TTS Initilization Failed", Toast.LENGTH_LONG)
                    .show();
            Log.e("TTS", "Initilization Failed");
        }
        tts.speak(currentWorkout.sayHowToHoldCup(), TextToSpeech.QUEUE_ADD, null);
        HashMap<String, String> beginCheck = new HashMap<String, String>();
        beginCheck.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
        beginCheck.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "Begin");
        tts.speak(begin, TextToSpeech.QUEUE_ADD, beginCheck);

    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {

    }
    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
}
