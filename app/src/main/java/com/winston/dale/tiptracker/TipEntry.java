package com.winston.dale.tiptracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by dalewinston on 1/19/16.
 */
public class TipEntry {

    private String date;
    private double amount;
    private String shift;
    private int week;
    private int id;

    public TipEntry(String date, double amount, String shift, int week) {
        this.date = date;
        this.amount = amount;
        this.shift = shift;
        this.week = week;
    }

    public TipEntry(String date, double amount, String shift, int week, int id) {
        this.date = date;
        this.amount = amount;
        this.shift = shift;
        this.week = week;
        this.id = id;
    }


    public static void addTipEntry(TipEntry tipEntry, Context context, boolean update) {
        TipDbHelper dbHelper = new TipDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the values
        values.put(DataContract.TipEntry.COLUMN_NAME_DATE, tipEntry.getDate());
        values.put(DataContract.TipEntry.COLUMN_NAME_AMOUNT, tipEntry.getAmount());
        values.put(DataContract.TipEntry.COLUMN_NAME_SHIFT, tipEntry.getShift());
        values.put(DataContract.TipEntry.COLUMN_NAME_WEEK, tipEntry.getWeek());


        if (update == true) {
            db.update(DataContract.TipEntry.TABLE_NAME, values, "_id=" + tipEntry.getId(), null);
        } else {
            long newRowId;
            newRowId = db.insert(DataContract.TipEntry.TABLE_NAME, null, values);
        }

    }


    public static ArrayList<TipEntry> getTipsByWeek(int week, Context context) {
        TipDbHelper dbHelper = new TipDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DataContract.TipEntry.TABLE_NAME + " WHERE "
                        + DataContract.TipEntry.COLUMN_NAME_WEEK + "=" + week + " ORDER BY " + DataContract.TipEntry.COLUMN_NAME_DATE + " DESC",
                null);

        ArrayList<TipEntry> tipEntries = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            //  Create a tip class that adds creates a tip object
            //  Then use the cursor to itterate through the database
            //  And create an arraylist of tip objects
            //  Display those tip objects
//            Comment comment = cursorToComment(cursor);
//            comments.add(comment);

            String date = cursor.getString(cursor.getColumnIndex(DataContract.TipEntry.COLUMN_NAME_DATE));
            double amount = cursor.getDouble(cursor.getColumnIndex(DataContract.TipEntry.COLUMN_NAME_AMOUNT));
            String shift = cursor.getString(cursor.getColumnIndex(DataContract.TipEntry.COLUMN_NAME_SHIFT));
            int whichWeek = cursor.getInt(cursor.getColumnIndex(DataContract.TipEntry.COLUMN_NAME_WEEK));
            int id = cursor.getInt(cursor.getColumnIndex(DataContract.TipEntry._ID));

            TipEntry tipEntry = new TipEntry(date, amount, shift, whichWeek, id);

            tipEntries.add(tipEntry);
            cursor.moveToNext();
        }

        return tipEntries;
    }

    public static void deleteTip(Context context, int id) {
        TipDbHelper dbHelper = new TipDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int deleted = db.delete(DataContract.TipEntry.TABLE_NAME, DataContract.TipEntry._ID + "=" + id, null);

        //db.rawQuery("DELETE FROM " + DataContract.TipEntry.TABLE_NAME + " WHERE " + DataContract.TipEntry._ID + "=" + id, null);
        Log.i("Delete method", deleted + "");
    }

    public static void updateTip(Context context, int id, String newDate, String newShift, double newAmount) {

    }

    public static String getWeeklyTips(int whichWeek, Context context) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
        ArrayList<TipEntry> tips = TipEntry.getTipsByWeek(whichWeek, context);
        double totalTips = 0;
        for (int i = 0; i < tips.size(); i++) {
            totalTips += tips.get(i).getAmount();
        }
        return  "$" + decimalFormat.format(totalTips);

    }

    public static String getYearTips(int totalWeeks, Context context) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
        double totalTips = 0;
        for (int i = 0; i < totalWeeks; i++) {
            ArrayList<TipEntry> tips = TipEntry.getTipsByWeek(i+1, context);
            for (int j = 0; j < tips.size(); j++) {
                totalTips += tips.get(j).getAmount();
            }
        }
        return "$" + decimalFormat.format(totalTips);
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getShift() {

        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public double getAmount() {

        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
