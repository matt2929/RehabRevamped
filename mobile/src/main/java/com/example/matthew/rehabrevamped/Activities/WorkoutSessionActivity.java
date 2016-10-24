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
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthew.rehabrevamped.UserWorkoutViews.WorkoutView;
import com.example.matthew.rehabrevamped.UserWorkoutViews.viewabstract;
import com.example.matthew.rehabrevamped.UserWorkouts.WorkoutSession;
import com.example.matthew.rehabrevamped.Utilities.SampleAverage;
import com.example.matthew.rehabrevamped.Utilities.SaveData;
import com.example.matthew.rehabrevamped.Utilities.Serialize;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.nio.ByteBuffer;
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
    private Sensor mAcc, mGyro, mStep;
    AudioManager mgr = null;
    public static float width=0, height=0;
    private WorkoutSession currentWorkout;
    public boolean workoutInProgress = false;
    private int sendWorkoutStringToWatchCount = 0, getSendWorkoutStringToWatchMax = 30;
    private float lastValue = 10;
    private SampleAverage sampleAverage = new SampleAverage();
    TextToSpeech tts;
    String saveString = "";
    String begin="Ready,Begin";
    float delayToStart= 3000;
    viewabstract currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentView = WorkoutSelectionScreen.CurrentWorkoutView;
        currentView.removeAllViews();
        setContentView(currentView);
        currentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                currentWorkout.addTouche(motionEvent.getX(), motionEvent.getY());
                return true;
            }
        });
        width=getWindow().getDecorView().getWidth();
        height=getWindow().getDecorView().getHeight();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, 10, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        tts = new TextToSpeech(this, this);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if(utteranceId.equals("Begin")){
                    workoutInProgress=true;
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
        } else {
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mSensorManager.registerListener(this, mGyro, Sensor.TYPE_GYROSCOPE);
        } else {

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            mStep = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorManager.registerListener(this, mStep, Sensor.TYPE_STEP_COUNTER);
        } else {

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
                /*if (dataRaw.equals("")) {

                    }*/
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        width=getWindow().getDecorView().getWidth();
        height=getWindow().getDecorView().getHeight();
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accX = event.values[0];
            accY = event.values[1];
            accZ = event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroX = event.values[0];
            gyroY = event.values[1];
            gyroZ = event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            walkCount = event.values[0];

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


                currentWorkout.dataIn(accX, accY, accZ, gyroX, gyroY, gyroZ, (int) walkCount, getApplicationContext());
                saveData = new SaveData(getApplicationContext(), currentWorkout.getWorkoutName());
                //Should App Talk?
                if (currentWorkout.shouldISaySomething()) {
                    tts.speak(currentWorkout.whatToSay(), TextToSpeech.QUEUE_ADD, null);
                }


                sampleAverage.addSmoothAverage(magDiff);

                saveString +=
                        +hour + ":" + minute + ":" + second
                                + "," + accX + "," + accY + "," + accZ + "," + sampleAverage.getMedianAverage() + ","
                                + gyroX + "," + gyroY + "," + gyroZ + ";";

                //
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

                saveData.saveData(saveString);
                try {
                    Serialize serialize = new Serialize(getApplicationContext());
                    serialize.Save(getApplicationContext(), currentWorkout.getWorkoutName(), currentWorkout.getGrade(), currentWorkout.getGrade(), WorkoutSelectionScreen.isLeftHand, currentWorkout.saveData());
                    Toast.makeText(getApplicationContext(), "" + currentWorkout.getGrade() + "%", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveString = "";
                //   sendAMessageToWatch(MessagingValues.WORKOUTOVER);
                workoutInProgress = false;
                Intent intent = new Intent(getApplicationContext(), WorkoutSelectionScreen.class);
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
        tts.speak(begin,TextToSpeech.QUEUE_ADD,beginCheck);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {

    }
}
