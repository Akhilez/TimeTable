package com.homemade.akhilez.timetable.editingActivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.homemade.akhilez.timetable.AssetsDBHelper;
import com.homemade.akhilez.timetable.OpenDBHelper;
import com.homemade.akhilez.timetable.R;

public class ChooseTimeTable extends AppCompatActivity {

    public static String presetName = "initial";
    public static String DB_PATH ;  //Environment.getExternalStorageDirectory().getPath();//"/data/data/com.example.akhilez.timetable/databases/";
    public static String DB_NAME = "appInternalDatabase.db";
    public AssetsDBHelper assetsDBHelper;
    public OpenDBHelper openDBHelper;
    public Context context;
    public Spinner spinnerCollege, spinnerBranch, spinnerSection, spinnerYear;
    public Button presetBtn,update4Btn,newPresetBtn;
    public static String selectedCollege,selectedBranch,selectedSection,selectedYear;
    public TextView yearText,branchText,sectionText;
    public EditText secE,branchE,yearE,collegeE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assetsDBHelper = new AssetsDBHelper(this);
        openDBHelper = new OpenDBHelper(this);


        spinnerCollege = (Spinner) findViewById(R.id.spinnerCollege);
        spinnerBranch = (Spinner) findViewById(R.id.spinnerBranch);
        spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);

        presetBtn = (Button) findViewById(R.id.setTimeTable);
        presetBtn.setVisibility(View.GONE);


        setterPresetName();


    }

    public void setterPresetName(){
        //get the preset name from layout
        String[] colleges = assetsDBHelper.getColleges();//new String[] { "Chai Latte", "Green Tea", "Black Tea" };
        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, colleges);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCollege.setAdapter(collegeAdapter);
        spinnerCollege.setSelection(0);
        spinnerCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCollege = (String) parent.getItemAtPosition(position);
                if (selectedCollege.equals("--select--")) {
                    spinnerBranch.setAdapter(null);
                    spinnerSection.setAdapter(null);
                    spinnerYear.setAdapter(null);
                    presetBtn.setVisibility(View.GONE);
                }else {
                    setSpinnerBranch();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void setSpinnerBranch(){
        String[] branches = assetsDBHelper.getBranches(selectedCollege);//new String[] { "Chai Latte", "Green Tea", "Black Tea" };
        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branches);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(collegeAdapter);
        spinnerBranch.setSelection(0);
        spinnerBranch.performClick();
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedBranch = (String) parent.getItemAtPosition(position);
                if(!selectedBranch.equals("--select--")) {
                    setSpinnerSection();
                }
                else{
                    spinnerSection.setAdapter(null);
                    spinnerYear.setAdapter(null);
                    presetBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void setSpinnerSection(){
        String[] branches = assetsDBHelper.getSections(selectedCollege, selectedBranch);//new String[] { "Chai Latte", "Green Tea", "Black Tea" };
        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branches);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSection.setAdapter(collegeAdapter);
        spinnerSection.setSelection(0);
        spinnerSection.performClick();
        spinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedSection = (String) parent.getItemAtPosition(position);
                if(!selectedSection.equals("--select--")) {
                    setSpinnerYear();
                }
                else{
                    spinnerYear.setAdapter(null);
                    presetBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void setSpinnerYear(){
        String[] branches = assetsDBHelper.getYears(selectedCollege, selectedBranch, selectedSection);//new String[] { "Chai Latte", "Green Tea", "Black Tea" };
        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branches);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(collegeAdapter);
        spinnerYear.setSelection(0);
        spinnerYear.performClick();
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedYear = (String) parent.getItemAtPosition(position);
                presetName = assetsDBHelper.getPresetFromTable(selectedCollege, selectedBranch, selectedSection, selectedYear);
                if(!selectedYear.equals("--select--")) {
                    presetBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void update4Things(View v){
        SharedPreferences sharedPref = getSharedPreferences("common", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("section",secE.getText().toString());
        editor.putString("branch",branchE.getText().toString());
        editor.putString("year",yearE.getText().toString());
        editor.putString("college",collegeE.getText().toString());
        editor.apply();
        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();
    }

    public void setPreset(View v){
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("common", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        openDBHelper.setPreset(assetsDBHelper,presetName);
        editor.putString("preset", presetName);
        String[] details = assetsDBHelper.getTableDetails(presetName);
        editor.putString("section", details[0]);
        editor.putString("branch", details[1]);
        editor.putString("year", details[2]);
        editor.putString("college", details[3]);
        editor.apply();

        Toast.makeText(getApplicationContext(), "Timetable has been set", Toast.LENGTH_LONG).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
