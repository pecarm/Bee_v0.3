package com.example.bee_v03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedingAdapter extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, Object>> arrayList;

    public FeedingAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        arrayList = data;
        inflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String date = arrayList.get(position).get("date").toString();
        Record record = (Record) arrayList.get(position).get("record");
        String name = arrayList.get(position).get("name").toString();
        String preview;
        if (record.getFeeding().length() > 20) {
            preview = record.getFeeding().substring(0, 17) + "...";
        } else preview = record.getFeeding();

        if (name.length() > 20) {
            name = name.substring(0, 17) + "...";
        }

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_view_record, null);
        }

        TextView tvDate = convertView.findViewById(R.id.adapter_view_record_date);
        TextView tvPreview = convertView.findViewById(R.id.adapter_view_record_feeding);
        TextView tvName = convertView.findViewById(R.id.adapter_view_record_hive_name);

        tvDate.setText(date);
        tvPreview.setText(preview);
        tvName.setText(name);
        return convertView;
    }
}
