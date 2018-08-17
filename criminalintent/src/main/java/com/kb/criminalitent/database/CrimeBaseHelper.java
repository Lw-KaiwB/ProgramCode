package com.kb.criminalitent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "crimeBase.db";
    private static final int DATABASE_VERSION = 1;

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + CrimeDbSchema.CrimeTable.TABLE_NAME + "(" +
                "_id integer primary key autoincrement ," +
                CrimeDbSchema.CrimeTable.Cols.UUID + " ," +
                CrimeDbSchema.CrimeTable.Cols.TITLE + " ," +
                CrimeDbSchema.CrimeTable.Cols.DATE + " ," +
                CrimeDbSchema.CrimeTable.Cols.SOLVED + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
