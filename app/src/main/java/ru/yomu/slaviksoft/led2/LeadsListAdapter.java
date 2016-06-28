package ru.yomu.slaviksoft.led2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Slavik on 04.04.2015.
 */
public class LeadsListAdapter extends ArrayAdapter<LeadItem> {

    private DBLeads dbLeads;
    private  ArrayList<LeadItem> mItems;
    private Context context;

    public LeadsListAdapter(Context context, int resource) {
        super(context, resource);

        this.context = context;
        mItems = new ArrayList();
//        dbLeads = new DBLeads(context);
//        mItems = dbLeads.getAllLeads();
//        dbLeads.close();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_activated_1, parent, false);
        }

        // update the item view
        LeadItem item = getItem(position);
        TextView title = (TextView) convertView.findViewById(android.R.id.text1);
        title.setText(item.title);

        return convertView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public LeadItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).id;
    }

    public void update(){

        dbLeads = new DBLeads(context);
        mItems = dbLeads.getAllLeads();
        dbLeads.close();

        notifyDataSetChanged();

        Log.d("DEBUG", "update()");

    }

}


