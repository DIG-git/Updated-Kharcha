package com.dristi.kharcha;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Navigation_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TimePicker timePicker;

    ViewPager viewPager;

    TextView ok,cancel;

    DatabaseHelper databaseHelper;

    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

//        preferences = getSharedPreferences("Userinfo",0);

        //id = getIntent().getStringExtra("id");

//        id = preferences.getString("userid","");

        databaseHelper = new DatabaseHelper(this);

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

//        username = navigationView.getHeaderView(0).findViewById(R.id.username);
//        email = navigationView.getHeaderView(0).findViewById(R.id.email);
//        gender = navigationView.getHeaderView(0).findViewById(R.id.imageView);
//
//        username.setText(preferences.getString("username", null));
//        email.setText(preferences.getString("email", null));

        /*Userinfo info = databaseHelper.getuserinfo(id);
        username.setText(info.username);
        email.setText(info.email);*/

        /*String sex = preferences.getString("gender", null);

        Userinfo info = databaseHelper.getuserinfo(id);

        if (info.image != null) {
            gender.setImageBitmap(getBitmap(info.image));
        } else if (sex.equals("Male")) {
            gender.setImageResource(R.drawable.);
        } else if (sex.equals("Female")) {
            gender.setImageResource(R.drawable.);
        }
        //   }*/
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        displayuser();
//    }
//
//    public void displayuser()
//    {
//        Userinfo info = databaseHelper.getuserinfo(id);
//        username.setText(info.username);
//        email.setText(info.email);
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.lendborrow){
            startActivity(new Intent(Navigation_drawer.this,Lend_Borrow.class));
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
        else if(id == R.id.budget){
            startActivity(new Intent(Navigation_drawer.this,Budget.class));
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

        // perform set on time changed listener event
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minutes = mcurrentTime.get(Calendar.MINUTE);

                // display a toast with changed values of time picker
                Toast.makeText(getApplicationContext(), hourOfDay + " - " + minute, Toast.LENGTH_SHORT).show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String timevalue = timePicker.getHour()+"-"+timePicker.getMinute();
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

//    public void refreshlist(){
//
//        Records records = new Records();
//        records.refresh();
//
//    }

}