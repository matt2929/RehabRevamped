package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by Matthew on 9/24/2016.
 */

public class PourCupView extends viewabstract {
    public float percentFull;
    public Paint beerPaint, foamPaint;
    Random random = new Random();
    float foamStart=50;
    public PourCupView(Context context) {
        super(context);
        initView();
    }

    public PourCupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PourCupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public PourCupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView() {
        beerPaint = new Paint();
        beerPaint.setColor(Color.rgb(255,191,0));
        foamPaint = new Paint();
        foamPaint.setColor(Color.WHITE);
        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    foamStart += (random.nextInt(11)-5);
        float waterLeftHeight = getHeight() * (percentFull);
        canvas.drawRect(0,  (getHeight() - waterLeftHeight), getWidth(), getHeight(), beerPaint);
        canvas.drawRect(0,  (getHeight() - waterLeftHeight), getWidth(), (getHeight() - waterLeftHeight)+foamStart, foamPaint);
    }

    @Override
    public void dataInput(float f) {
        percentFull = f;
    }

    @Override
    public void stringInput(String s) {
    }
}
