package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Matthew on 9/25/2016.
 */

public abstract class viewabstract extends RelativeLayout implements WorkoutView{
    Paint whitePaint;
    String displayS="";
    float gravX=0,gravY=0,gravZ=0;
    public viewabstract(Context context) {
        super(context);
    }

    public viewabstract(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public viewabstract(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public viewabstract(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void gimmiGrav(Float x, float y, float z) {

    }

    @Override
    public void dataInput(float f) {
    }

    @Override
    public void stringInput(String s) {

    }
}
