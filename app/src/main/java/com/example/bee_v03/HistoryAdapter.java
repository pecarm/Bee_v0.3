package com.example.bee_v03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryAdapter extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, Object>> arrayList;

    public HistoryAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        arrayList = data;
        inflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String date = arrayList.get(position).get("date").toString();
        String preview = arrayList.get(position).get("preview").toString();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_view_object_history, null);
        }

        TextView twDate = convertView.findViewById(R.id.adapter_view_object_history_date);
        TextView twPreview = convertView.findViewById(R.id.adapter_view_object_history_text);

        twDate.setText(date);
        twPreview.setText(preview);
        return convertView;
    }
}
