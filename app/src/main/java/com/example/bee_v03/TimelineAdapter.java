package com.example.bee_v03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class TimelineAdapter extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, Object>> arrayList;

    public TimelineAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        arrayList = data;
        inflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record record = (Record) arrayList.get(position).get("record");
        String date = record.getYear()+"/"+record.getMonth()+"/"+record.getDay();
        String name = arrayList.get(position).get("name").toString();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_view_dashboard_timeline, null);
        }

        TextView twDate = convertView.findViewById(R.id.adapter_view_dashboard_timeline_date);
        TextView twName = convertView.findViewById(R.id.adapter_view_dashboard_timeline_hive);

        twDate.setText(date);
        twName.setText(name);
        return convertView;
    }
}
