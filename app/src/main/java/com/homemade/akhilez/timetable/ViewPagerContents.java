package com.homemade.akhilez.timetable;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Akhilez on 1/13/2016.
 */
public class ViewPagerContents {
    public static int i=8;
    public static String[] getDayPeriods(String dayString){
        String[] dayArray = {
                Table.getPeriod(0,dayString),
                Table.getPeriod(1,dayString),
                Table.getPeriod(2,dayString),
                Table.getPeriod(3,dayString),
                Table.getPeriod(4,dayString),
                Table.getPeriod(5,dayString),
                Table.getPeriod(6,dayString),
                Table.getPeriod(7,dayString),
                Table.getPeriod(8,dayString)
        };
        return dayArray;
    }

    public static RelativeLayout[] getGapRelatives(View rootView){
        final RelativeLayout[] relative = new RelativeLayout[10];
        relative[0]= (RelativeLayout) rootView.findViewById(R.id.list_gap1);
        relative[1]= (RelativeLayout) rootView.findViewById(R.id.list_gap2);
        relative[2]= (RelativeLayout) rootView.findViewById(R.id.list_gap3);
        relative[3]= (RelativeLayout) rootView.findViewById(R.id.list_gap4);
        relative[4]= (RelativeLayout) rootView.findViewById(R.id.list_gap5);
        relative[5]= (RelativeLayout) rootView.findViewById(R.id.list_gap6);
        relative[6]= (RelativeLayout) rootView.findViewById(R.id.list_gap7);
        relative[7]= (RelativeLayout) rootView.findViewById(R.id.list_gap8);
        return relative;
    }

    public static RelativeLayout[] getRelatives(String dayString,View rootView){
        final RelativeLayout[] relative = new RelativeLayout[10];
        relative[0]= (RelativeLayout) rootView.findViewById(R.id.rl1);
        relative[1]= (RelativeLayout) rootView.findViewById(R.id.rl2);
        relative[2]= (RelativeLayout) rootView.findViewById(R.id.rl3);
        relative[3]= (RelativeLayout) rootView.findViewById(R.id.rl4);
        relative[4]= (RelativeLayout) rootView.findViewById(R.id.rl5);
        relative[5]= (RelativeLayout) rootView.findViewById(R.id.rl6);
        relative[6]= (RelativeLayout) rootView.findViewById(R.id.rl7);
        relative[7]= (RelativeLayout) rootView.findViewById(R.id.rl8);
        relative[8]= (RelativeLayout) rootView.findViewById(R.id.rl9);
        relative[9]= (RelativeLayout) rootView.findViewById(R.id.rl10);
        return relative;
    }

    public static TextView[] getPeriodViews(String dayString,RelativeLayout[] relative){
        TextView[] periods = new TextView[10];
        periods[0] = (TextView) relative[0].findViewById(R.id.period1);
        periods[1] = (TextView) relative[1].findViewById(R.id.period2);
        periods[2] = (TextView) relative[2].findViewById(R.id.period3);
        periods[3] = (TextView) relative[3].findViewById(R.id.period4);
        periods[4] = (TextView) relative[4].findViewById(R.id.period5);
        periods[5] = (TextView) relative[5].findViewById(R.id.period6);
        periods[6] = (TextView) relative[6].findViewById(R.id.period7);
        periods[7] = (TextView) relative[7].findViewById(R.id.period8);
        periods[8] = (TextView) relative[8].findViewById(R.id.period9);
        periods[9] = (TextView) relative[9].findViewById(R.id.blank_list);
        return periods;
    }

    public static TextView[] getTimeviews(View relative){
        TextView times[] = new TextView[9];
        times[0] = (TextView) relative.findViewById(R.id.t1);
        times[1] = (TextView) relative.findViewById(R.id.t2);
        times[2] = (TextView) relative.findViewById(R.id.t3);
        times[3] = (TextView) relative.findViewById(R.id.t4);
        times[4] = (TextView) relative.findViewById(R.id.t5);
        times[5] = (TextView) relative.findViewById(R.id.t6);
        times[6] = (TextView) relative.findViewById(R.id.t7);
        times[7] = (TextView) relative.findViewById(R.id.t8);
        times[8] = (TextView) relative.findViewById(R.id.t9);

        for(int i=0;i<9;i++){
            times[i].setText(time24to12(Table.pTimes[i]));
        }

        return times;
    }
    public static String time24to12(int time){
        int min,hr;
        String minS,hrS;
        min =time%100;
        hr = time/100;
        if(hr>12)hr=hr-12;
        hrS=Integer.toString(hr);
        //if(hr<10)hrS="0"+Integer.toString(hr);
        minS=Integer.toString(min);
        if(min<10)minS="0"+Integer.toString(min);
        return hrS+":"+minS;
    }

    public static void highlight(RelativeLayout relative,TextView textView,TextView timeView, String color){
        //relative.setBackgroundColor(Color.parseColor(color));
        relative.setBackgroundResource(getBackground());//R.drawable.thu_highlighter);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setPadding(dpToPx(15),0,0,0);
        //timeView.setTextColor(Color.WHITE);
        //timeView.setTypeface(null, Typeface.BOLD_ITALIC);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private static int getBackground(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
            case Calendar.MONDAY:
                return R.drawable.type2mon;
            case Calendar.WEDNESDAY:
                return R.drawable.type2wed;
            case Calendar.TUESDAY:
                return R.drawable.type2tue;
            case Calendar.THURSDAY:
                return R.drawable.type2thu;
            case Calendar.FRIDAY:
                return R.drawable.type2fri;
            case Calendar.SATURDAY:
            default:
                return R.drawable.type2sat;
        }
    }

    public static void deHighlight(RelativeLayout relative,TextView textView,TextView timeView){
        relative.setBackgroundColor(Color.parseColor("#FAFAFA"));
        textView.setTextColor(Color.parseColor("#737373"));
        textView.setTypeface(null, Typeface.NORMAL);
        //timeView.setTextColor(Color.parseColor("#737373"));
        //timeView.setTypeface(null, Typeface.NORMAL);
    }

    public static void focus(TextView period){
        period.setFocusableInTouchMode(true);
        period.setFocusable(true);
        period.requestFocus();
    }

    public static void deFocus(TextView period){
        period.setFocusableInTouchMode(false);
        period.setFocusable(false);
        //period.requestFocus();
    }

    public static void setTimer(RelativeLayout[] relative){
        relative[0].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[0];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[1].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[1];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[2].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[2];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[3].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[3];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[4].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[4];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[5].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[5];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[6].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[6];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[7].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[7];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[8].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[8];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });
    }

    public static void setTomTimer(RelativeLayout[] relative){
        relative[0].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[0];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[1].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[1];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[2].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[2];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[3].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[3];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[4].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[4];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[5].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[5];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[6].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[6];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[7].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[7];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = com.homemade.akhilez.timetable.Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });

        relative[8].setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int desTime = com.homemade.akhilez.timetable.Table.pTimes[8];
                int nowTime = com.homemade.akhilez.timetable.Table.getTime24hr();
                String timeDif = Table.timeDif48(desTime, nowTime);
                Snackbar.make(v, timeDif, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

        });
    }
}