package com.example.bee_v03;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.w3c.dom.ls.LSException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SelectCustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<HivesLocation> expandableListTitle;
    private List<Hive> expandableListDetail;

    public SelectCustomExpandableListAdapter(Context context, List<HivesLocation> expandableListTitle, List<Hive> expandableListDetail) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.expandableListTitle = expandableListTitle;
    }

    //region Inherited methods
    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            List<Hive> filteredHives = this.expandableListDetail.stream()
                    .filter(hive -> hive.getId_location() == this.expandableListTitle.get(groupPosition).getId_location())
                    .collect(Collectors.toList());
            return filteredHives.size();
        }
        catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Hive> filteredHives = this.expandableListDetail.stream()
                .filter(hive -> hive.getId_location() == this.expandableListTitle.get(groupPosition).getId_location())
                .collect(Collectors.toList());
        return filteredHives.get(childPosition);
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
        final String expandedListText = getChild(groupPosition, childPosition).toString();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_select_hive, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandableListSelectHive);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    //endregion
}
