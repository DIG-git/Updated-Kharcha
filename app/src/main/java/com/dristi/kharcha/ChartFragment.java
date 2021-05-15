package com.dristi.kharcha;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class ChartFragment extends Fragment {

    TextView pie, line;

    String mon, day, datevalue, fromd, tod;

    DatabaseHelper dbhelper;

    SharedPreferences preferences, execute;

    int i = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chart,null);

        pie = view.findViewById(R.id.pie);
        line = view.findViewById(R.id.line);

        dbhelper = new DatabaseHelper(getActivity());

        pie.setBackgroundResource(R.drawable.layerlist_records);

        preferences = getActivity().getSharedPreferences("ChartDate", 0);

        execute = getActivity().getSharedPreferences("Execute",0);

        fromd = preferences.getString("fromd"," ");
        tod = preferences.getString("tod"," ");

        PieChartFragment pieChartFragment = new PieChartFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.frame,pieChartFragment).commit();

        pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabclick(pie);
            }
        });

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabclick(line);
            }
        });

        return view;
    }

    public synchronized void executeOnce() {
        if (execute.getBoolean("execute",false)) {
            return;
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(Calendar.getInstance().getTime());
            preferences.edit().putString("fromd", date).commit();
            preferences.edit().putString("tod", getnewdate(date)).commit();
            execute.edit().putBoolean("execute",true).commit();
        }
    }

    public String getnewdate(String date){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try{
            calendar.setTime(df.parse(date));
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        calendar.add(Calendar.DATE,30);
        String newdate = df.format(calendar.getTime());
        return newdate;
    }

    public void tabclick(View view)
    {
        pie.setBackgroundColor(Color.TRANSPARENT);
        line.setBackgroundColor(Color.TRANSPARENT);
        if(view.getId()==R.id.pie){
            i = 0;
            pie.setBackgroundResource(R.drawable.layerlist_records);
            PieChartFragment pieChartFragment = new PieChartFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.frame,pieChartFragment).commit();
        }
        else{
            i = 1;
            line.setBackgroundResource(R.drawable.layerlist_records);
            LineChartFragment lineChartFragment = new LineChartFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.frame,lineChartFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(i == 0)
        {
            pie.setBackgroundResource(R.drawable.layerlist_records);
            PieChartFragment pieChartFragment = new PieChartFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.frame,pieChartFragment).commit();
        }
        else{
            line.setBackgroundResource(R.drawable.layerlist_records);
            LineChartFragment lineChartFragment = new LineChartFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.frame,lineChartFragment).commit();
        }

        executeOnce();

        fromd = preferences.getString("fromd"," ");
        tod = preferences.getString("tod"," ");

        preferences.edit().putString("fromd", fromd).apply();
        preferences.edit().putString("tod", tod).apply();

//        Log.d("from1",fromd);
//        Log.d("to1",tod);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.date:
                setDate();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setDate(){
        final Dialog dialog = new Dialog(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.budget_dialog,null);

        final CalendarView calendarView = view.findViewById(R.id.datepicker);

        final EditText amount = view.findViewById(R.id.amount),
                fromdate = view.findViewById(R.id.fromdate),
                todate = view.findViewById(R.id.todate);

        final Spinner spinner = view.findViewById(R.id.category_spinner);

        Button ok =  view.findViewById(R.id.okbutton),
                cancel = view.findViewById(R.id.cancelbutton);

        amount.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        ok.setText("OK");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if(dayOfMonth<10){
                    day = "0" + dayOfMonth;
                }
                else{
                    day = "" + dayOfMonth;
                }

                if((month + 1 )<10){
                    mon = "0" + (month + 1) ;
                }
                else{
                    mon = "" + (month + 1) ;
                }

                datevalue = year + "-" + mon + "-" + day;

                fromdate.setText(datevalue);
            }
        });

        fromdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        if(dayOfMonth<10){
                            day = "0" + dayOfMonth;
                        }
                        else{
                            day = "" + dayOfMonth;
                        }

                        if((month + 1 )<10){
                            mon = "0" + (month + 1) ;
                        }
                        else{
                            mon = "" + (month + 1) ;
                        }

                        datevalue = year + "-" + mon + "-" + day;

                        fromdate.setText(datevalue);
                    }
                });

                return false;
            }
        });

        todate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        if(dayOfMonth<10){
                            day = "0" + dayOfMonth;
                        }
                        else{
                            day = "" + dayOfMonth;
                        }

                        if((month + 1 )<10){
                            mon = "0" + (month + 1) ;
                        }
                        else{
                            mon = "" + (month + 1) ;
                        }

                        datevalue = year + "-" + mon + "-" + day;

                        todate.setText(datevalue);
                    }
                });

                return false;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromd = fromdate.getText().toString();
                tod = todate.getText().toString();

                preferences.edit().putString("fromd", fromd).apply();
                preferences.edit().putString("tod", tod).apply();

                if(i == 0)
                {
                    pie.setBackgroundResource(R.drawable.layerlist_records);
                    PieChartFragment pieChartFragment = new PieChartFragment();
                    getChildFragmentManager().beginTransaction().replace(R.id.frame,pieChartFragment).commit();
                }
                else{
                    line.setBackgroundResource(R.drawable.layerlist_records);
                    LineChartFragment lineChartFragment = new LineChartFragment();
                    getChildFragmentManager().beginTransaction().replace(R.id.frame,lineChartFragment).commit();
                }

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

}
