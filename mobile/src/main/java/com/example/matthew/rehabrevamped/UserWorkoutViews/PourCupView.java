package com.example.matthew.rehabrevamped.UserWorkoutViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Matthew on 9/24/2016.
 */

public class PourCupView extends RelativeLayout implements WorkoutView{
    public PourCupView(Context context) {
        super(context);
    }

    public PourCupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PourCupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PourCupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void dataInput(float f) {

    }
}
