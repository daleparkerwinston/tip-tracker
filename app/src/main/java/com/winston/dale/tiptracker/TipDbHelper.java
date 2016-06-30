package com.winston.dale.tiptracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by dalewinston on 1/14/16.
 */
public class TipDbHelper extends SQLiteOpenHelper{

    public static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tips.db";

    public TipDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataContract.TipEntry.SQL_CREATE_TIP_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataContract.TipEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
