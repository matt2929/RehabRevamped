package com.example.matthew.rehabrevamped.Utilities;

import java.util.ArrayList;

/**
 * Created by research on 8/29/2017.
 */

public class CalculateAverages {
    /**
     * Finds the average of (ArrayList<Integer> sessions) and returns it.
     * @param sessions
     * @return
     */
    public static int calculateAverage(ArrayList<Integer> sessions){
        int average=0;
        for(int i = 0;i<sessions.size();i++){
            average = average + sessions.get(i);
        }
        int value = (int)average/sessions.size();
        return value;
    }
    /**
     * compares the average values in ArrayList<Integer> sessionsLeft, and ArrayList<Integer> sessionsRight
     * and returns the one with the lower average.
     * @param sessionsLeft
     * @param sessionsRight
     * @return
     */
    public static ArrayList<Integer> compareLines(ArrayList<Integer> sessionsLeft,
                                            ArrayList<Integer> sessionsRight) {
        if(sessionsLeft.size()<1){
            return sessionsLeft;
        }
        if(sessionsRight.size()<1){
            return sessionsRight;
        }
        float averageLeftJerkScore=0;
        for(int i = 0;i<sessionsLeft.size();i++){
            averageLeftJerkScore = averageLeftJerkScore + sessionsLeft.get(i);
        }
        averageLeftJerkScore = averageLeftJerkScore/sessionsLeft.size();

        float averageRightJerkScore=0;
        for(int i = 0;i<sessionsRight.size();i++){
            averageRightJerkScore = averageRightJerkScore + sessionsRight.get(i);
        }
        averageRightJerkScore = averageRightJerkScore/sessionsRight.size();

        if(averageLeftJerkScore>averageRightJerkScore){
            return sessionsRight;
        }
        else{
            return sessionsLeft;
        }
    }

}
