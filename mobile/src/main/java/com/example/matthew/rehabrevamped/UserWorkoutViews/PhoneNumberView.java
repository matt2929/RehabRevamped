package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
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
    double time=-1;
    private String currentPhoneNumber="";
    private String  origonalPhoneNumber ="";
    public PhoneNumberView(Context context) {
        super(context);
        viewinit();
    }

    public PhoneNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewinit();
    }

    public PhoneNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewinit();
    }

    public PhoneNumberView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        gridLayout.setColumnCount(1);
        gridLayout.setRowCount(3);
        gridLayout.setFocusable(false);

        TextView phoneNumberText = new TextView(getContext());
        phoneNumberText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        origonalPhoneNumber=createPhoneNumber();
        Log.i("PhoneTest",origonalPhoneNumber);
        phoneNumberText.setText(origonalPhoneNumber);
        phoneNumberText.setTextSize(22);
        phoneNumberText.setTextColor(Color.WHITE);


        resultText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        resultText.setText("");
        resultText.setTextSize(22);
        resultText.setTextColor(Color.WHITE);

        gridLayout.addView(phoneNumberText,0);
        gridLayout.addView(resultText,1);

        GridLayout buttonLayout = new GridLayout(getContext());
            buttonLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
            buttonLayout.setColumnCount(3);
            buttonLayout.setRowCount(4);
        buttonLayout.setFocusable(false);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        for(int i = 0;i<10;i++) {
            final Button button = new Button(getContext());
            button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            button.setText(i + "");
            button.setFocusable(true);
            button.setFocusableInTouchMode(true);///add this line
            button.requestFocus();
            final int f = i;

            button.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    double d = Calendar.getInstance().getTimeInMillis();
                    if(time<0){
                        time = d;
                    }
                    if((d-time)>50){
                        currentPhoneNumber=currentPhoneNumber+f;
                        time=d;
                    }
                    return false;
                }
            });
            buttonLayout.addView(button, i);
        }
        gridLayout.addView(buttonLayout,2);
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
