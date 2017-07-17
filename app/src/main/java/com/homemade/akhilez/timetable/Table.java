package com.homemade.akhilez.timetable;

import android.support.v4.view.ViewPager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Math;

/**
 * Created by Akhilez on 1/10/2016.
 */
public class Table {

    //VARIABLES
    public static int[] pTimes = new int[10];    //{930,1020,1110,1125,1215,1305,1345,1435,1525,1615};
    public static String[] days={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    public static String[] daysSmall={"sun","mon","tue","wed","thu","fri","sat"};
    public static String[] daysFull={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    public static String timeString,dayString;
    public static int timeInt,dayInt;
    public static String[] subjects = new String[9]; //=  {"JAVA","FLAT","CO","DBMS","DAA","ES","GS","Java Lab","DBMS lab"};  //ReadDatabase.getSubjects();
    public static String j,f,c,d,a,g,jl,dl,e,l="Lunch",s="Break";
    public static String[] monday;
    public static String[] tuesday;
    public static String[] wednesday ;
    public static String[] thursday;
    public static String[] friday;
    public static String[] saturday;
    public static String[][] table;
    public static String fragmentDay;
    public static String[] colors = {"#8A2BE2","#3F51B5","#239A54","#ff8800","#FF4444","#D41F6F"};//#D41F6F

    //METHODS
    public static void initializeDaysToInitial(){
        Table.monday= new String[] {c,f,s,j,j,l,dl,dl,dl};
        Table.tuesday=new String[] {a,d,s,d,c,l,a,g,g};
        Table.wednesday = new String[] {j, j, s, c, d,l,f,a,e};
        Table.thursday= new String[] {f,f,s,j,j,l,a,e,d};
        Table.friday= new String[] {d,c,s,e,c,l,jl,jl,jl};
        Table.saturday= new String[] {c,a,s,e,f,l,d,a,f};
        Table.table = new String[][] {Table.monday,Table.tuesday,Table.wednesday,Table.thursday,Table.friday,Table.saturday};
    }
    public static void initializeSubjects(String[] subs){
        Table.j=Table.subjects[0];
        Table.f=Table.subjects[1];
        Table.c=Table.subjects[2];
        Table.d=Table.subjects[3];
        Table.a=Table.subjects[4];
        Table.g=Table.subjects[6];
        Table.jl=Table.subjects[7];
        Table.dl=Table.subjects[8];
        Table.e=Table.subjects[5];
    }
    public static String getTime(){
        Date date = new Date();
        String dateString=date.toString(),timeString="";
        int i,min=11,max=16;
        char c;
        for (i=min; i<max ; i++){
            if(i==13)continue;
            c=dateString.charAt(i);
            timeString += c;
        }
        return timeString;
    }
    public static String amOrPm(){
        Date date = new Date();
        String dateString=date.toString(),timeString="";
        timeString += dateString.charAt(18);
        timeString += dateString.charAt(19);
        return timeString;
    }
    public static int    getTime24hr(){
        int time = Integer.parseInt(getTime());
        String timeString=amOrPm();
        if(timeString.equals("PM"))time+=12;
        return time;
    }
    public static int    set24hr(String AMoPM,int time){
        if (AMoPM.equals("PM"))time+=12;
        return time;
    }
    public static String timeDif(int time1,int time2){
        int min1=(int)(time1/100)*60+(int)(time1%100);
        int min2=(int)(time2/100)*60+(int)(time2%100);
        min2=Math.abs(min2-min1);
        min1=0;
        if(min2>=60) {
            min1 = (int) (min2 / 60);
            min2 = min2 % 60;
        }
        String hr="",min="";
        if(min1<10)hr="0";
        if(min2<10)min="0";
        hr+=Integer.toString(min1);
        min+=Integer.toString(min2);
        return hr+":"+min;
    }
    public static String timeDif48(int time1,int time2){
        int min1=(int)(time1/100)*60+(int)(time1%100);
        int min2=(int)(time2/100)*60+(int)(time2%100);
        min2=Math.abs(min2-min1);
        min1=0;
        if(min2>=60) {
            min1 = 23-((int) (min2 / 60));
            min2 = 60-(min2 % 60);
        }
        String hr="",min="";
        if(min1<10)hr="0";
        if(min2<10)min="0";
        //if(min1>10)hr=Integer.toString(24-min1);
        hr+=Integer.toString(min1);
        min+=Integer.toString(min2);
        return hr+":"+min;
    }
    public static String getDay(){
        Date date = new Date();
        String dateString=date.toString(),dayString="";
        int i,min=0,max=3;
        char c;
        for (i=min; i<max ; i++){
            c=dateString.charAt(i);
            dayString += c;
        }
        return dayString;
    }
    public static int    bring(int pTime){
        if(pTime<pTimes[0]) return 9;
        else if (pTime>1615) return 10;
        for(int i=0; i<9; i++)
            if(pTime<=pTimes[i+1]) return i;
        return -1;
    }
    public static int    getDayInt(String dayString){
        for(int i=0;i<7;i++)
            if(dayString.equals(days[i]))
                return i;
        return -2;
    }
    public static String getPeriod(int cur,String dayString){
        if(dayString.equals("Sun"))return dayString;
        if(cur>=0 && cur<=9){
            String[] curDay=table[dayInt-1];
            return(curDay[cur]);
        }
        else return "timeOut";
    }
    public static void   goToPresent(ViewPager viewPager){
       // String curDayString = getDay();
        int curTime = Integer.parseInt(getTime());
        curTime=set24hr(amOrPm(),curTime);
        int curDay=getDayInt(getDay())-1;
        if(curDay!=-2&&curTime<1615){
            viewPager.setCurrentItem(curDay);
        }
        else viewPager.setCurrentItem((curDay+1)%6);
    }
    public static int    getNextDayInt(int curDayInt){
        return (curDayInt+1)%6;
    }
    public static int    getPrevDayInt(int curDayInt){
        return (curDayInt-1)%6;
    }
    public static int    getPrevDayInt(String curDayStr){
        int curDayInt = getDayInt(curDayStr);
        return (curDayInt-1)%6;
    }
    public static int timeStrtoInt(String time){
        //Log.i("time",time);
        if(time.equals(""))return 0;
        int hr= Integer.parseInt(""+time.charAt(0))*10+Integer.parseInt(""+time.charAt(1));
        int min = Integer.parseInt(""+time.charAt(3))*10+Integer.parseInt(""+time.charAt(4));
        return hr*100+min;
    }
    public static long getNextTime(){
        int secOnly = getSeconds();
        int curTime= getTime24hr();
        int curPer=bring(curTime);
        int desTime=0;
        if(curPer==9)desTime=pTimes[0];
        else if(curPer==10){
            desTime=pTimes[0];
            String timedif=timeDif48(curTime, desTime);
            int timedifInt = timeStrtoInt(timedif);
            int timedifMin=(timedifInt/100)*60+timedifInt%100;
            long timedifMill=timedifMin*60*1000 + (60 - secOnly)*1000;
            return timedifMill;
        }
        else desTime=pTimes[curPer+1];
        String timedif=timeDif(curTime,desTime);
        int timedifInt = timeStrtoInt(timedif);
        int timedifMin=(timedifInt/100)*60+timedifInt%100;
        long timedifMill=timedifMin*60*1000 + (60 - secOnly)*1000;
        return timedifMill;
    }
    public static int getSeconds(){
        String c = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int t = Character.getNumericValue(c.charAt(17))*10+Character.getNumericValue(c.charAt(18));
        //Log.d("Time : ", Integer.toString(t));//2016-08-01 14:59:16
        return t;
    }



}
