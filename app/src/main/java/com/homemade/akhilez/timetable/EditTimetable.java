package com.homemade.akhilez.timetable;

import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class EditTimetable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String curEditDay="mon";
    public LinearLayout subsLinear,periodsLinear;
    public OpenDBHelper myDb;
    public TextView periodsView[],subjectsView[];
    private TextView headingDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timetable);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        myDb = new OpenDBHelper(this);
        subsLinear = (LinearLayout) this.findViewById(R.id.subjects_linear);
        setSubjectsTexts(subsLinear);
        periodsLinear = (LinearLayout)this.findViewById(R.id.class_linear);
        headingDay = (TextView) findViewById(R.id.headingDayEdit);

        setListCols();
        setListeners();
    }

    public static int pxToDp(int px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int dp = px / (metrics.densityDpi / 160);
        return Math.round(dp);
    }

    public static int dpToPx(int dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int px = dp * (metrics.densityDpi / 160);
        return Math.round(px);
    }

    View.OnDragListener dropListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch(dragEvent){
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:

                    ScrollView myScrollView = (ScrollView) findViewById(R.id.myScrollView);
                    int y = Math.round(v.getY())+Math.round(event.getY());
                    int mScrollDistance = myScrollView.getScrollY();
                    int translatedY = y - mScrollDistance;
                    int threshold = dpToPx(50) ;
                    // make a scrolling up due the y has passed the threshold
                    if (translatedY < dpToPx(-100)) {
                        // make a scroll up by 30 px
                        myScrollView.smoothScrollBy(0, -1*dpToPx(30));
                    }
                    // make a auto scrolling down due y has passed the 500 px border
                    if (translatedY + threshold > dpToPx(600)) {
                        // make a scroll down by 30 px
                        myScrollView.smoothScrollBy(0, dpToPx(30));
                    }

                    break;


                case DragEvent.ACTION_DROP:
                    TextView target = (TextView) v;
                    String dragged =((TextView) event.getLocalState()).getText().toString();
                    if(dragged.equals("NONE"))
                        dragged = "";
                    target.setText(dragged);
                    String id = Integer.toString(target.getId() - periodsView[0].getId() +1);
                    myDb.setEditedPeriod(curEditDay, id, dragged);
                    break;
            }
            /*

                    if (e.GetY () < 90) {
                        ScrollCalendar.StartScroll (-15);
                    } else if (e.GetY () > yourScrollView.Height - 90) {
                        ScrollCalendar.StartScroll (15);
                    } else
                        ScrollCalendar.StopScroll ();
            */

            return true;
        }
    };

    View.OnLongClickListener longListen = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("","");
            DragShadow dragShadow = new DragShadow(v);
            v.startDrag(data,dragShadow,v,0);
            return false;
        }
    };

    public void setListeners(){
        for (int i =0;i<subjectsView.length;i++){
            subjectsView[i].setOnLongClickListener(longListen);
        }
        for (int i =0;i<periodsView.length;i++){
            periodsView[i].setOnDragListener(dropListener);
        }
    }

    public void setListCols(){
        setClassesTexts(periodsLinear);
        setHeadingDay();
    }

    private void setHeadingDay(){
        for(int i =0;i<7;i++){
            if(curEditDay.equals(Table.daysSmall[i])) headingDay.setText(Table.daysFull[i]);
        }
    }

    public void setSubjectsTexts(LinearLayout linearLayout){
        String[] subs = myDb.getSubjects();
        subjectsView = new TextView[subs.length+3];
        int px47 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics());
        int px150 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,getResources().getDisplayMetrics());
        int px10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics());
        int px5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params;
        for( int i = 0; i < subs.length+3; i++ ){
            subjectsView[i] = new TextView(this);
            if(i<subs.length) subjectsView[i].setText(subs[i]);
            else if(i==subs.length) subjectsView[i].setText("LUNCH");
            else if(i==subs.length+1) subjectsView[i].setText("BREAK");
            else if(i==subs.length+2) subjectsView[i].setText("NONE");
            subjectsView[i].setWidth(px150);
            subjectsView[i].setTag("sub" + (i + 1));
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,px47);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, px5, 0, px5);
            subjectsView[i].setLayoutParams(params);
            subjectsView[i].setTextColor(getResources().getColor(R.color.color3));
            subjectsView[i].setPadding(px10, px10, px10, px10);
            subjectsView[i].setBackgroundColor(Color.WHITE);
            subjectsView[i].setTypeface(null, Typeface.BOLD_ITALIC);
            subjectsView[i].setGravity(Gravity.CENTER);
            subjectsView[i].setTextSize(22);
            if(!subjectsView[i].getText().equals(""))
                linearLayout.addView(subjectsView[i]);
        }
    }

    public void setClassesTexts(LinearLayout linearLayout){
        String[] periods = myDb.getPeriodsOf(curEditDay);
        periodsView = new TextView[9];
        periodsView[0]=(TextView)linearLayout.findViewById(R.id.class1);
        periodsView[1]=(TextView)linearLayout.findViewById(R.id.class2);
        periodsView[2]=(TextView)linearLayout.findViewById(R.id.class3);
        periodsView[3]=(TextView)linearLayout.findViewById(R.id.class4);
        periodsView[4]=(TextView)linearLayout.findViewById(R.id.class5);
        periodsView[5]=(TextView)linearLayout.findViewById(R.id.class6);
        periodsView[6]=(TextView)linearLayout.findViewById(R.id.class7);
        periodsView[7]=(TextView)linearLayout.findViewById(R.id.class8);
        periodsView[8]=(TextView)linearLayout.findViewById(R.id.class9);
        for(int i =0; i<periods.length-1;i++){
            if(!periods[i].equals(""))periodsView[i].setText(periods[i]);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.change_timings) {

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mon) {
            curEditDay="mon";
        } else if (id == R.id.nav_tue) {
            curEditDay="tue";
        } else if (id == R.id.nav_wed) {
            curEditDay="wed";
        } else if (id == R.id.nav_thu) {
            curEditDay="thu";
        } else if (id == R.id.nav_fri) {
            curEditDay="fri";
        } else if (id == R.id.nav_sat) {
            curEditDay="sat";
        }

        setListCols();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class DragShadow extends View.DragShadowBuilder{
        ColorDrawable greyBox;
        public DragShadow(View v){
            super(v);
            greyBox = new ColorDrawable(Color.WHITE);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            View v = getView();
            int height = v.getHeight();
            int width = v.getWidth();
            greyBox.setBounds(0,0,width,height);
            shadowSize.set(width + 1, height + 1);
            shadowTouchPoint.set((int)width/2,(int)height/2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            greyBox.draw(canvas);
        }
    }
}