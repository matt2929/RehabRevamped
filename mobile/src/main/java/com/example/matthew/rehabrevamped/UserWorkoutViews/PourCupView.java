package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.example.matthew.rehabrevamped.Utilities.SampleAverage;

import java.util.Random;

/**
 * Created by Matthew on 9/24/2016.
 */

public class PourCupView extends viewabstract {
    SampleAverage sampleAverage= new SampleAverage();
    public float percentFull;
    public Paint beerPaint, foamPaint;
    Random random = new Random();
    float foamStart=20;
    float gravvX=0,gravvY=0,gravvZ=0;
    float angle=0;
    Float gravX=-1f,gravY=-1f;
    int spin=0;
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
    public void gimmiGrav(Float x, float y, float z) {
        gravvX=x;
        gravvY=y;
        gravvZ=z;
    }

    @Override
    protected void onDraw(Canvas canvas) {
      //  canvas.rotate(sampleAverage.getMedianAverage());
        foamStart += (random.nextInt(10)-5);
        if(foamStart<=0){
            foamStart=1;
        }
        float waterLeftHeight = getHeight() * (percentFull);
        canvas.drawRect(-5000,  (getHeight() - waterLeftHeight), getWidth()+5000, getHeight(), beerPaint);
        canvas.drawRect(-5000,  (getHeight() - waterLeftHeight), getWidth()+5000, (getHeight() - waterLeftHeight)+foamStart, foamPaint);
        Log.e("ang:",""+(float) Math.toDegrees(Math.atan((double) (-gravvY/gravvX))));

    }

    @Override
    public void dataInput(float f) {
        percentFull = f;
    }

    @Override
    public void stringInput(String s) {
        gravX=(float)(int)Float.parseFloat(s.split("\\,")[0]);
        Log.e("X:",""+gravX);
        gravY=(float)(int)(Float.parseFloat(s.split("\\,")[1]));


        Log.e("Y:",""+gravY);
        //angle = ((float)Math.round(Math.toDegrees(Math.atan((double) (-gravY/gravX)))));
        sampleAverage.addSmoothAverage(angle);
    }
}
