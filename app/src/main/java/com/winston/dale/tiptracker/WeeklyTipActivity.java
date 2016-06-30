package com.winston.dale.tiptracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class WeeklyTipActivity extends AppCompatActivity {

    private ListView listView;
    private TipAdapter tipAdapter;
    private ImageView trashIcon;
    private int whichWeek;
    private TextView headerText;
    private DecimalFormat decimalFormat;
    private Button viewPaycheckBtn;

    private ArrayList<TipEntry> tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_tip2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddTipActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init() {
        Intent thisIntent = getIntent();
        if (thisIntent != null && thisIntent.getExtras() != null && thisIntent.getExtras().containsKey("whichWeek")) {
            whichWeek = thisIntent.getExtras().getInt("whichWeek");
        }
        decimalFormat = new DecimalFormat("#,###,##0.00");
        setListView();
        setPaycheckBtn();
    }

    public void setPaycheckBtn() {
        viewPaycheckBtn = (Button) findViewById(R.id.view_paycheck_btn);
        viewPaycheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeeklyTipActivity.this, ShowPaycheckActivity.class);
                intent.putExtra("weekInt", whichWeek);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tips = TipEntry.getTipsByWeek(whichWeek, getApplicationContext());
        double totalTips = getTotalTips(tips);
        headerText.setText("Total tips: $" + decimalFormat.format(totalTips));
        tipAdapter.clear();
        tipAdapter.addAll(tips);
        tipAdapter.notifyDataSetChanged();
    }

    public void setListView() {
        tips = TipEntry.getTipsByWeek(whichWeek, getApplicationContext());
        listView = (ListView) findViewById(R.id.list_view);

        tipAdapter = new TipAdapter(this, R.layout.listview_tip_rows, tips);

        View header = (View) getLayoutInflater().inflate(R.layout.listview_tip_header, null);
        listView.addHeaderView(header);
        headerText = (TextView) findViewById(R.id.weekly_header);
        double totalTips = getTotalTips(tips);
        headerText.setText("Total tips: $" + decimalFormat.format(totalTips));
        listView.setAdapter(tipAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WeeklyTipActivity.this, AddTipActivity.class);

                TipEntry tipEntry = tips.get(position - 1);
                intent.putExtra("dateString", tipEntry.getDate());
                intent.putExtra("shiftString", tipEntry.getShift());
                intent.putExtra("amountDouble", tipEntry.getAmount());
                intent.putExtra("idInt", tipEntry.getId());
                intent.putExtra("weekInt", tipEntry.getWeek());
                startActivity(intent);
            }
        });
    }

    public double getTotalTips(ArrayList<TipEntry> tips) {
        double totalTips = 0;
        for (int i = 0; i < tips.size(); i++) {
            totalTips += tips.get(i).getAmount();
        }
        return totalTips;
    }

}
