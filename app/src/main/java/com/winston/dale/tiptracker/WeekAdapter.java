package com.winston.dale.tiptracker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dalewinston on 1/19/16.
 */
public class WeekAdapter extends ArrayAdapter<Week> {
    Context context;
    int layoutResourceId;
    ArrayList<Week> weeks;

    public WeekAdapter(Context context, int layoutResourceId, ArrayList<Week> weeks) {
        super(context, layoutResourceId, weeks);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.weeks = weeks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeekHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new WeekHolder();
            holder.date = (TextView) row.findViewById(R.id.date_string);
            holder.amount = (TextView) row.findViewById(R.id.amount_string);

            row.setTag(holder);
        } else {
            holder = (WeekHolder) row.getTag();
        }
        Week week = weeks.get(position);
        holder.date.setText(week.getDate());
        holder.amount.setText(week.getAmount());

        return row;
    }

    static class WeekHolder {
        TextView date;
        TextView amount;
    }
}
