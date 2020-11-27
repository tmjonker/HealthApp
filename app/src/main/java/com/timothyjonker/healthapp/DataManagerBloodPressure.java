/*
 * Copyright (c) 2020. Timothy Jonker @ NVCC
 *
 * Author: Timothy Jonker
 * Affiliation: NVCC
 *
 * Terms of Use:
 * This application is part of the term projects of the course ITP226 of Fall 2020.  It is not to released to any third party, whether with or without the permission of the author.  Any unauthorized use of this application may be subject to prosecution.
 */

package com.timothyjonker.healthapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DataManagerBloodPressure {
    private static final String myid = DataManagerBloodPressure.class.getName();

    public static final String databaseName = "health.db";
    private static final int databaseVersion = 1;
    private static final String tableName = "bloodPressureData";

    // This is the actual database
    private SQLiteDatabase mDB;


    public DataManagerBloodPressure(Context context) {
        // Create an instance of our internal CustomSQLiteOpenHelper class
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        // Get a writable database
        mDB = helper.getWritableDatabase();
    }

    // Here are all our helper methods

    // Insert a record
    public void insert(long date, long systolic, long diastolic){
        String query = String.format("INSERT INTO bloodPressureData (timeStamp, systolic, diastolic) " +
                "VALUES (%d, %d, %d);", date, systolic, diastolic);
        Log.i(myid, "insert() = " + query);
        mDB.execSQL(query);
    }

    // Show all the records
    public Cursor selectAll() {
        String query = String.format("SELECT timeStamp, systolic, diastolic FROM bloodPressureData " +
                "ORDER BY id DESC ;");
        Log.i(myid, "selectAll() = " + query);
        Cursor c = mDB.rawQuery(query, null);
        return c;
    }


    // Find a specific record
    public Cursor selectLastK(long K) {
        String query = String.format("SELECT timeStamp, systolic, diastolic FROM bloodPressureData " +
                "ORDER BY id DESC LIMIT %d ;", K);
        Log.i(myid, "selectLastK() = " + query);
        Cursor c = mDB.rawQuery(query, null);
        return c;
    }

    // Find a specific record
    public Cursor selectAverage() {
        String query = String.format("SELECT avg(systolic), avg(diastolic) FROM bloodPressureData ;");
        Log.i(myid, "selectAverage() = " + query);
        Cursor c = mDB.rawQuery(query, null);
        return c;
    }

    // Find a specific record
    public Cursor selectAverageLastK(long K) {
        String query = String.format("SELECT avg(systolic), avg(diastolic) FROM " +
                "( SELECT timeStamp, systolic, diastolic FROM bloodPressureData ORDER BY timeStamp DESC LIMIT %d );", K);
        Log.i(myid, "selectLastK() = " + query);
        Cursor c = mDB.rawQuery(query, null);
        return c;
    }

    public ArrayList<String[]> getData(Cursor cursor) {
        ArrayList<String[]> data = new ArrayList<>();
        int columns = cursor.getColumnCount();
        if (cursor.moveToFirst()) {
            do {
                if (columns == 3) {
                    // a regular row
                    long[] fields = new long[3];
                    fields[0] = cursor.getLong(0);
                    fields[1] = cursor.getLong(1);
                    fields[2] = cursor.getLong(2);

                    data.add(new String[]{TimeUtils.timeToDate(fields[0]),
                            Long.toString(fields[1]),
                            Long.toString(fields[2])});
                } else if (columns == 2) {
                    // An average value
                    double field1 = cursor.getDouble(0);
                    double field2 = cursor.getDouble(1);
                    data.add(new String[]{"-",
                            String.format("%.2f", field1),
                            String.format("%.2f", field2)});
                }
            } while (cursor.moveToNext());
        }
        return data;
    }


    // This class is created when DataManagerBloodPressure is initialized
    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) {
            super(context, databaseName, null, databaseVersion);
        }

        // This method only runs the first time the database is created
        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        // Allow new table to be added
        @Override
        public void onOpen(SQLiteDatabase db) {
            String query = String.format("CREATE TABLE IF NOT EXISTS bloodPressureData " +
                    "(id integer primary key autoincrement not null, timeStamp integer not null, " +
                    "systolic integer, diastolic integer); ");
            Log.i(myid, "create() = " + query);
            db.execSQL(query);
        }

        // This method only runs when we increment databaseVersion
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public static class TimeUtils {
        public static String timeToDate(long timeStamp) {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(timeStamp);
            String date = DateFormat.format("MMM. dd, yyyy hh:mm a", calendar).toString();
            return date;
        }

        public static String formatDuration(long milliseconds) {
            int seconds = (int) (milliseconds / 1000) % 60;
            int minutes = (int) (milliseconds / (1000*60)) % 60;
            int hours = (int) (milliseconds / (1000*60*60)) % 24;
            int days = (int) (milliseconds / (1000*60*60*24)) ;
            String duration = "";
            if (days>0) duration += " " + days + " days";
            if (hours>0) duration += " " + hours + " hours";
            if (minutes>0) duration += " " + minutes + " minutes";
            if (seconds>0) duration += " " + seconds + " seconds";
            if (duration.length()==0) duration += " 0 seconds";
            return duration.trim();
        }
    }

}
