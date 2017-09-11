package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by research on 9/4/2017.
 */

public class HorizontalPickUpCountView extends viewabstract {
    Paint whitePaint;
    String displayS="";
    float gravX=0,gravY=0,gravZ=0;
    public HorizontalPickUpCountView(Context context) {
        super(context);
        viewinit();
    }

    public HorizontalPickUpCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewinit();
    }

    public HorizontalPickUpCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewinit();
    }

    public HorizontalPickUpCountView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
    public void gimmiGrav(Float x, float y, float z) {
        gravX=x;
        gravY=y;
        gravZ=z;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(displayS,getWidth()/2,getHeight()/2,whitePaint);
    }


    @Override
    public void dataInput(float f) {

    }

    @Override
    public void stringInput(String s)
    {
        displayS=s;
    }

}
