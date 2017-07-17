package com.homemade.akhilez.timetable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.homemade.akhilez.timetable.TableContract.Subjects;
import com.homemade.akhilez.timetable.TableContract.TimeTable;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.net.ssl.SSLPeerUnverifiedException;


/**
 * Created by Akhilez on 1/24/2016.
 */

public class OpenDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_PATH = "/data/data/com.homemade.akhilez.timetable/databases/"; //Environment.getExternalStorageDirectory().getPath();//
    public static final String DATABASE_NAME = "appInternalDatabase.db";
    public static final String COMMA = ",";
    public static String[] subjects;
    private static final int DATABASE_VERSION = 1;
    public boolean dbExist;
    public SQLiteDatabase dbSql;
    public static int noOfSubjects;

    //METHODS
    public OpenDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        /*dbExist=DbExists();
        if(!dbExist) {
            dbSql = this.getWritableDatabase();
        }
        */
    }

    public boolean isTableEmpty(String tableName){
        if(dbSql!=null)dbSql.close();
        SQLiteDatabase db = getReadableDatabase();
        String count = "SELECT count(*) FROM "+tableName;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        mcursor.close();
        db.close();
        return !(icount>0);
    }

    public void setPreset(AssetsDBHelper assetsDBHelper,String presetName){

        if(dbSql!=null)dbSql.close();
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from timetable;");
        //setSubjects(assetsDBHelper, presetName);
        setTimeTable(assetsDBHelper, presetName);

        //dbSql.close();
        //dbSql.execSQL("insert into timetable values(1,1,1,'a','a','a','a','a','a')");
    }

    public void setTimeTable(AssetsDBHelper assetsDBHelper,String presetName){
        if(dbSql!=null)dbSql.close();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = assetsDBHelper.getTimeTable(presetName);
        String pno,startTime,endTime,mon,tue,wed,thu,fri,sat;
        String query;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            pno = cursor.getString(cursor.getColumnIndex("pno"));
            startTime = cursor.getString(cursor.getColumnIndex("startTime"));
            endTime = cursor.getString(cursor.getColumnIndex("endTime"));
            mon = cursor.getString(cursor.getColumnIndex("mon"));
            tue = cursor.getString(cursor.getColumnIndex("tue"));
            wed = cursor.getString(cursor.getColumnIndex("wed"));
            thu = cursor.getString(cursor.getColumnIndex("thu"));
            fri = cursor.getString(cursor.getColumnIndex("fri"));
            sat = cursor.getString(cursor.getColumnIndex("sat"));
            query = "insert into "+TimeTable.TABLE_NAME+" values ( "+pno+COMMA+"'"+startTime+"'"+COMMA+"'"+endTime+"'"+COMMA+"'"+mon+"'"+COMMA+"'"+tue+"'"+COMMA+"'"+wed+"'"+COMMA+"'"+thu+"'"+COMMA+"'"+fri+"'"+COMMA+"'"+sat+"'"+" )";
            db.execSQL(query);

        }
        cursor.close();
        setSubjects();

    }

    public void setSubjects(){
        String[] days = {"mon","tue","wed","thu","fri","sat"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        ArrayList<String> subs = new ArrayList<>();
        for(int j=0; j< days.length; j++){
            c = db.rawQuery("select distinct "+days[j]+" from timetable",null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String s = c.getString(0);
                int count = 0;
                for(int i=0;i<subs.size();i++){
                    if(s.equals(subs.get(i))) {
                        count++;
                        break;
                    }
                }
                if(count == 0)
                    subs.add(s);
            }
        }
        subjects = subs.toArray(new String[0]);
    }

    public void setEditedPeriod(String day,String row,String newPeriod){
        if(dbSql!=null) dbSql.close();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "update timetable set "+day+" = '"+newPeriod+"' where pno = "+row;
        db.execSQL(sql);
        db.close();
    }

    public boolean DbExists() {
        SQLiteDatabase db = null;
        try {
            String databasePath = DATABASE_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(databasePath, null,
                    SQLiteDatabase.OPEN_READWRITE);

            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.setVersion(1);

        }

        catch (SQLiteException e) {
            Log.e("SqlHelper", "Database Not Found");
        }

        if (db != null) {
            db.close();
        }

        return db != null ? true : false;
    }

    public void initializeDays(){
        Table.monday= getPeriodsOf("mon");//new String[] {c,f,s,j,j,l,dl,dl,dl};
        Table.tuesday= getPeriodsOf("tue");//new String[] {a,d,s,d,c,l,a,g,g};
        Table.wednesday = getPeriodsOf("wed");//new String[] {j, j, s, c, d,l,f,a,e};
        Table.thursday= getPeriodsOf("thu");//new String[] {f,f,s,j,j,l,a,e,d};
        Table.friday= getPeriodsOf("fri");//new String[] {d,c,s,e,c,l,jl,jl,jl};
        Table.saturday= getPeriodsOf("sat");//new String[] {c,a,s,e,f,l,d,a,f};
        Table.table = new String[][] {Table.monday,Table.tuesday,Table.wednesday,Table.thursday,Table.friday,Table.saturday};
    }

    public String[] getPeriodsOf(String day){
        if(dbSql!=null)dbSql.close();
        SQLiteDatabase db = getReadableDatabase();
        String[] periods = new String[10];

        Cursor cursor = db.rawQuery("select "+day+" from "+TimeTable.TABLE_NAME,null);

        int i =0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            periods[i]=cursor.getString(cursor.getColumnIndex(day));
            i++;
        }
        cursor.close();
        db.close();
        return periods;
    }

    public String[] getSubjects(){
        return subjects;
        //return new String[] {"JAVA","FLAT","CO","DBMS","DAA","ES","GS","Java Lab","DBMS lab"};
    }

    public int[] getTimes(){
        if(dbSql!=null)dbSql.close();
        SQLiteDatabase db = getReadableDatabase();
        String[] subjects = new String[11];
        int[] times = new int[11];

        Cursor cursor = db.rawQuery("select startTime from timetable", null);
        //cursor.moveToFirst();

        int i =0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            subjects[i]=cursor.getString(cursor.getColumnIndex("startTime"));
            times[i]=Table.timeStrtoInt(subjects[i]);
            i++;
        }
        cursor.close();
        cursor = db.rawQuery("select endTime from timetable",null);
        cursor.moveToLast();
        times[i]=Table.timeStrtoInt(cursor.getString(cursor.getColumnIndex("endTime")));
        cursor.close();
        db.close();
        return times;
        //return new int[] {930,1020,1110,1125,1215,1305,1345,1435,1525,1615};
    }

    public int getEndTime(){
        int x;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select endTime from timetable",null);
        c.moveToLast();
        //Log.d("endTime : ",c.getString(0));
        x = Table.timeStrtoInt(c.getString(0));
        c.close();
        db.close();
        return x;

    }

    public int getStartTime(){
        int x;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select startTime from timetable",null);
        c.moveToFirst();
        //Log.d("StartTime : ",c.getString(0));
        x = Table.timeStrtoInt(c.getString(0));
        c.close();
        db.close();
        return x;
    }

    public static int getNoOfSubjects(SQLiteDatabase db){
        return subjects.length;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TimeTable.TABLE_NAME + " (" +
                TimeTable.COLUMN_NAME_PNO + " integer primary key," +
                TimeTable.COLUMN_NAME_STARTTIME + " varchar," +
                TimeTable.COLUMN_NAME_ENDTIME + " varchar," +
                TimeTable.COLUMN_NAME_MON + " varchar," +
                TimeTable.COLUMN_NAME_TUE + " varchar," +
                TimeTable.COLUMN_NAME_WED + " varchar," +
                TimeTable.COLUMN_NAME_THU + " varchar," +
                TimeTable.COLUMN_NAME_FRI + " varchar," +
                TimeTable.COLUMN_NAME_SAT       + " varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TimeTable.TABLE_NAME);
        onCreate(db);
    }


}
