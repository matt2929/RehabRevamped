package com.example.matthew.rehabrevamped.Utilities;

import java.io.Serializable;

/**
 * Created by matt2929 on 9/18/17.
 */

public class WorkoutParameters implements Serializable{
    private int Repetitions = 0;
    private String Name = "Not Set";
    private String Object = "Not Set";
    private boolean[] AccelerometerChoice = {true,true,true};
    /*
     * @param Name the name of the activity
     * @param Object the name of the 3D printed object the patient will be using
     * @param Repetitions the amount of times you want the user to complete a given activity
     * @param The difficulty setting of a given workout input dependent upon workout selected
     */
    public WorkoutParameters(String Name,String Object,int Repetitions, boolean[] AccelerometerChoice){
        this.Name=Name;
        this.Repetitions=Repetitions;
        this.Object = Object;
        this.AccelerometerChoice = AccelerometerChoice;
    }

    public String getName(){
        return Name;
    }

    public int getRepetitions() {
        return Repetitions;
    }

    public boolean[] getAccelerometerChoice() {
        return AccelerometerChoice;
    }

    public void completedRepitition(){
        Repetitions--;
    }

    public String getObject(){
        return Object;
    }
}
