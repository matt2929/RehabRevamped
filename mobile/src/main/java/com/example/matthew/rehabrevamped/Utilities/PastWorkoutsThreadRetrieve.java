package com.example.matthew.rehabrevamped.Utilities;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by matt2929 on 10/29/17.
 */

public class PastWorkoutsThreadRetrieve implements Runnable {
    SerializeWorkoutData serializeWorkoutData;
    Context context;
    ArrayList<WorkoutHistoricalData.WorkoutSession> workoutSessions;
    Boolean taskComplete = false;

    public PastWorkoutsThreadRetrieve(Context context) {
        this.context = context;
        run();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            serializeWorkoutData = new SerializeWorkoutData(context);
            workoutSessions = serializeWorkoutData.getUsers(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        taskComplete = true;
    }

    public float getSpecificWorkoutAverage(String workName) {
        if (!taskComplete) return -1f;
        float sum = 0;
        float num = 0;
        for (int i = 0; i < workoutSessions.size(); i++) {
            if (workName.equals(workoutSessions.get(i).getWorkoutName())) {
                sum += workoutSessions.get(i).getJerkScore();
                num++;
            }
        }
        if (num == -1) return -1;
        return (sum / num);
    }
}
