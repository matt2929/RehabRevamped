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

import java.util.ArrayList;
import java.util.Calendar;

public class HistoryGraph extends Activity {

    CustomViewGraph graphDataView;
    ListView listView;
    ArrayList<String> stringArrlist = new ArrayList<String>();
    ArrayList<Float> intArrlist = new ArrayList<Float>();
    ArrayList<WorkoutHistoricalData.WorkoutSession> sessions = new ArrayList<WorkoutHistoricalData.WorkoutSession>();
    ArrayList<WorkoutHistoricalData.WorkoutSession> temp = new ArrayList<WorkoutHistoricalData.WorkoutSession>();
    TextView textView;
    CheckBox checkBox;
    Button timeDurationButton;
    int biggest = Integer.MIN_VALUE;
    int smallest = Integer.MAX_VALUE;
    int currentDuration = 0;
    int currentShakeType = 0;

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
        graphDataView = (CustomViewGraph) findViewById(R.id.GraphData);
        graphDataView.setVisibility(View.VISIBLE);
        graphDataView.setBackgroundColor(Color.rgb(83,146,196));
        listView = (ListView) findViewById(R.id.datalist);
        textView = (TextView) findViewById(R.id.pointInfo);
        checkBox = (CheckBox) findViewById(R.id.line);
        timeDurationButton = (Button) findViewById(R.id.setDataDuration);
        setupViews();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                graphDataView.dotOrLine(b);
            }
        });
        timeDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDuration();
            }
        });
        graphDataView.setValues(intArrlist);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, stringArrlist));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    graphDataView.selectHighlightedData((intArrlist.size() - i) - 1);
                    textView.setText("The selected workout had a total of " + (intArrlist.get(i-1)).intValue() + " shakes.");
                    textView.setTextColor(Color.WHITE);
                    textView.setShadowLayer(20,5,5,Color.BLACK);
                }
            }
        });
    }

    public void setupViews() {

        if (sessions.size() == 0) {
            textView.setText("There is no data within these parameters.");
        } else {
            textView.setText("");
            graphDataView.selectHighlightedData(-1);
            //
            stringArrlist.clear();
            intArrlist.clear();
            float start = 0;
            int biggest = Integer.MIN_VALUE;
            int smallest = Integer.MAX_VALUE;
            stringArrlist.add("All \'"+sessions.get(0).getWorkoutName()+ "\' Activities:");

            for (int i = 0; i < sessions.size(); i++) {
                WorkoutHistoricalData.WorkoutSession s = sessions.get(i);

                start = (float) sessions.get(i).getJerkScore();
                stringArrlist.add((s.get_Cal().get(Calendar.MONTH) + 1) + "/" + s.get_Cal().get(Calendar.DAY_OF_MONTH)+ "/" + s.get_Cal().get(Calendar.YEAR)
                        + "\nTime: " + s.get_Cal().get(Calendar.HOUR_OF_DAY) + ":" + s.get_Cal().get(Calendar.MINUTE) + ":" + s.get_Cal().get(Calendar.SECOND) + "\nShakes: " + start + "\nHand: " + s.isLeftHand());
                intArrlist.add((float) start);
                if (start + .5 > biggest) {
                    biggest = (int) start;
                }
                if (start - .5 < smallest) {
                    smallest = (int) start;
                }
            }
            graphDataView.setValues(intArrlist);
            graphDataView.invalidate();
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
    public void changeDuration() {
        sessions = new ArrayList<WorkoutHistoricalData.WorkoutSession>();

        switch (currentDuration) {
            case 0:
                for (int i = 0; i < temp.size(); i++) {
                    WorkoutHistoricalData.WorkoutSession ws = temp.get(i);
                    Calendar cal = Calendar.getInstance();
                    if (ws.get_Cal().get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                        sessions.add(ws);
                    }
                }
                timeDurationButton.setText("Duration\nYear");
                currentDuration = 1;
                break;
            case 1:
                for (int i = 0; i < temp.size(); i++) {
                    WorkoutHistoricalData.WorkoutSession ws = temp.get(i);
                    Calendar cal = Calendar.getInstance();
                    if (ws.get_Cal().get(Calendar.MONTH) == cal.get(Calendar.MONTH) && ws.get_Cal().get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                        sessions.add(ws);
                    }
                }
                timeDurationButton.setText("Duration\nMonth");
                currentDuration = 2;
                break;
            case 2:
                for (int i = 0; i < temp.size(); i++) {
                    WorkoutHistoricalData.WorkoutSession ws = temp.get(i);
                    Calendar cal = Calendar.getInstance();

                    if (ws.get_Cal().get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH) && ws.get_Cal().get(Calendar.MONTH) == cal.get(Calendar.MONTH) && ws.get_Cal().get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                        sessions.add(ws);
                    }

                }
                timeDurationButton.setText("Duration\nToday");
                currentDuration = 3;
                break;
            case 3:
                timeDurationButton.setText("Duration\nAll Time");
                currentDuration = 0;
                sessions = new ArrayList<WorkoutHistoricalData.WorkoutSession>(temp);
                break;
        }
        setupViews();
    }

}
