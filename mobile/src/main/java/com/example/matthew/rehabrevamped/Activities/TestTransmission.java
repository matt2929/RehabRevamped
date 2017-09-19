package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.Utilities.WorkoutParameters;

import java.util.ArrayList;

public class TestTransmission extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_transmission);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            ArrayList<WorkoutParameters> workoutParameters = (ArrayList<WorkoutParameters>) bundle.getSerializable("wokoutParameters");
            Toast.makeText(getApplicationContext(),"Data Loaded",Toast.LENGTH_SHORT).show();
        }
    }
}
