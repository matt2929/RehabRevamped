package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.ListView;

import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.Utilities.CalculateAverages;
import com.example.matthew.rehabrevamped.Utilities.HistoryDisplayAdapter;
import com.example.matthew.rehabrevamped.Utilities.SerializeWorkoutData;
import com.example.matthew.rehabrevamped.Utilities.WorkoutHistoricalData;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.ArrayList;


public class HistoryList extends Activity {
    private ListView lv;
    ArrayList<String> WorkoutStrings = new ArrayList<String>();
    static String workoutName = "";
    static ArrayList<WorkoutHistoricalData.WorkoutSession> AllWorkOuts = new ArrayList<WorkoutHistoricalData.WorkoutSession>();
    SerializeWorkoutData serializeWorkoutData;
    static int workoutPosition;
    ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
    String hand = "";
    /**adds a custom adaptor to listView using workOutSession data to determine how many 'slots', data
    sent to custom adaptor through an ArrayList<Object>
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        lv = (ListView) findViewById(R.id.HistoryView);

        try {
            serializeWorkoutData = new SerializeWorkoutData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AllWorkOuts = serializeWorkoutData.getUsers(getApplicationContext());
        WorkoutStrings.add("HistoryList\n");
        int i = 0;

        for (WorkoutHistoricalData.WorkoutSession s : AllWorkOuts) {
            if (!WorkoutStrings.contains("\nWorkout Name: " + s.getWorkoutName() + "\nHAND: <Left>")
                    && !WorkoutStrings.contains("\nWorkout Name: " + s.getWorkoutName() + "\nHAND: <Right>")) {
                ArrayList<Object> set = new ArrayList<Object>();
                set.add(s.getWorkoutName());
                ArrayList<Object> graphPoints = addPoints(s.getWorkoutName());
                set.addAll(graphPoints);
                data.add(set);
            }
            WorkoutStrings.add(
                    "\nWorkout Name: " + s.getWorkoutName()
                            + "\nHAND: <" + hand + ">");
            i++;
        }
        lv.setItemsCanFocus(false);
        lv.setAdapter(new HistoryDisplayAdapter(this, android.R.layout.simple_list_item_activated_1, data));
        //lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        Log.i("Alpha", "test");
                // AllWorkOuts = serializeWorkoutData.getUsers(getApplicationContext());
       //         workoutPosition = position - 1;
       //         workoutName = AllWorkOuts.get(workoutPosition).getWorkoutName();
       //         Intent intent = new Intent(getApplicationContext(), HistoryGraph.class);
       //         startActivity(intent);
       //     }
      // });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    /**
     * Creates a straight red line at the y axis value given in the parameter,
     * its meant to represent the Jerk Score the user should aim for.
     **/
    public LineGraphSeries<DataPoint> addAverageLine(ArrayList<Integer> sessions) {
        LineGraphSeries<DataPoint> lineGraphSeries = null;
        int value = CalculateAverages.calculateAverage(sessions);
        lineGraphSeries = new LineGraphSeries<DataPoint>();
        lineGraphSeries.appendData(new DataPoint(0, value), true, 2);
        lineGraphSeries.appendData(new DataPoint(sessions.size(), value), true, 2);
        lineGraphSeries.setThickness(3);
        lineGraphSeries.setColor(Color.parseColor("#FF0000"));
        return lineGraphSeries;
    }
    /**returns a LineGraphSeries<DataPoint> all x values are the jerkscores from sessions and all y
    values are incremented.
     **/
    public LineGraphSeries<DataPoint> workOutResults(ArrayList<Integer> sessions) {
        LineGraphSeries<DataPoint> series = null;
        DataPoint[] dataPoint = new DataPoint[sessions.size()];
        if (sessions.size() == 0) {
            // textView.setText("There is no data within these parameters.");
        } else {
            //  textView.setText("");
            //
            float start = 0;

            for (int i = 0; i < sessions.size(); i++) {
                start = (float) sessions.get(i);
                dataPoint[i] = new DataPoint(i, start);
            }
            series = new LineGraphSeries<>(dataPoint);
        }
        return series;
    }
    /**
     * retrieves the left and right hand data for the activity in the string, determine which
     has a lower average jerkscore, if the data size is less then 1 for either then it returns
     an arrayList of (null, dataPoints of the one with the larger jerkscore) else it returns
     (dataPoints of of the one with the larger jerkscore(note all y values would be the same so
     it displays as a line) ,dataPoints of the one with the larger jerkscore). It also sets the hand
     value equal to which ever data set has the higher average jerkscore.
     * @param name
     * @return
     */
    public ArrayList<Object> addPoints(String name){
        ArrayList<Object> linePoints = new ArrayList<Object>();
        ArrayList<Integer> sessionsLeft = new ArrayList<Integer>();
        ArrayList<Integer> sessionsRight = new ArrayList<Integer>();
        for (int i = 0; i < HistoryList.AllWorkOuts.size(); i++) {
            if(HistoryList.AllWorkOuts.get(i).getWorkoutName().equals(name) && HistoryList.AllWorkOuts.get(i).isLeftHand() == true) {
                sessionsLeft.add((int)HistoryList.AllWorkOuts.get(i).getJerkScore());
            }
            if(HistoryList.AllWorkOuts.get(i).getWorkoutName().equals(name) && HistoryList.AllWorkOuts.get(i).isLeftHand() == false) {
                sessionsRight.add((int)HistoryList.AllWorkOuts.get(i).getJerkScore());
            }
        }
        ArrayList<Integer> averageSession =  CalculateAverages.compareLines(sessionsLeft,sessionsRight);
        Log.i("sessionL",sessionsLeft.toString());
        Log.i("sessionR",sessionsRight.toString());
        if(averageSession.size() >1 ){
            linePoints.add(addAverageLine(CalculateAverages.compareLines(sessionsLeft,sessionsRight)));
        }
        else{
            linePoints.add(null);
        }
        if(averageSession.equals(sessionsLeft)) {
            linePoints.add(workOutResults(sessionsRight));
            hand = "Right";
        }
        if(averageSession.equals(sessionsRight)){
            linePoints.add(workOutResults(sessionsLeft));
            hand = "Left";
        }
        return linePoints;
    }
}