package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Matthew on 9/24/2016.
 */

public class WalkWithCupView extends RelativeLayout implements WorkoutView{
    public WalkWithCupView(Context context) {
        super(context);
    }

    public WalkWithCupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WalkWithCupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WalkWithCupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void dataInput(float f) {

    }
}
