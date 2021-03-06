package com.example.matthew.rehabrevamped.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;


import com.example.matthew.rehabrevamped.R;
import com.example.matthew.rehabrevamped.Utilities.WorkoutHistoricalData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoryGraph extends Activity {

    ListView listView;
    ArrayList<String> stringArrlist = new ArrayList<String>();
    ArrayList<Float> intArrlist = new ArrayList<Float>();
    ArrayList<WorkoutHistoricalData.WorkoutSession> sessions = new ArrayList<WorkoutHistoricalData.WorkoutSession>();
    ArrayList<WorkoutHistoricalData.WorkoutSession> temp = new ArrayList<WorkoutHistoricalData.WorkoutSession>();
    TextView textView;
    CheckBox checkBox;
    GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        for (int i = 0; i < HistoryList.AllWorkOuts.size(); i++) {
            if (HistoryList.AllWorkOuts.get(i).getWorkoutName().equals(HistoryList.workoutName)) {
                sessions.add(HistoryList.AllWorkOuts.get(i));
            }
        }

        temp = new ArrayList<>(sessions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history_graph);
        graph = (GraphView) findViewById(R.id.graph);
        listView = (ListView) findViewById(R.id.datalist);
        textView = (TextView) findViewById(R.id.pointInfo);
        checkBox = (CheckBox) findViewById(R.id.line);
        setupViews();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, stringArrlist));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    textView.setText("The selected workout had a total of " + (intArrlist.get(i - 1)).intValue() + " shakes.");
                    textView.setTextColor(Color.WHITE);
                    textView.setShadowLayer(20, 5, 5, Color.BLACK);
                }
            }
        });
    }

    public void setupViews() {
        series = new LineGraphSeries<>();
        DataPoint[] dataPoint = new DataPoint[sessions.size()];
        if (sessions.size() == 0) {
            textView.setText("There is no data within these parameters.");
        } else {
            textView.setText("");
            //
            stringArrlist.clear();
            intArrlist.clear();
            float start = 0;
            stringArrlist.add("All \'" + sessions.get(0).getWorkoutName() + "\' Activities:");

            for (int i = 0; i < sessions.size(); i++) {
                WorkoutHistoricalData.WorkoutSession s = sessions.get(i);

                start = (float) sessions.get(i).getJerkScore();
                stringArrlist.add((s.get_Cal().get(Calendar.MONTH) + 1) + "/" + s.get_Cal().get(Calendar.DAY_OF_MONTH) + "/" + s.get_Cal().get(Calendar.YEAR)
                        + "\nTime: " + s.get_Cal().get(Calendar.HOUR_OF_DAY) + ":" + s.get_Cal().get(Calendar.MINUTE) + ":" + s.get_Cal().get(Calendar.SECOND) + "\nShakes: " + start + "\nHand: " + s.isLeftHand());
                intArrlist.add((float) start);
                dataPoint[i]=new DataPoint(i,start);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);
            graph.addSeries(series);
            listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_activated_1, stringArrlist) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Get the Item from ListView
                    View view = super.getView(position, convertView, parent);

                    // Initialize a TextView for ListView each Item
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextColor(Color.WHITE);
                    // Set the text color of TextView (ListView Item)

                    // Generate ListView Item using TextView
                    return view;
                }
            });
            listView.invalidate();
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            listView.invalidate();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


}
