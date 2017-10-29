package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Matthew on 9/25/2016.
 */

public class UnlockPhoneView extends viewabstract {
    Paint bluePaint;
    Paint redPaint;
    Paint yellowPaint;
    Paint whitePaint,whitePaintCenter;
    Paint blackPaint;
    Float x = null, y = null;
    String count="";
    Integer horzontalMax;

    Point focalPoint = new Point();
    float tempWidth;


    /**
     * creates a arc and red rectangle
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        focalPoint.set(getWidth()/2,(7*getHeight())/8);
        horzontalMax=focalPoint.y/3;
        yellowPaint.setStrokeWidth(3);
        yellowPaint.setStyle(Paint.Style.STROKE);
        redPaint.setStrokeWidth(10);
        Log.i("RectF",(focalPoint.x-horzontalMax)+","+(focalPoint.y-horzontalMax)+","+(focalPoint.x+horzontalMax)+","+(focalPoint.y+(focalPoint.y-horzontalMax)));

        RectF arc=new RectF(focalPoint.x-horzontalMax,focalPoint.y-horzontalMax,focalPoint.x+horzontalMax,focalPoint.y+horzontalMax);
        tempWidth=arc.width();
        canvas.drawArc(arc,0,360,false,yellowPaint);
        canvas.drawPoint(focalPoint.x,focalPoint.y,redPaint);
        //canvas.drawRect(0,(3*getHeight())/4,getWidth(),getHeight(),redPaint);

        canvas.save();
        canvas.restore();
    }
    public float getTempWidth(){
        return tempWidth;
    }
    @Override
    public void dataInput(float f) {
        super.dataInput(f);
    }

    @Override
    public void stringInput(String s) {
            x = Float.valueOf(s.split("\\,")[0]);
            y = (Float.valueOf(s.split("\\,")[1]));
            count = s.split("\\,")[2];
    }

    public void initView() {

        bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(30);
        whitePaintCenter = new Paint();
        whitePaintCenter.setColor(Color.WHITE);
        whitePaintCenter.setTextSize(40);
        whitePaintCenter.setTextAlign(Paint.Align.CENTER);

        blackPaint=new Paint();
        blackPaint.setColor(Color.BLACK);
        setBackgroundColor(Color.BLACK);
        whitePaint.setTextAlign(Paint.Align.CENTER);

    }

    public UnlockPhoneView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void gimmiGrav(Float x, float y, float z) {

    }

    public UnlockPhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public UnlockPhoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public UnlockPhoneView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    public Point getFocalPoint(){
        return focalPoint;
    }
    public Integer getHorizontalMax(){
        return horzontalMax;
    }

}
