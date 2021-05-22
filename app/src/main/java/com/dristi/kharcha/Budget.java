package com.dristi.kharcha;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Budget extends AppCompatActivity {

    ListView listView;

    FloatingActionButton floatingActionButton;

    DatabaseHelper databaseHelper;

    String day, mon, datevalue;

    private ArrayList<Categories_item> categorylist;

    private Categories_adapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        listView = findViewById(R.id.listbudget);

        floatingActionButton = findViewById(R.id.entry);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBudget();
            }
        });

        listView.setAdapter(new Budgetadapter(Budget.this, databaseHelper.getbudgetlist()));
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
    
    private void initlist(){

        categorylist = new ArrayList<>();

        categorylist.add(new Categories_item("Household",R.drawable.ic_household));
        categorylist.add(new Categories_item("Eating-out",R.drawable.ic_eating_out));
        categorylist.add(new Categories_item("Grocery",R.drawable.ic_grocery));
        categorylist.add(new Categories_item("Personal",R.drawable.ic_personal));
        categorylist.add(new Categories_item("Utilities",R.drawable.ic_utilities));
        categorylist.add(new Categories_item("Medical",R.drawable.ic_medical));
        categorylist.add(new Categories_item("Education",R.drawable.ic_education));
        categorylist.add(new Categories_item("Entertainment",R.drawable.ic_entertainment));
        categorylist.add(new Categories_item("Clothing",R.drawable.ic_clothing));
        categorylist.add(new Categories_item("Transportation",R.drawable.ic_transportation));
        categorylist.add(new Categories_item("Shopping",R.drawable.ic_shopping));
        categorylist.add(new Categories_item("Others",R.drawable.savings));

    }

    public boolean isAmountEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("This field cannot be empty");
            return false;
        }
    }

    public void addBudget(){
        final Dialog dialog = new Dialog(Budget.this,R.style.Theme_AppCompat_Light_Dialog_Alert);

        View view = LayoutInflater.from(this).inflate(R.layout.budget_dialog,null);

        final CalendarView calendarView = view.findViewById(R.id.datepicker);

        final EditText amount = view.findViewById(R.id.amount),
                fromdate = view.findViewById(R.id.fromdate),
                todate = view.findViewById(R.id.todate);

        final Spinner spinner = view.findViewById(R.id.category_spinner);

        Button ok =  view.findViewById(R.id.okbutton),
                cancel = view.findViewById(R.id.cancelbutton);

        initlist();
        Adapter = new Categories_adapter(Budget.this, categorylist);
        spinner.setAdapter(Adapter);

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

                if(isAmountEmpty(amount)) {

                    Categories_item categoryval = (Categories_item) spinner.getSelectedItem();
                    String categoryVal = categoryval.getName().toString();

                    String a = amount.getText().toString();
                    int amountVal = Integer.parseInt(a);

                    String fromd = fromdate.getText().toString();
                    String tod = todate.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("fromdate", fromd);
                    contentValues.put("amount", amountVal);
                    contentValues.put("todate", tod);
                    contentValues.put("category", categoryVal);

                    databaseHelper.insertBudget(contentValues);
                }

                    listView.setAdapter(new Budgetadapter(Budget.this, databaseHelper.getbudgetlist()));

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

    public void updateBudget(final int id){
        final Dialog dialog = new Dialog(Budget.this,R.style.Theme_AppCompat_Light_Dialog_Alert);

        View view = LayoutInflater.from(this).inflate(R.layout.budget_dialog,null);

        final CalendarView calendarView = view.findViewById(R.id.datepicker);

        final EditText amount = view.findViewById(R.id.amount),
                fromdate = view.findViewById(R.id.fromdate),
                todate = view.findViewById(R.id.todate);

        final Spinner spinner = view.findViewById(R.id.category_spinner);

        Button ok =  view.findViewById(R.id.okbutton),
                cancel = view.findViewById(R.id.cancelbutton);

        ok.setText("Update");

        initlist();
        Adapter = new Categories_adapter(Budget.this, categorylist);
        spinner.setAdapter(Adapter);

        BudgetInfo info = databaseHelper.getbudgetinfo(id);

        amount.setText(String.valueOf(info.amount));
        fromdate.setText(info.fromdate);
        todate.setText(info.todate);

        spinner.setSelection(findIndex(info.category));

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

                if(isAmountEmpty(amount)) {

                    Categories_item categoryval = (Categories_item) spinner.getSelectedItem();
                    String categoryVal = categoryval.getName().toString();

                    String a = amount.getText().toString();
                    int amountVal = Integer.parseInt(a);

                    String fromd = fromdate.getText().toString();
                    String tod = todate.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("fromdate", fromd);
                    contentValues.put("amount", amountVal);
                    contentValues.put("todate", tod);
                    contentValues.put("category", categoryVal);

                    databaseHelper.updatebudget(id, contentValues);
                }

                    listView.setAdapter(new Budgetadapter(Budget.this, databaseHelper.getbudgetlist()));

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

    private int id;

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getOrder()){

            case 1:
                id = item.getGroupId();
                updateBudget(id);
                break;

            case 2:
                id = item.getGroupId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Budget.this);

                builder.setTitle("Delete item !!");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseHelper.deletebudget(id);

                        listView.setAdapter(new Budgetadapter(Budget.this, databaseHelper.getbudgetlist()));
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

                break;

            default:
                return super.onContextItemSelected(item);

        }
        return super.onContextItemSelected(item);
    }

    private int findIndex(String cateogry) {
        int selection = 0;
        for(int i = 0; i < categorylist.size(); i++)  {
            if (categorylist.get(i).getName().equals(cateogry)) {
                selection = i;
                break;
            }
        }
        return selection;
    }
}
