package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthew.rehabrevamped.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by research on 9/5/2017.
 */

public class PhoneNumberView extends viewabstract  {
    String displayS="";
    GridLayout gridLayout;
    private final TextView resultText = new TextView(getContext());
    private GridLayout layout;
    double time=-1;
    private String currentPhoneNumber="";
    private String  origonalPhoneNumber ="";
    public PhoneNumberView(Context context) {
        super(context);
        layout = new GridLayout(getContext());
        viewinit();
    }

    public PhoneNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        layout = new GridLayout(getContext());
        viewinit();
    }

    public PhoneNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layout = new GridLayout(getContext());
        viewinit();
    }

    public PhoneNumberView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        layout = new GridLayout(getContext());
        viewinit();
    }

    /**
     * Draws a textview and fills it with a phone number, then draws a second textView, and finally
     * it creates an array of buttons that will add numbers to the currentPhoneNumber String.
     * all of these are added to the gridView.
     */
    public void viewinit() {
        setWillNotDraw(false);
        gridLayout = new GridLayout(getContext());
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        gridLayout.setColumnCount(1);
        gridLayout.setRowCount(3);


        TextView phoneNumberText = new TextView(getContext());
        phoneNumberText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        origonalPhoneNumber=createPhoneNumber();
        phoneNumberText.setText(origonalPhoneNumber);
        phoneNumberText.setTextSize(40);
        phoneNumberText.setTextColor(Color.WHITE);

        phoneNumberText.setGravity(Gravity.CENTER);


        //center

        resultText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        resultText.setText("");
        resultText.setTextSize(40);
        resultText.setTextColor(Color.WHITE);
        resultText.setGravity(Gravity.CENTER);

        gridLayout.addView(phoneNumberText,0);
        gridLayout.addView(resultText,1);

        layout.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setColumnCount(1);
        layout.setRowCount(4);

        gridLayout.addView(layout,2);
    }

    /**
     * gridView is added to the screen
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        removeAllViews();
        addView(gridLayout);
        final TextToSpeech tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        int number=0;
        for(int k= 0;k<5;k++) {
            LinearLayout buttonLayout = new LinearLayout(getContext());
            buttonLayout.setWeightSum(3);

            for (int i = 0; i < 3; i++) {
                if(number==10){
                    break;
                }
                final Button button = new Button(getContext());
                button.setTextSize(60);
                button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
                final int f = number;
                number++;
                button.setText(f + "");
                button.setBackgroundColor(Color.GRAY);
                button.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        double d = Calendar.getInstance().getTimeInMillis();
                        if (time < 0) {
                            time = d;
                        }
                        if ((d - time) > 200) {
                            tts.speak(f + "", TextToSpeech.QUEUE_ADD, null);
                            currentPhoneNumber = currentPhoneNumber + f;
                            time = d;
                            button.setBackgroundColor(Color.GREEN);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    button.setBackgroundColor(Color.GRAY);
                                }
                            }, 100);

                        }
                        return false;
                    }
                });
                buttonLayout.addView(button);

            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.height=layout.getHeight()/5;
            buttonLayout.setLayoutParams(lp);

            layout.addView(buttonLayout);
        }
    }

    @Override
    public void dataInput(float f) {

    }

    @Override
    public void stringInput(String s)
    {
    }

    /**
     * sends the currentPhoneNumber
     * @return
     */
    public String getPhoneNumber(){
     return currentPhoneNumber;
    }

    /**
     * sends the OriginalPhoneNumber
     * @return
     */
    public String getOriginalPhoneNumber(){
        return origonalPhoneNumber;
    }

    /**
     * changes the second textview
     * @param s
     */
    public void setResultText(String s){
        resultText.setText(s);
        currentPhoneNumber=s;
    }

    /**
     * creates a string of random numbers
     * @return
     */
    public String createPhoneNumber(){
        Random random =  new Random();
        String S = "";
        for(int i=0;i<12;i++) {
            int n = random.nextInt(10);
            S=S+n;
        }
        // DecimalFormat formatter = new DecimalFormat("#,###,###,####");
        // S = formatter.format((Object)S);
        return S;
    }
}