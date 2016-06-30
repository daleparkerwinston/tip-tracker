package com.winston.dale.tiptracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Week> arrayAdapter;
    private ListView listView;
    private TextView headerText;
    private int weekOfYear;
    private int year;
    private ArrayList<Week> weeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Start the Add Tip Activity
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddTipActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {

        // Create a list of weeks for the current year
        Calendar currentCalender = Calendar.getInstance();
        weekOfYear = currentCalender.get(Calendar.WEEK_OF_YEAR);
        year = currentCalender.get(Calendar.YEAR);


//        ArrayList<String> weeks = new ArrayList<>();
//        for (int i = 0; i < weekOfYear; i++) {
//            weeks.add(i, "Week " + (i+1));
//        }

        weeks = populateWeeks();


        //  Show the most recent week first
        Collections.reverse(weeks);

        // Attach the list to list view
        listView = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new WeekAdapter(this, R.layout.listview_week_row, weeks);

        View header = (View) getLayoutInflater().inflate(R.layout.listview_week_header, null);
        listView.addHeaderView(header);
        headerText = (TextView) findViewById(R.id.listview_week_header);
        headerText.setText("Total tips YTD: " + TipEntry.getYearTips(weekOfYear, this));

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WeeklyTipActivity.class);

                // Weeks are in reverse order in the list
                // So we position 0 will be the most current (Highest number) week
                int whichWeek = parent.getCount() - position;
                Log.i("whichWeek", whichWeek + " is the week");
                intent.putExtra("whichWeek", whichWeek);
                startActivity(intent);
            }
        });
    }


    public String getWeekDates(int whichWeek, int whichYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, whichWeek);
        calendar.set(Calendar.YEAR, whichYear);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.DATE, 6);
        Date endDate = calendar.getTime();
        String endStr = formatter.format(endDate);
        return endStr;
    }

    @Override
    protected void onResume() {
        super.onResume();
        headerText.setText("Total tips YTD: " + TipEntry.getYearTips(weekOfYear, this));
        arrayAdapter.clear();
        weeks = populateWeeks();
        Collections.reverse(weeks);
        arrayAdapter.addAll(weeks);

        arrayAdapter.notifyDataSetChanged();
    }

    private ArrayList<Week> populateWeeks() {
        ArrayList<Week> weekList = new ArrayList<>();
        for (int i = 0; i < weekOfYear; i++) {
            Week week = new Week("Week Ending: " + getWeekDates(i + 1, year), "Total Tips: " + TipEntry.getWeeklyTips(i + 1, this));
            weekList.add(week);
        }
        return weekList;
    }

}
