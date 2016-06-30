package com.winston.dale.tiptracker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by dalewinston on 1/19/16.
 */
public class TipAdapter extends ArrayAdapter<TipEntry> {

    Context context;
    int layoutResourceId;
    ArrayList<TipEntry> data;

    public TipAdapter(Context context, int layoutResourceId, ArrayList<TipEntry> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TipHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new TipHolder();
            holder.date = (TextView) row.findViewById(R.id.date_text);
            holder.shift = (TextView) row.findViewById(R.id.shift_text);
            holder.amount = (TextView) row.findViewById(R.id.amount_text);

            row.setTag(holder);
        } else {
            holder = (TipHolder)row.getTag();
        }

        TipEntry tipEntry = data.get(position);
        holder.date.setText(tipEntry.getDate());
        holder.shift.setText(tipEntry.getShift());

        DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");

        holder.amount.setText("$" + decimalFormat.format(tipEntry.getAmount()));

        return  row;
    }

    static class TipHolder {
        TextView date;
        TextView amount;
        TextView shift;
    }

}

