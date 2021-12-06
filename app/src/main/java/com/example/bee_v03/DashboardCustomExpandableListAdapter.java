package com.example.bee_v03;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardCustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private List<Alert> expandableListDetail;
    private List<Hive> allHives;

    public DashboardCustomExpandableListAdapter(Context context, List<String> levels, List<Alert> alerts, List<Hive> hives) {
        this.context = context;
        this.expandableListTitle = levels;
        this.expandableListDetail = alerts;
        this.allHives = hives;
    }

    //region Inherited methods
    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            List<Alert> filteredAlerts = this.expandableListDetail.stream()
                    .filter(alert -> alert.getSeverity() == groupPosition + 1)
                    .collect(Collectors.toList());
            return filteredAlerts.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Alert> filteredAlerts = this.expandableListDetail.stream()
                .filter(alert -> alert.getSeverity() == groupPosition + 1)
                .collect(Collectors.toList());
        return filteredAlerts.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = getGroup(groupPosition).toString();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_select_location, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.expandableListSelectLocation);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Alert alert = (Alert) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_select_hive, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandableListSelectHive);

        Hive hive = allHives.stream()
                .filter(h -> h.getId_hive() == alert.getId_hive())
                .collect(Collectors.toList()).get(0);

        String preview;
        if (alert.getText().length() > 20) {
            preview = alert.getText().substring(0, 17) + "...";
        } else preview = alert.getText();
        expandedListTextView.setText(alert.getDate() + ", " + hive.getName() + ", " + preview);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    //endregion
}
