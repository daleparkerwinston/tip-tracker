package com.winston.dale.tiptracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Menu;

import java.util.Calendar;

public class AddTipActivity extends AppCompatActivity {

    EditText dateText;
    EditText amountText;
    Spinner shiftSpinner;
    Button saveButton;
    Button deleteButton;
    int week;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu_add_tip, menu);
        return true;
    }

    // The action bar choices
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                showDiscardDialog();
                // Add Dialog to this to confirm leaving add activity and loosing data
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_calculator:
                Intent i = new Intent();
                i.setClassName("com.android.calculator2",
                        "com.android.calculator2.Calculator");
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        dateText = (EditText) findViewById(R.id.editText_date);
        dateText.setInputType(0x00000000); // Stops keyboard from being shown
        amountText = (EditText) findViewById(R.id.editText_amount);
        saveButton = (Button) findViewById(R.id.button_save);
        deleteButton = (Button) findViewById(R.id.button_delete);
        // Get intent and set fields

        id = -1;


        // Set the week to the proper week
        week = 0;
        setButtonListeners();
        populateShiftSpinner();

        Intent thisIntent = getIntent();
        if (thisIntent != null && thisIntent.getExtras() != null && thisIntent.getExtras().containsKey("dateString")) {
            dateText.setText(thisIntent.getExtras().getString("dateString"));
            amountText.setText(Double.toString(thisIntent.getExtras().getDouble("amountDouble")));

            if (thisIntent.getExtras().getString("shiftString").equals("Brunch")) {
                shiftSpinner.setSelection(1);
            }
            week = thisIntent.getExtras().getInt("weekInt");
            id = thisIntent.getExtras().getInt("idInt");
        }
    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateText.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(monthOfYear + 1).append("/").append(dayOfMonth).append("/")
                        .append(year));

                Calendar setCalendar = Calendar.getInstance();
                setCalendar.set(Calendar.YEAR, year);
                setCalendar.set(Calendar.MONTH, monthOfYear);
                setCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                week = setCalendar.get(Calendar.WEEK_OF_YEAR);

            }

        };
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void populateShiftSpinner() {
        shiftSpinner = (Spinner) findViewById(R.id.spinner_shift);
        // Create array adapter using shifts_array in strings.xml
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.shifts_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shiftSpinner.setAdapter(arrayAdapter);
    }

    private void showDiscardDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Discard Changes?").setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Delete Tip Entry?").setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TipEntry.deleteTip(getApplicationContext(), id);
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setButtonListeners() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id > -1) {
                    showDeleteDialog();
                } else {
                    showDiscardDialog();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = dateText.getText().toString();
                double amount = Double.parseDouble(amountText.getText().toString());
                String shift = shiftSpinner.getSelectedItem().toString();

                if (id > -1) { // id has changed, tipentry already exists
                    TipEntry tipEntry = new TipEntry(date, amount, shift, week, id);
                    TipEntry.addTipEntry(tipEntry, getApplicationContext(), true);
                } else {
                    TipEntry tipEntry = new TipEntry(date, amount, shift, week);
                    TipEntry.addTipEntry(tipEntry, getApplicationContext(), false);
                }


                finish();
            }
        });
    }




    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
