package com.example.matthew.rehabrevamped;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;
import java.util.List;

public class ConnectToPhone extends Activity implements SensorEventListener {
    GoogleApiClient mGoogleApiClient;
    private Node mNode;
    private TextView xText, yText, zText;
    private float xMin, yMin, zMin, xMax, yMax, zMax;
    private SensorManager mSensorManager;
    private Sensor mGrav;
    private boolean phoneWantsMyData;
    private float gravX = 0, gravY = 0, gravZ = 0;
    private float thresehold = 2.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_phone);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                xText = (TextView) stub.findViewById(R.id.xText);
                yText = (TextView) stub.findViewById(R.id.yText);
                zText = (TextView) stub.findViewById(R.id.zText);
                xText.setText("");
                yText.setText("not connected");
                zText.setText("");
            }
        });
        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            mGrav = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            mSensorManager.registerListener(this, mGrav, Sensor.TYPE_GRAVITY);
        } else {
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(failedListener)
                .build();
        mGoogleApiClient.connect();
    }

    GoogleApiClient.OnConnectionFailedListener failedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
        }
    };
    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new
            GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    yText.setText("Connected and Waiting Workout Selection");
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
            ByteBuffer byteBuffer = ByteBuffer.wrap(messageEvent.getData());
            messageEvent.getData();
            String dataRaw = new String(byteBuffer.array());

            Log.e("val", dataRaw);

            if (messageEvent.getPath().equals("/rehabphonetowatch")) {
                if (dataRaw.split("\\,")[0].equals("Start")) {
                    sendPhoneConformation();
                    phoneWantsMyData = true;
                    xMin = Float.parseFloat(dataRaw.split("\\,")[1]);
                    yMin = Float.parseFloat(dataRaw.split("\\,")[2]);
                    zMin = Float.parseFloat(dataRaw.split("\\,")[3]);
                    xMax = Float.parseFloat(dataRaw.split("\\,")[4]);
                    yMax = Float.parseFloat(dataRaw.split("\\,")[5]);
                    zMax = Float.parseFloat(dataRaw.split("\\,")[6]);
                    activityStart();
                } else if (dataRaw.equals("Reset")) {
                    xText.setText("");
                    yText.setText("Connected and Waiting Workout Selection");
                    zText.setText("");
                    activityReset();
                    phoneWantsMyData = false;
                }
            }
        }
    };


    private void sendPhoneData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mNode != null) {
                    byte[] bytes = ("BadData".getBytes());

                    Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(),
                            "/rehabwatchtophone", bytes).await();
                } else {

                }
            }
        }).start();
    }

    private void sendPhoneConformation() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mNode != null) {
                    byte[] bytes = ("Conformation".getBytes());

                    Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(),
                            "/rehabwatchtophone", bytes).await();
                } else {

                }
            }
        }).start();
    }


    private void sendPhoneError() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mNode != null) {
                    byte[] bytes = ("Error".getBytes());

                    Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(),
                            "/rehabwatchtophone", bytes).await();
                } else {

                }
            }
        }).start();
    }


    public void activityStart() {

    }

    public void activityReset() {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (phoneWantsMyData) {
            Sensor sensor = event.sensor;
            if (sensor.getType() == Sensor.TYPE_GRAVITY) {
                gravX = event.values[0];
                gravY = event.values[1];
                gravZ = event.values[2];
                xText.setText("X: " + gravX);
                yText.setText("Y: " + gravY);
                zText.setText("Z: " + gravZ);
                xText.setTextColor(Color.GREEN);
                yText.setTextColor(Color.GREEN);
                zText.setTextColor(Color.GREEN);

                if (gravX>xMax||gravX<xMin) {
                    xText.setTextColor(Color.RED);
                sendPhoneError();
                }

                if (gravY>yMax||gravY<yMin) {
                    yText.setTextColor(Color.RED);
                    sendPhoneError();

                }
                if (gravZ>zMax||gravZ<zMin) {
                    zText.setTextColor(Color.RED);
                    sendPhoneError();

                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
