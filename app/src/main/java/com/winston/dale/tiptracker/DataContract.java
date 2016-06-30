package com.winston.dale.tiptracker;

import android.provider.BaseColumns;

/**
 * Created by dalewinston on 1/14/16.
 */
public final class DataContract {
    public DataContract() { }

    // Tip Entry Table Schema
    public static abstract class TipEntry implements BaseColumns {
        public static final String TABLE_NAME = "tip";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_SHIFT = "shift";
        public static final String COLUMN_NAME_WEEK = "week";

        public static final String SQL_CREATE_TIP_TABLE = "CREATE TABLE " + TipEntry.TABLE_NAME +
                " (" + TipEntry._ID + " INTEGER PRIMARY KEY,"  + TipEntry.COLUMN_NAME_DATE +
                " TEXT," + TipEntry.COLUMN_NAME_AMOUNT + " REAL," + TipEntry.COLUMN_NAME_SHIFT +
                " TEXT," + TipEntry.COLUMN_NAME_WEEK + " INTEGER);";

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TipEntry.TABLE_NAME;
    }

    // Paycheck Entry Table Schema


    // SQL statements

}
