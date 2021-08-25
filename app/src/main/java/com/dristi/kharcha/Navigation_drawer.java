package com.dristi.kharcha;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Navigation_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TimePicker timePicker;

    private ViewPager viewPager;

    private TextView ok,cancel;

    private SharedPreferences preferences, execute;

    private DatabaseHelper databaseHelper;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = this.getSharedPreferences("Reminder", 0);
        execute = this.getSharedPreferences("Execute", 0);

        databaseHelper = new DatabaseHelper(this);

        if(preferences.getInt("hour", 30) == 30){
            alarmManager();
        }else{
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Date dat = new Date();
            Calendar cal_alarm = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();
            cal_now.setTime(dat);
            cal_alarm.setTime(dat);
            cal_alarm.set(Calendar.HOUR_OF_DAY,preferences.getInt("hour", 0));
            cal_alarm.set(Calendar.MINUTE,preferences.getInt("minute", 0));
            if(cal_alarm.before(cal_now)){
                cal_alarm.add(Calendar.DATE,1);
            }

            Intent myIntent = new Intent(Navigation_drawer.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(Navigation_drawer.this, 0, myIntent, 0);

            manager.setRepeating(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),1000 * 60 * 60 * 24, pendingIntent);
        }

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.containermain);

        viewPager.setAdapter(new Navigation_drawer.mainpageadapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((mainpageadapter)viewPager.getAdapter()).getFragment(position);

                if (position == 1 && fragment != null)
                {
                    fragment.onResume();
                }

                if (position == 0 && fragment != null)
                {
                    fragment.onResume();
                }

                if (position == 2 && fragment != null)
                {
                    fragment.onResume();
                }

                navigation.getMenu().getItem(position).setChecked(true);
                menuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.accounts:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.records:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.charts:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.lendborrow){
            Intent intent = new Intent(Navigation_drawer.this, Lend_Borrow.class);
            intent.putExtra("id", 0);
            startActivity(intent);
        }
        else if(id == R.id.budget){
            startActivity(new Intent(Navigation_drawer.this, Budgets.class));
        }
        else if(id == R.id.reminder){
            showCustomdialog();
        }
        else if (id == R.id.payments){
            startActivity(new Intent(Navigation_drawer.this,Payments.class));
        }
        else if (id == R.id.nav_share){
            Intent myintent = new Intent(Intent.ACTION_SEND);
            myintent.setType("text/plain");
            String sharesub = "Kharcha";
            String sharebody = "Download this app to manage your money efficiently";
            myintent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
            myintent.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(myintent,"Share using"));
        }
        else if (id == R.id.about){
            startActivity(new Intent(Navigation_drawer.this, About.class));
        }
        else if (id == R.id.feedback){
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"amatyaichhya@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
            intent.putExtra(Intent.EXTRA_TEXT,"");
            intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class mainpageadapter extends FragmentPagerAdapter
    {
        private FragmentManager mfm;
        private Map<Integer,String> mFragmentTags;

        public mainpageadapter(FragmentManager fm) {
            super(fm);
            mfm = fm;
            mFragmentTags = new HashMap<Integer,String>();
        }

        @Override
        public Fragment getItem(int position){
            if(position == 0){
                return new Accounts();
            }
            if(position == 1){
                return new Records();
            }
            return new ChartFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mfm.findFragmentByTag(tag);
        }

    }

    public void showCustomdialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Set a Reminder");

        View view = LayoutInflater.from(this).inflate(R.layout.activity_reminder,null);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);

        timePicker.setIs24HourView(false); // used to display AM/PM mode

        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                preferences.edit().putInt("hour", timePicker.getHour()).apply();
                preferences.edit().putInt("minute", timePicker.getMinute()).apply();

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

    }

    public void alarmManager(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY,18);
        cal_alarm.set(Calendar.MINUTE,30);
        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }

        Intent myIntent = new Intent(Navigation_drawer.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Navigation_drawer.this, 0, myIntent, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
