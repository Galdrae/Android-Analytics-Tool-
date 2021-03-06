package com.example.deosfriend.design;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import Controller.Child;
import Controller.Message;
import database.DBAdapter;
import database.IntervalDBAdapter;
import database.NewGradingDB;
import database.SessionDBAdapter;


public class MainActivity extends ActionBarActivity {

    Button create, view;
    TextView inName, inPriDi, inSecDi, inRemarks, inInspector, inNoAdults, inNoChildren, testTimer;
    RadioButton male, female;
    CheckBox cbSession1, cbSession2, cbSession3, cbSession4;

    Spinner spinnerDDVenue;
    Spinner spinnerDDActivity;

    String[] venueSpinner = {
            "[ Select Venue ]",
            "Ngee Ann Child Centre",
            "Bedok North Care Centre",
            "Eng Neo Child Centre",
            "Alekandra Hospital",
            "Stamford Junior school",

    };

    String[] activitySpinner = {
            "[ Select Activty ]",
            "Hearing Test",
            "Interaction Screening",
            "Sensory-motor Evaluation",
            "Cognitive Evaluation Test",
            "Interest Screening Test",
            "Adaptive Function Assessment",
            "Adaptive Behaviour Screening Test",

    };

    DBAdapter myDB;
    SessionDBAdapter mySessionDB;
    IntervalDBAdapter myIntervalDB;
    NewGradingDB myNewGradingDB;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myNewGradingDB = new NewGradingDB(getApplicationContext());

        spinnerDDVenue = (Spinner) findViewById(R.id.spinnerVenue);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_dropdown_item, venueSpinner);
        spinnerDDVenue.setAdapter(adapter);
        spinnerDDVenue.setPrompt("Select your favorite Planet!");

        // for activity
        spinnerDDActivity = (Spinner) findViewById(R.id.spinnerActivity);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_dropdown_item, activitySpinner);

        spinnerDDActivity.setAdapter(adapter1);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ListView_Database.class);
                        MainActivity.this.startActivity(intent);
                    }
                }
        );

        create = (Button) findViewById(R.id.btnCreate);
        view = (Button) findViewById(R.id.btnView);

        openDB();
        openIntervalDB();
        openSessionDB();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void openIntervalDB() {
        myIntervalDB = new IntervalDBAdapter(this);
        myIntervalDB.open();
    }

    public void closeIntervalDB() {
        myIntervalDB.close();
    }

    private void openSessionDB() {
        mySessionDB = new SessionDBAdapter(this);
        mySessionDB.open();
    }

    public void closeDB() {
        myDB.close();
    }

    // Create record
    // =================================================
    public void onClick_Create(View v) {


        inName = (TextView) findViewById(R.id.tbName);
        inPriDi = (TextView) findViewById(R.id.tbPriDi);
        inSecDi = (TextView) findViewById(R.id.tbSecDi);
        inRemarks = (TextView) findViewById(R.id.tbRemarks);
        inInspector = (TextView) findViewById(R.id.tbInspector);
        inNoAdults = (TextView) findViewById(R.id.tbNoAdults);
        inNoChildren = (TextView) findViewById(R.id.tbNoChildren);
        female = (RadioButton) findViewById(R.id.rbFemale);
        male = (RadioButton) findViewById(R.id.rbMale);
        cbSession1 = (CheckBox) findViewById(R.id.cbSession1);
        cbSession2 = (CheckBox) findViewById(R.id.cbSession2);
        cbSession3 = (CheckBox) findViewById(R.id.cbSession3);
        cbSession4 = (CheckBox) findViewById(R.id.cbSession4);

        String name = inName.getText().toString();
        String priDi = inPriDi.getText().toString();
        String secDi = inSecDi.getText().toString();
        String remarks = inRemarks.getText().toString();
        String inspector = inInspector.getText().toString();
        String noOfAdults = inNoAdults.getText().toString();
        String noOfChildren = inNoChildren.getText().toString();

        String gender = "";
        String errorMsg = "";

        // Validations
        if (spinnerDDVenue.getSelectedItem() == "[ Select Venue ]" )
        { errorMsg = "Select a venue"; Message.message(this, errorMsg); }
        if (spinnerDDActivity.getSelectedItem() == "[ Select Activty ]" )
        { errorMsg = "Select a venue"; Message.message(this, errorMsg); }

        // gender
        if (male.isChecked())
            gender = male.getText().toString();
        if (female.isChecked())
            gender = female.getText().toString();

        // textfields
        if ( name.isEmpty() || priDi.isEmpty() || secDi.isEmpty() || remarks.isEmpty() || inspector.isEmpty() || noOfAdults.isEmpty() || noOfChildren.isEmpty()) {
            errorMsg = "Please fill in fields";
            Message.message(this, errorMsg);
        }
        if (female.isChecked() == false && male.isChecked() == false) {
            errorMsg = "Select gender";
            Message.message(this, errorMsg);
        }
        // Checkboxes
        if ( cbSession1.isChecked() == false && cbSession2.isChecked() == false && cbSession3.isChecked() == false && cbSession4.isChecked() == false){
            errorMsg = "Select the sessions to be added";
            Message.message(this, errorMsg);
        }
        //===================================================================

        // if all fields are filled
        if (errorMsg == "" && female.isChecked() && cbSession1.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", 1);
/*            Message.message(this, "Female Child added");
            displayRecordSet(cursor);*/
            //***** Export
            String Session = "1";
            Child newChild = new Child(name, gender, secDi, priDi ,remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", Session);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if (errorMsg == "" && male.isChecked() && cbSession1.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", 1);
            //***** Export
            String SessionNumber = "1";
            Child newChild = new Child(name, gender, secDi, priDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", SessionNumber);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if (errorMsg == "" && female.isChecked() && cbSession2.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", 2);
            //***** Export
            String SessionNumber = "2";
            Child newChild = new Child(name, gender, secDi, priDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", SessionNumber);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if (errorMsg == "" && male.isChecked() && cbSession2.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", 2);
            //***** Export
            String SessionNumber = "2";
            Child newChild = new Child(name, gender, secDi, priDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", SessionNumber);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if (errorMsg == "" && female.isChecked() && cbSession3.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", 3);
            //***** Export
            String SessionNumber = "3";
            Child newChild = new Child(name, gender, secDi, priDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", SessionNumber);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if (errorMsg == "" && male.isChecked() && cbSession3.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", 3);
            //***** Export
            String SessionNumber = "3";
            Child newChild = new Child(name, gender, secDi, priDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", SessionNumber);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if (errorMsg == "" && female.isChecked() && cbSession4.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", 4);
            //***** Export
            String SessionNumber = "4";
            Child newChild = new Child(name, gender, secDi, priDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.female_user, "Not observed", SessionNumber);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if (errorMsg == "" && male.isChecked() && cbSession4.isChecked()) {
            long newID = myDB.insertRow(name, gender, priDi, secDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", 4);
            //***** Export
            String SessionNumber = "4";
            Child newChild = new Child(name, gender, secDi, priDi, remarks, inspector,
                    spinnerDDVenue.getSelectedItem().toString(), spinnerDDActivity.getSelectedItem().toString(),
                    noOfAdults, noOfChildren, R.drawable.male_user, "Not observed", SessionNumber);
            int status = myNewGradingDB.addPersonDataChild(newChild);
            if (status == 1){
                Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        }
        if ( errorMsg == "" ) {
            Intent intent = new Intent(MainActivity.this, ListView_Database.class);
            MainActivity.this.startActivity(intent);
        }
    }

    // View Database
    // =================================================
    public void onClick_View(View v) {
        //    Message.message(this, "View clicked");
        //  Cursor cursor = myDB.getAllRowsIncomplete("Not observed");
        //  displayRecordSet(cursor);
    }

    // Wipe Database
    // =================================================
    public void onClick_Delete(View v) {
        //    Message.message(this, "Delete clicked");
        myDB.deleteAll();
        myNewGradingDB.deleteAllPerson();
        myNewGradingDB.deleteAllPersonChild();
        myNewGradingDB.deleteAllPersonInterval();
        myNewGradingDB.deleteAllPersonSession();
        myIntervalDB.deleteAll();
        mySessionDB.deleteAll();
        Toast.makeText(this, "Data delete successfully", Toast.LENGTH_LONG).show();
    }

    // Go to View List
    // =================================================
    public void onClick_List(View v) {
        Intent intent = new Intent(MainActivity.this, ListView_Database.class);
        MainActivity.this.startActivity(intent);
    }

    // Radio Buttons
    // =================================================
    public void onClick_rbMale(View v) {
        female = (RadioButton) findViewById(R.id.rbFemale);
        female.setChecked(false);
    }

    public void onClick_rbFemale(View v) {
        male = (RadioButton) findViewById(R.id.rbMale);
        male.setChecked(false);
    }

    // Retreive Database
    private void displayRecordSet(Cursor cursor) {

        String message = "";

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String age = cursor.getString(2);
                String gender = cursor.getString(3);
                String image = cursor.getString(4);

                message += "ID: " + id
                        + ", Name: " + name
                        + ", Age: " + age
                        + ", Gender: " + gender
                        + ", Image is: " + image
                        + "\n";
            } while (cursor.moveToNext());
        }

        cursor.close();
        Message.message(this, message);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
