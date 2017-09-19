package com.example.matthew.rehabrevamped.Utilities;

import java.util.ArrayList;

/**
 * Created by matt2929 on 9/18/17.
 * Input Lines From a file converted to workout paramerters so we know what the doctor wants the patient to do
 */
//
    /*
    @params params list of strings that are from the prescribed workout file.

     */
public class DecodeInputFile {
    private ArrayList<WorkoutParameters> params = new ArrayList<>();
    //Example Activity Stuff For Now
    public DecodeInputFile(ArrayList<String> strings) {
        params.add(new WorkoutParameters("V.Pickup", "Cup", 1, new boolean[]{true,true,true}));
        params.add(new WorkoutParameters("H.Pickup", "Cup", 2,  new boolean[]{true,true,true}));
        params.add(new WorkoutParameters("Twist", "Cup", 3,  new boolean[]{true,true,true}));
        params.add(new WorkoutParameters("PhoneNumber", "None", 4,  new boolean[]{true,true,true}));
        params.add(new WorkoutParameters("WalkSpill", "Bowl", 5,  new boolean[]{true,true,true}));
    }

    public ArrayList<WorkoutParameters> getWorkoutParams() {
        return params;
    }
}
