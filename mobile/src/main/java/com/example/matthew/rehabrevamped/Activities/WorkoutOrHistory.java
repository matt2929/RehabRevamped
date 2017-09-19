package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.Utilities.DecodeInputFile;
import com.example.matthew.rehabrevamped.Utilities.WorkoutParameters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class WorkoutOrHistory extends Activity {
    Button buttonWorkout, buttonHistory, buttonLoadWorkout;
    private static final int FILE_SELECT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_or_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        buttonWorkout = (Button) findViewById(R.id.chooseworkout);
        buttonHistory = (Button) findViewById(R.id.choosehistory);
        buttonLoadWorkout = (Button) findViewById(R.id.findPreMadeWorkout);
        buttonWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WorkoutSelectionScreenManual.class);
                startActivity(intent);

            }
        });
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryList.class);
                startActivity(intent);

            }
        });
        buttonLoadWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                ArrayList<WorkoutParameters> temp = extractInfo(uri);
                startWorkoutWithDirections(temp);
            }
        }
    }
    //Let User Pick File
    private void showFileChooser() {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, FILE_SELECT_CODE);
    }
    //Convert URI to usable workout parameters
    public ArrayList<WorkoutParameters> extractInfo(Uri uri) {
        InputStream inputStream = null;
        ArrayList<String> strings = new ArrayList<>();
        try {
            inputStream = getContentResolver().openInputStream(uri);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
            inputStream.close();
        }
            catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DecodeInputFile decodeInputFile = new DecodeInputFile(strings);
        ArrayList<WorkoutParameters>  params = decodeInputFile.getWorkoutParams();
        return params;

    }
    //Start Workouts Incept We Aren't In Control The Phone Is :O
    public void startWorkoutWithDirections(ArrayList<WorkoutParameters> workoutParameters) {
        Intent intent = new Intent(getApplicationContext(), WorkoutSelectionScreenAuto.class);
        intent.putExtra("wokoutParameters", workoutParameters);
        startActivity(intent);
    }

}
