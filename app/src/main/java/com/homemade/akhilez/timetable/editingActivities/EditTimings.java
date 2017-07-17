package com.homemade.akhilez.timetable.editingActivities;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.homemade.akhilez.timetable.OpenDBHelper;
import com.homemade.akhilez.timetable.R;

public class EditTimings extends AppCompatActivity {
    ListView subjectsList;
    OpenDBHelper openDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectsList = (ListView) findViewById(R.id.listView_subjects);
        openDBHelper = new OpenDBHelper(this);

        final Button okButton = (Button)findViewById(R.id.dialog_ok);

        String[] subjects = openDBHelper.getSubjects();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, subjects);
        subjectsList.setAdapter(adapter);
        subjectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {
                int itemPosition     = position;
                String  itemValue    = (String) subjectsList.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                FireMissilesDialogFragment newFragment = new FireMissilesDialogFragment();
                newFragment.show(getFragmentManager(), "missiles");

            }
        });

        final EditText subEdit = (EditText) findViewById(R.id.dialog_subject);
        //subEdit.setText(itemValue);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }
}



