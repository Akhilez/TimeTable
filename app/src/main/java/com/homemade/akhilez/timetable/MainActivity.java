package com.homemade.akhilez.timetable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.homemade.akhilez.timetable.editingActivities.ChooseTimeTable;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
//    private ViewPager mViewPager;

    OpenDBHelper myDb;
    public AssetsDBHelper assetsDBHelper;


    private Toolbar toolbar;
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;

    private static FloatingActionButton fab;
    private static int curDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assetsDBHelper = new AssetsDBHelper(this);
        myDb = new OpenDBHelper(this);

        if(myDb.isTableEmpty(TableContract.TimeTable.TABLE_NAME)) {
            myDb.setPreset(assetsDBHelper, "initial");
            Intent intent = new Intent(MainActivity.this, ChooseTimeTable.class);
            startActivity(intent);
            //finish();
        }

        //INITIALIZING 'TABLE' VARIABLES BASED ON DATABASE
        Table.subjects =  myDb.getSubjects();
        //Table.initializeSubjects(Table.subjects);
        myDb.initializeDays();
        //Table.faculty = myDb.getFaculty();
        Table.pTimes = myDb.getTimes();

//=================================================================
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout = (SmartTabLayout) findViewById(R.id.tab_layout);
        //tabLayout.setupWithViewPager(viewPager);
        tabLayout.setViewPager(viewPager);


//==================================================================

        SharedPreferences sharedPref = getSharedPreferences("common", Context.MODE_PRIVATE);
        String pn = sharedPref.getString("preset","noPreset");
        if(!pn.equals("noPreset")) {
            TextView secText = (TextView)toolbar.findViewById(R.id.toolbar_section);
            String secStr = sharedPref.getString("section","noSection");
            String setterText;
            if (!secStr.equals("-"))
                setterText = sharedPref.getString("branch","noBranch")+"-"+secStr;
            else
                setterText = sharedPref.getString("branch","noBranch");
            secText.setText(setterText);
        }

        //tabLayout.setSelectedTabIndicatorHeight(10);

        Table.dayString=Table.getDay();
        //Table.dayString="Sun";
        curDay=Table.getDayInt(Table.dayString)-1;
        //curDay=1;

        Table.goToPresent(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();*/
                Table.goToPresent(viewPager);

                //NOTIFICATION (working)
                /*
                Intent intent = new Intent();
                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0);
                Notification notif = new Notification.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("Ticker Title")
                        .setContentTitle("Title")
                        .setContentText("Content Text")
                        .setContentIntent(pIntent).getNotification();
                notif.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notifManager.notify(0,notif);
                */


            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                /*Toast toast = Toast.makeText(getApplicationContext(), "page scrolled", Toast.LENGTH_SHORT);
                toast.show();*/
                int curDayFab = curDay;
                int curTime = Integer.parseInt(Table.getTime());
                curTime = Table.set24hr(Table.amOrPm(), curTime);
                if (curTime > 1615) curDayFab = (curDayFab + 1) % 6;
                if (Table.getDay().equals("Sun")) curDayFab = 0;
                //if (tabLayout.getSelectedTabPosition() == curDayFab) {
                //int position = FragmentPagerItem.getPosition(getArguments());
                int position = viewPager.getCurrentItem();
                if ( position == curDayFab) {
                    fab.hide();
                } else fab.show();

                tabColors(tabLayout);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            public void tabColors(SmartTabLayout tabLayout) {
                //String[] background = {"#5F4484","#3F51B5","#239A54","#ff8800","#FF4444","#D41F6F"};

                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                int pos = viewPager.getCurrentItem();

                for (int i = 0; i < 6; i++) {
                    if (pos == i) {
                        tabLayout.setBackgroundColor(Color.parseColor(Table.colors[i]));
                        toolbar.setBackgroundColor(Color.parseColor(Table.colors[i]));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.setStatusBarColor(Color.parseColor(Table.colors[i]));
                            window.setNavigationBarColor(Color.parseColor(Table.colors[i]));
                        }
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Table.colors[5 - i])));
                        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor(Table.colors[5 - i]));

                        SmartTabLayout.TabColorizer tabColorizer = new SmartTabLayout.TabColorizer() {
                            @Override
                            public int getIndicatorColor(int position) {
                                return Color.parseColor(Table.colors[5-position]);
                            }

                            @Override
                            public int getDividerColor(int position) {
                                return 0;//Color.parseColor(Table.colors[5 - position]);
                            }
                        };
                        tabLayout.setCustomTabColorizer(tabColorizer);


                    }
                }
            }
        });


        //=====================notifications2==================
        /*
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver1.class); // AlarmReceiver1 = broadcast receiver

        PendingIntent pendingIntent = PendingIntent.getBroadcast(  MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 30);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            Log.d("Hey","Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d("Alarm", "Alarms set for everyday 8 30 am.");

        //=======================================================^
        */


        //hide actionbar on scroll

        //showNotification();
        //setAlarm(viewPager);
    }

    /* THIS IS METHOD TO PUSH NOTIFICATION
    public void showNotification(View view){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Notification 1")
                .setContentText("Notification message")
                .setTicker("TimeTable");
        Intent moreInfoIntent = new Intent(this,MainActivity.class);

        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);

        tStackBuilder.addParentStack(MainActivity.class);
        tStackBuilder.addNextIntent(moreInfoIntent);

        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(99,notificationBuilder.build());


    }
    */

    //METHOD TO SET NOTIFICATION ALARM
    /*
    public void setAlarm(View view){
        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;
        Intent alertIntent = new Intent(this,AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,
                PendingIntent.getBroadcast(this,1,alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
    */

    //=======================================================================\/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MondayFragment(), "Monday");
        adapter.addFrag(new TuesdayFragment(), "Tuesday");
        adapter.addFrag(new WednesdayFragment(), "Wednesday");
        adapter.addFrag(new ThursdayFragment(), "Thursday");
        adapter.addFrag(new FridayFragment(), "Friday");
        adapter.addFrag(new SaturdayFragment(), "Saturday");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //=======================================================================^


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem timerItem = menu.findItem(R.id.break_timer);
        MenuItem titleNext = menu.findItem(R.id.menuNext);
        TextView timerText = (TextView) MenuItemCompat.getActionView(timerItem);
        TextView nextText = (TextView) MenuItemCompat.getActionView(titleNext);
        //timerText.setPadding(0, 0, 10, 0); //Or something like that...
        timerText.setTypeface(Typeface.DEFAULT_BOLD);
        timerText.setTextColor(Color.WHITE);
        long nextTime=Table.getNextTime();
        int time24 = Table.getTime24hr();
        if ( (Table.getDay().equals("Sun")&&Table.getTime24hr()<myDb.getEndTime()) || ( Table.getDay().equals("Sat") && Table.getTime24hr()>myDb.getEndTime() ) ) {
            nextText.setText("");
        }else if(time24>myDb.getEndTime() && time24<myDb.getStartTime() ){
            Timer.startTimer(nextTime - 1001, 10, timerText);
            nextText.setText("next : ");
        }

        /*MenuItem secItem = menu.findItem(R.id.toolbar_section);
        TextView secText = (TextView) MenuItemCompat.getActionView(secItem);
        SharedPreferences sharedPref = getSharedPreferences("common", Context.MODE_PRIVATE);
        String pn = sharedPref.getString("preset","noPreset");
        if(!pn.equals("noPreset")){
            secText.setText(assetsDBHelper.getSection(pn));
            secText.setTextSize(15);
            secText.setTextColor(Color.WHITE);
            secText.setTypeface(null,Typeface.ITALIC);
            secText.setPadding(0,0,15,0);
        }*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.impNotes){
            Intent intent = new Intent(MainActivity.this, ImportantNotes.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.settings_menu){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
