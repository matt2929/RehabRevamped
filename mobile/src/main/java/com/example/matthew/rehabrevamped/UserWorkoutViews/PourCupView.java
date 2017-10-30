package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.example.matthew.rehabrevamped.Utilities.SampleAverage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Matthew on 9/24/2016.
 */

public class PourCupView extends viewabstract {
    SampleAverage sampleAverage = new SampleAverage();
    public float percentFull;
    public Paint beerPaint, foamPaint;
    Random random = new Random();
    float foamStart = 20;
    float gravvX = 0, gravvY = 0, gravvZ = 0;
    float angle = 0;
    Float gravX = -1f, gravY = -1f;
    int spin = 0,updateCount=0,updateMax=5;
    ArrayList<bubbles> Bubbles= new ArrayList<bubbles>();
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
        beerPaint.setColor(Color.rgb(255, 191, 0));
        foamPaint = new Paint();
        foamPaint.setColor(Color.WHITE);
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void gimmiGrav(Float x, float y, float z) {
        gravvX = x;
        gravvY = y;
        gravvZ = z;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Random random = new Random();
        updateCount++;
        if(updateCount==updateMax){
            Bubbles.add(new bubbles(random.nextInt(getWidth()+10000)-5000,getHeight(),-15,15));
            Bubbles.add(new bubbles(random.nextInt(getWidth()),getHeight(),-15,15));
            Bubbles.add(new bubbles(random.nextInt(getWidth()),getHeight(),-15,15));
            updateCount=0;
        }

        double angletemp=Math.atan((double) (-gravvY / gravvX));
        if (gravvX == 0.0) {
            angletemp=0;
        }
        Log.e("ang:", "" + (float) Math.toDegrees(angletemp));
        if (gravX < 0) {
            canvas.rotate((90*3)-(sampleAverage.getMedianAverage()), getWidth() / 2, getHeight() / 2);
            Log.e("pos","left");

        } else {
            canvas.rotate(90-sampleAverage.getMedianAverage() , getWidth() / 2, getHeight() / 2);
            Log.e("pos","right");
        }
        foamStart += (random.nextInt(10) - 5);
        if (foamStart <= 0) {
            foamStart = 1;
        }
        float waterLeftHeight = getHeight() * (percentFull);
        canvas.drawRect(-5000, (getHeight() - waterLeftHeight), getWidth() + 5000, getHeight(), beerPaint);
        canvas.drawRect(-5000, (getHeight() - waterLeftHeight), getWidth() + 5000, (getHeight() - waterLeftHeight) + foamStart, foamPaint);
        ArrayList<bubbles> bubTemp=new ArrayList<bubbles>();

        for(bubbles b:Bubbles){
            b.update();

            canvas.drawCircle(b._X,b._Y,b._rad,foamPaint);
            if(b._Y<getHeight()-waterLeftHeight){
                b.set_speedY(1);
                b.set_Y(-100);
                bubTemp.add(b);
            }

        }
        for(bubbles b:bubTemp){
            Bubbles.remove(b);
        }


    }

    @Override
    public void dataInput(float f) {
        percentFull = f;
    }

    @Override
    public void stringInput(String s) {
        gravX = (float) (int) Float.parseFloat(s.split("\\,")[0]);
        Log.e("X:", "" + gravX);
        gravY = (float) (int) (Float.parseFloat(s.split("\\,")[1]));

        Log.e("Y:", "" + gravY);
        angle = ((float) Math.round(Math.toDegrees(Math.atan((double) (gravY / gravX)))));
        sampleAverage.addSmoothAverage(angle);
    }

    public class bubbles{
        float _X,_Y,_speedY,_rad;
        public bubbles(float x,float y,float speedy,int radius){
            _X=x;
            _Y=y;
            _speedY=speedy;
            _rad=radius;
        }
        public void update(){
            _Y+=_speedY;
        }

        public float get_rad() {
            return _rad;
        }

        public float get_Y() {
            return _Y;
        }

        public void set_speedY(float _speedY) {
            this._speedY = _speedY;
        }

        public void set_Y(float _Y) {
            this._Y = _Y;
        }
    }
}
