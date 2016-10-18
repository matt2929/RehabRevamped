package com.example.matthew.rehabrevamped.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Matthew on 7/15/2016.
 */
public class WorkoutHistoricalData implements Serializable {
    ArrayList<WorkoutSession> _history;

    public WorkoutHistoricalData(ArrayList<WorkoutSession> history) {
        _history = history;
    }

    public void addWorkout(WorkoutSession ws) {
        _history.add(ws);
    }

    public ArrayList<WorkoutSession> get_history() {
        return _history;
    }

    public boolean leftHand = false;
    public int grade = 0;

    public static class WorkoutSession implements Serializable {
        public Calendar CalendarData = Calendar.getInstance();
        public float JerkScore =0;
        public String WorkoutInfo = "";
        public String WorkoutName = "";
        public boolean LeftHand = false;
        public int Grade = 0;

        public WorkoutSession(String workoutname,float jerkscore, String workoutinfo, int grade, boolean leftHand) {
            WorkoutInfo = workoutinfo;
            JerkScore =jerkscore;
            WorkoutName = workoutname;
            LeftHand=leftHand;
            Grade=grade;
        }

        public Calendar get_Cal() {
            return CalendarData;
        }

        public float getJerkScore() {
            return JerkScore;
        }

        public String getWorkoutInfo() {
            return WorkoutInfo;
        }

        public String getWorkoutName() {
            return WorkoutName;
        }

        public int getGrade() {
            return Grade;
        }

        public boolean isLeftHand() {
            return LeftHand;
        }
    }

}
