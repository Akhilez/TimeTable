package com.homemade.akhilez.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.homemade.akhilez.timetable.TableContract.TimeTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akhilez on 1/30/2016.
 */
public class AssetsDBHelper extends SQLiteAssetHelper{

    private static final String DATABASE_NAME = "appDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public AssetsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getTimeTable(String presetName) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("select * from timetables where preset = '"+presetName+"'",null);
        c.moveToFirst();
        db.close();
        return c;

    }

    public String[] getColleges() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select distinct college from presets ",null);
        List<String> collegeList = new ArrayList<String>();
        collegeList.add("--select--");
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String s = c.getString(c.getColumnIndex("college"));
            if(!s.equals("initial"))collegeList.add(s);
        }
        String[] colleges = collegeList.toArray(new String[collegeList.size()]);
        c.close();
        db.close();
        //return new String[] {"KMIT","NULL"};
        return colleges;
    }

    public String[] getBranches(String college){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select distinct branch from presets where college = '" + college+"'" , null);
        List<String> collegeList = new ArrayList<String>();
        collegeList.add("--select--");
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            collegeList.add(c.getString(c.getColumnIndex("branch")));
        }
        String[] colleges = collegeList.toArray(new String[collegeList.size()]);
        c.close();
        db.close();
        //return new String[] {"KMIT","NULL"};
        return colleges;
    }

    public String[] getSections(String college, String branch){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select distinct sec from presets where college = '"+college+"' and branch = '"+branch+"'",null);
        List<String> collegeList = new ArrayList<String>();
        collegeList.add("--select--");
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            collegeList.add(c.getString(c.getColumnIndex("sec")));
        }
        String[] colleges = collegeList.toArray(new String[collegeList.size()]);
        c.close();
        db.close();
        //return new String[] {"KMIT","NULL"};
        return colleges;
    }

    public String[] getYears(String college, String branch, String sec){
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("select distinct acYear from presets where college = '"+college+"' and branch = '"+branch+"' and sec = '"+sec+"'",null);

        List<String> collegeList = new ArrayList<String>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            collegeList.add(c.getString(c.getColumnIndex("acYear")));
        }

        String[] colleges = collegeList.toArray(new String[collegeList.size()]);

        c.close();
        db.close();
        //return new String[] {"KMIT","NULL"};
        return colleges;
    }

    public String getPresetFromTable(String college, String branch, String section, String year){
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("select presetName from presets where college = '" + college + "' and branch = '" + branch + "' and sec = '" + section + "' and acYear = '" + year + "'", null);

        c.moveToFirst();
        String pname = c.getString(c.getColumnIndex("presetName"));
        c.close();
        db.close();

        return pname;
    }

    public String[] getTableDetails(String presetName){
        String[] details = {"","","",""};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select sec,branch,acYear,college from presets where presetName = '"+presetName+"'",null);
        cursor.moveToFirst();
        if(cursor.getCount()!=0)
            for (int i =0;i<4;i++)
                details[i] = cursor.getString(i);

        return details;
    }

}
