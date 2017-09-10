package com.example.matthew.rehabrevamped.Utilities;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.matthew.rehabrevamped.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by research on 8/3/2017.
 */

public class HistoryDisplayAdapter extends ArrayAdapter{
    private ArrayList<ArrayList<Object>> historyRowList;
    private int resource;
    private LayoutInflater inflater;

    /**
     * creates the adapter and assigns the data values
     * @param context
     * @param resource
     * @param objects
     */
    public HistoryDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ArrayList<Object>> objects) {
        super(context, resource, objects);
        historyRowList = objects;
        this.resource=resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * populates the adapters textfields and graphView
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView = inflater.inflate(R.layout.historylistrow,null);
        }
        TextView name=(TextView)convertView.findViewById(R.id.name);
        TextView hand=(TextView)convertView.findViewById(R.id.hand);
        GraphView graph=(GraphView)convertView.findViewById(R.id.graph);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(100.0);
        graph.removeAllSeries();
        name.setText((String)historyRowList.get(position).get(0));

        if(historyRowList.get(position).get(1)!=null){
            graph.addSeries((LineGraphSeries<DataPoint>)historyRowList.get(position).get(1));
        }
        graph.addSeries((LineGraphSeries<DataPoint>)historyRowList.get(position).get(2));
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Times");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Average Jerk Score");
        return convertView;
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }
}
