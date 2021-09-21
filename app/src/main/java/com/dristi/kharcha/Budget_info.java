package com.dristi.kharcha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Budget_info extends AppCompatActivity {

    private TextView category, currency, amount, left, status, date, currencyb, amountb;

    private ImageView imageView;

    private ProgressBar progressBar;

    private DatabaseHelper dbhelper;

    private SharedPreferences preferences, detail_id;

    private TextView list, chart;

    private ViewPager pager;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        id = getIntent().getIntExtra("id",0);

        dbhelper = new DatabaseHelper(this);

        category = findViewById(R.id.category);
        currency = findViewById(R.id.currency);
        amount = findViewById(R.id.amount);
        left = findViewById(R.id.left);
        status = findViewById(R.id.status);
        date = findViewById(R.id.date);
        currencyb = findViewById(R.id.currencyb);
        amountb = findViewById(R.id.amountb);
        list = findViewById(R.id.list);
        chart = findViewById(R.id.chart);

        imageView = findViewById(R.id.image);

        progressBar = findViewById(R.id.myprogress);

        pager = findViewById(R.id.pager);

        preferences = Budget_info.this.getSharedPreferences("Currency",0);
        detail_id = Budget_info.this.getSharedPreferences("Detail_id",0);

        final BudgetInfo info = dbhelper.getbudgetdetail(id);

        detail_id.edit().putInt("id", info.id).commit();

        category.setText(info.category);
        currency.setText(preferences.getString("currency", "Rs.") + " ");
        currencyb.setText(preferences.getString("currency", "Rs.") + " ");
        date.setText(info.fromdate + "  -  " + info.todate);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String to_date = df.format(Calendar.getInstance().getTime());

        if(to_date.compareTo(info.todate) > 0){
            status.setText("Expired");
            status.setTextColor(Color.RED);
        }
        else{
            status.setText("Active");
            status.setTextColor(Color.parseColor("#FF42B511"));
        }

        progressBar.setMax(info.amount);

        int total = dbhelper.getBudgettotal(info.category, info.fromdate, info.todate);

        amountb.setText(String.valueOf(info.amount));

        if(total == 0){
            progressBar.setProgress(0);
            amount.setText(String.valueOf(info.amount));
        }

        progressBar.setProgress(total);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#03cd75"), PorterDuff.Mode.SRC_IN);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#03cd75"), PorterDuff.Mode.SRC_IN);

        amount.setText(String.valueOf(info.amount-total));

        if(total>info.amount){
            progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            left.setText("Overspent");
        }

        switch (info.category){
            case "Household":
                imageView.setImageResource(R.drawable.ic_household);
                break;
            case "Eating-out":
                imageView.setImageResource(R.drawable.ic_eating_out);
                break;
            case "Grocery":
                imageView.setImageResource(R.drawable.ic_grocery);
                break;
            case "Personal":
                imageView.setImageResource(R.drawable.ic_personal);
                break;
            case "Utilities":
                imageView.setImageResource(R.drawable.ic_utilities);
                break;
            case "Medical":
                imageView.setImageResource(R.drawable.ic_medical);
                break;
            case "Education":
                imageView.setImageResource(R.drawable.ic_education);
                break;
            case "Entertainment":
                imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Clothing":
                imageView.setImageResource(R.drawable.ic_clothing);
                break;
            case "Transportation":
                imageView.setImageResource(R.drawable.ic_transportation);
                break;
            case "Shopping":
                imageView.setImageResource(R.drawable.ic_shopping);
                break;
            case "Others":
                imageView.setImageResource(R.drawable.savings);
                break;
            case "Lend":
                imageView.setImageResource(R.drawable.ic_utilities);
                break;
            case "Borrow":
                imageView.setImageResource(R.drawable.ic_cash);
                break;
            default:
                break;
        }

        pager.setCurrentItem(0);
        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        list.setBackgroundColor(Color.parseColor("#FF6C6C6C"));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    list.setBackgroundColor(Color.parseColor("#FF6C6C6C"));
                    chart.setBackgroundColor(Color.parseColor("#FFCDCDCD"));
                }
                else{
                    list.setBackgroundColor(Color.parseColor("#FFCDCDCD"));
                    chart.setBackgroundColor(Color.parseColor("#FF6C6C6C"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    public class pagerAdapter extends FragmentPagerAdapter{

        public pagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
             if(position == 0){
                 return new BudgetListFragment();
             }
             return new BudgetChartFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}