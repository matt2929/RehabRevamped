package com.example.matthew.rehabrevamped.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.matthew.rehabrevamped.R;

public class WelcomeScreen extends AppCompatActivity {
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        button = (Button) findViewById(R.id.welcomescreencontinue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), WorkoutOrHistory.class);
                startActivity(intent);

            }
        });
    }
}
