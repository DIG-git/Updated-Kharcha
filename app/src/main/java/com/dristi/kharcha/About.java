package com.dristi.kharcha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class About extends AppCompatActivity {

    private WebView about;

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        about = findViewById(R.id.about);

        text = "<html><body><p align=\"justify\">";
        text+= "Kharcha: Expense Tracking System is an application which provides a way for organizing, pre-planning, managing, predicting and tracking the daily income\n" +
                " and expense so you can effectively keep record of the expenses\n" +
                " for efficient use of their income or monthly allowance. It enables you to not just keep the tab on the expenses but also to plan ahead keeping the past\n" +
                " budget in mind. It also allows the users to keep track of the lend-borrow records and payments.\n" +
                " Also, it provides you with a pre-planning system to plan your\n" +
                " weekly, monthly records or plan your trips, events and tours, providing a range of\n" +
                " time for the system to monitor the planned budget and the real-time expenses made\n" +
                " by you.";
        text+= "</p></body></html>";

        about.loadData(text, "text/html", "utf-8");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}