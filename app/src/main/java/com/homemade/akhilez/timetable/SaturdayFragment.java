package com.homemade.akhilez.timetable;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Akhilez on 1/10/2016.
 */
public class SaturdayFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.monday_fragment, container, false);

        //com.example.akhilez.timetable.Table.dayString=com.example.akhilez.timetable.Table.getDay();
        Table.timeString=Table.getTime();

        Table.dayString="Sat";
        Table.fragmentDay=Table.dayString;
        //com.example.akhilez.timetable.Table.timeString="1040";
        Table.timeInt=Integer.parseInt(Table.timeString);
        Table.dayInt=Table.getDayInt(Table.dayString);
        int curPeriodInt=Table.bring(Table.timeInt);

        String[] dayArray = ViewPagerContents.getDayPeriods(Table.dayString);

        final RelativeLayout[] gapRelative = ViewPagerContents.getGapRelatives(rootView);
        final RelativeLayout[] relative = ViewPagerContents.getRelatives(Table.dayString, rootView);
        RelativeLayout lastRelative = (RelativeLayout) rootView.findViewById(R.id.rl10);
        final TextView[] periods = ViewPagerContents.getPeriodViews(Table.dayString, relative);
        final TextView[] times = ViewPagerContents.getTimeviews(rootView);

        for(int i=0;i<9;i++)
            periods[i].setText(dayArray[i]);

        for(int i=0;i<9;i++){
            if (periods[i].getText().equals("")){
                relative[i].setVisibility(View.GONE);
                if(i<8) gapRelative[i].setVisibility(View.GONE);
            }
            if(periods[i].getText().equals("BREAK")||periods[i].getText().equals("LUNCH")){
                periods[i].setTextColor(Color.parseColor("#a9a9a9"));
                periods[i].setTextSize(18);
            }
        }

        //SET HEADING
        RelativeLayout headingLayout = (RelativeLayout)rootView.findViewById(R.id.headingTable);
        if (Table.dayString.equals(Table.getDay())){
            TextView headingText = new TextView(this.getContext());
            headingText.setText("Today");
            headingText.setTypeface(Typeface.create("sans-serif-thin", Typeface.ITALIC));
            headingText.setTextColor(Color.parseColor(Table.colors[5]));
            headingText.setPadding(dpToPx(25),0,0,0);
            headingLayout.setPadding(0, 0, 0, 10);
            headingLayout.addView(headingText);
        }
        else if(Table.getDay().equals("Fri")){
            TextView headingText = new TextView(this.getContext());
            headingText.setText("Tomorrow");
            headingText.setTypeface(Typeface.create("sans-serif-thin", Typeface.ITALIC));
            headingText.setTextColor(Color.parseColor(Table.colors[5]));
            headingText.setPadding(dpToPx(25),0,0,0);
            headingLayout.setPadding(0, 0, 0, 10);
            headingLayout.addView(headingText);
        }
        else if(Table.getDay().equals("Sun")){
            TextView headingText = new TextView(this.getContext());
            headingText.setText("Yesterday");
            headingText.setTypeface(Typeface.create("sans-serif-thin", Typeface.ITALIC));
            headingText.setTextColor(Color.parseColor(Table.colors[5]));
            headingText.setPadding(dpToPx(25),0,0,0);
            headingLayout.setPadding(0, 0, 0, 10);
            headingLayout.addView(headingText);
        }

        //Setting up TIMER
        if(Table.dayString.equals(Table.getDay()))ViewPagerContents.setTimer(relative);
        if(Table.getDay().equals(Table.days[Table.getDayInt(Table.dayString)-1])&&Table.getTime24hr()>1630)ViewPagerContents.setTomTimer(relative);

        if(Table.getDay().equals(Table.fragmentDay)){
            if(curPeriodInt>=0 && curPeriodInt<=8) {
                ViewPagerContents.highlight(relative[curPeriodInt],periods[curPeriodInt],times[curPeriodInt],Table.colors[5]);
                ViewPagerContents.focus(periods[curPeriodInt + 1]);
                addTimer(relative[curPeriodInt]);
            }
        }


        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        return rootView;
    }
    public void addTimer(RelativeLayout parent){
        RelativeLayout rel = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        rel.setGravity(Gravity.CENTER_VERTICAL);
        params.setMargins(0,0,dpToPx(60),0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextView timerText = new TextView(getContext());
        timerText.setTextColor(Color.WHITE);
        timerText.setTypeface(null, Typeface.BOLD_ITALIC);
        timerText.setText("--:--:--");
        rel.setLayoutParams(params);
        rel.addView(timerText);
        parent.addView(rel);
        long nextTime=Table.getNextTime();
        Timer.startTimer(nextTime - 1001, 10, timerText);

    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
