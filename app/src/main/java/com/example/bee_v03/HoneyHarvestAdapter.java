package com.example.bee_v03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class HoneyHarvestAdapter extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, Object>> data;

    public HoneyHarvestAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data = data;
        inflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String date = data.get(position).get("date").toString();
        HoneyHarvest harvest = (HoneyHarvest) data.get(position).get("harvest");

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_view_object_honey_harvest, null);
        }

        TextView tvDate = convertView.findViewById(R.id.adapter_view_object_honey_harvest_date);
        TextView tvAmount = convertView.findViewById(R.id.adapter_view_object_honey_harvest_amount);
        TextView tvWaterContent = convertView.findViewById(R.id.adapter_view_object_honey_harvest_water_content);

        tvDate.setText(date);
        tvAmount.setText(harvest.getAmount() + " kg");
        tvWaterContent.setText(harvest.getWater_content() + " %");

        return convertView;
    }
}
