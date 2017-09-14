package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Matthew on 9/24/2016.
 */

public class WalkWithCupView extends viewabstract{
    public WalkWithCupView(Context context) {
        super(context);
        viewinit();
    }

    public WalkWithCupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewinit();
    }

    public WalkWithCupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewinit();
    }

    @Override
    public void gimmiGrav(Float x, float y, float z) {

    }

    public WalkWithCupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        viewinit();
    }
    public void viewinit() {
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        setBackgroundColor(Color.BLACK);
        whitePaint.setTextSize(60);
        whitePaint.setTextAlign(Paint.Align.CENTER);
    }
    @Override
    public void dataInput(float f) {

    }
    @Override
    public void stringInput(String s) {
        displayS=s;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        whitePaint.setColor(Color.BLUE);
        canvas.drawRect(0,0,getWidth(),getHeight(),whitePaint);
        whitePaint.setColor(Color.LTGRAY);
        canvas.drawCircle(100,35,5,whitePaint);
        canvas.drawCircle(200,900,5,whitePaint);
        canvas.drawCircle(343,315,5,whitePaint);
        canvas.drawCircle(412,400,5,whitePaint);
        canvas.drawCircle(567,200,5,whitePaint);
        canvas.drawCircle(810,900,5,whitePaint);
        canvas.drawCircle(988,1300,5,whitePaint);
        whitePaint.setColor(Color.WHITE);
        canvas.drawText(displayS,getWidth()/2,getHeight()/2,whitePaint);
    }
}
