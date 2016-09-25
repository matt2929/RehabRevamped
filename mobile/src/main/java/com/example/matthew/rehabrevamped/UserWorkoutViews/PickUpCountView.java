package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Matthew on 9/24/2016.
 */

public class PickUpCountView  extends RelativeLayout implements WorkoutView{


    public PickUpCountView(Context context) {
        super(context);
    }

    public PickUpCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PickUpCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PickUpCountView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public void dataInput(float f) {

    }

}
