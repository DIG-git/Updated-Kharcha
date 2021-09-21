package com.dristi.kharcha;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_SHORT;

public class Records extends Fragment{

    private CalendarView calendarView;

    private TextView income, expense, norecord, inctot, exptot;

    private DatabaseHelper dbhelper;

    private String day, date, mon, incexp, datevalue;

    private ArrayList<Categories_item> categorylist;

    private Categories_adapter Adapter;

    private RecyclerView recyclerView;

    private ImageView calShow;

    private int calcount = 1;

//    AppBarLayout appBarLayout;
//
//    CollapsingToolbarLayout collapsingToolbarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.records_layout,null);

        income = view.findViewById(R.id.income);
        expense = view.findViewById(R.id.expense);
        norecord = view.findViewById(R.id.norecord);
        inctot = view.findViewById(R.id.inctot);
        exptot = view.findViewById(R.id.exptot);

        calendarView = view.findViewById(R.id.calendar);

        calShow = view.findViewById(R.id.calShow);

//        appBarLayout = view.findViewById(R.id.appbar);
//
//        collapsingToolbarLayout = view.findViewById(R.id.collapse_toolbar);
//
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                if(scrollRange == -1){
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if(scrollRange + i == 0){
//                    collapsingToolbarLayout.setTitle("Kharcha");
//                    isShow = false;
//                }
//                else{
//                    collapsingToolbarLayout.setTitle("Kharcha");
//                    isShow = true;
//                }
//            }
//        });

        dbhelper = new DatabaseHelper(getActivity());

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        incexp = "Expense";

        expense.setBackgroundResource(R.drawable.layerlist_records);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String to_date = df.format(Calendar.getInstance().getTime());

        setdate(to_date);

        displayexpense(getDate());

        inctot.setText(String.valueOf(dbhelper.getincometot(getDate())));
        exptot.setText(String.valueOf(dbhelper.getexpensetot(getDate())));

        calShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcount = calcount + 1;
                if(calcount % 2 == 0){
                    calendarView.setVisibility(View.GONE);
                }
                else{
                    calendarView.setVisibility(View.VISIBLE);
                }
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incexp = "Income";
                tabclick(income);
                displayincome(getDate());
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incexp = "Expense";
                tabclick(expense);
                displayexpense(getDate());
            }
        });

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

                setdate(datevalue);

                if(incexp.equals("Expense")){
                    displayexpense(getDate());
                }
                else{
                    displayincome(getDate());
                }
            }
        });

        return view;
    }

    public void setdate(String datevalue){
        date = datevalue;
    }

    public String getDate(){
        return date;
    }

    public void tabclick(View view)
    {
        income.setBackgroundColor(Color.TRANSPARENT);
        expense.setBackgroundColor(Color.TRANSPARENT);
        if(view.getId()==R.id.income)
        {
            income.setBackgroundResource(R.drawable.layerlist_records);
        }
        else{
            expense.setBackgroundResource(R.drawable.layerlist_records);
        }
    }

    public void displayexpense (String value){

        if(dbhelper.getexpenseinfo(value).isEmpty()){
            norecord.setVisibility(View.VISIBLE);
        }
        else{
            norecord.setVisibility(View.INVISIBLE);
        }

        recyclerView.setAdapter(new Expense_recycleviewAdapter(getActivity(), dbhelper.getexpenseinfo(value)));
    }

    public void displayincome (String value){

        if(dbhelper.getincomeinfo(value).isEmpty()){
            norecord.setVisibility(View.VISIBLE);
        }
        else{
            norecord.setVisibility(View.INVISIBLE);
        }

        recyclerView.setAdapter(new Income_recycleviewAdapter(getActivity(), dbhelper.getincomeinfo(value)));
    }

    private int id;

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getOrder()) {

            case 1:
                id = item.getGroupId();
                if(incexp.equals("Expense")){
                    updateExpense(id);
                }
                else{
                    updateIncome(id);
                }
                break;

            case 2:
                id = item.getGroupId();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Delete item !!");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(incexp.equals("Expense")){
                            dbhelper.deleteExpense(id);
                            displayexpense(getDate());
                        }
                        else{
                            dbhelper.deleteincome(id);
                            displayincome(getDate());
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
                // remove stuff here
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public void updateIncome(int Id){

        final Dialog dialog = new Dialog(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Update Income");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_income,null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);
        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);
        dbhelper = new DatabaseHelper(getActivity());

        //int id = getIntent().getIntExtra("id",0);

        final Spinner income_spinner = view.findViewById(R.id.income_spinner);

        income_spinner.setAdapter(new Income_spinner(getActivity(), getSpinner()));

        if(id!=0){
            ExpenseInfo info = dbhelper.getincomeinfo(Id);
            amount.setText(String.valueOf(info.amount));
            description.setText(info.description);
            int spinnerPosition = ((ArrayAdapter<String>)income_spinner.getAdapter()).getPosition(info.category);
            income_spinner.setSelection(spinnerPosition);

            add.setText("Update");
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAmountEmpty(amount)){
                    String categoryval = income_spinner.getSelectedItem().toString();

                    if (categoryval.equals("Choose category")){
                        makeText(getActivity(),"Choose category", LENGTH_SHORT).show();
                    }
                    else{
                        String a = amount.getText().toString();
                        int amountval = Integer.parseInt(a);

                        String descriptionval = description.getText().toString();

                        ContentValues contentValues=new ContentValues();
                        contentValues.put("amount",amountval);
                        contentValues.put("description",descriptionval);
                        contentValues.put("category", categoryval);

                        dbhelper.updateincome(id,contentValues);
                    }

                    displayincome(getDate());

                    dialog.dismiss();
                }
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

    public void updateExpense(int Id){

        final Dialog dialog = new Dialog(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Update Expense");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense,null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        final Spinner income_spinner = view.findViewById(R.id.cashcredit);
        final Spinner spinnercategories = view.findViewById(R.id.categories);
        final CheckBox payments = view.findViewById(R.id.payments);

        payments.setVisibility(View.GONE);

        //id=getIntent().getIntExtra("id",0);

        income_spinner.setAdapter(new Income_spinner(getActivity(), getSpinner()));
        initlist();

        Adapter = new Categories_adapter(getActivity(),categorylist);
        spinnercategories.setAdapter(Adapter);
        spinnercategories.setPadding(5,0,0,0);

        ExpenseInfo info = dbhelper.getexpenseinfo(Id);
        amount.setText(String.valueOf(info.amount));
        description.setText(info.description);
        int spinnerPosition = ((ArrayAdapter<String>)income_spinner.getAdapter()).getPosition(info.cashcredit);
        income_spinner.setSelection(spinnerPosition);

        spinnercategories.setSelection(findIndex(info.category));

        add.setText("Update");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {

                    Categories_item categoryval = (Categories_item) spinnercategories.getSelectedItem();
                    String categoryVal = categoryval.getName().toString();

                    if (categoryval.equals("Choose category")){
                        makeText(getActivity(),"Choose category", LENGTH_SHORT).show();
                    }


                    final String spinnerval = income_spinner.getSelectedItem().toString();

                    String a = amount.getText().toString();
                    int amountVal = Integer.parseInt(a);

                    String descriptionVal = description.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("amount", amountVal);
                    contentValues.put("description", descriptionVal);
                    contentValues.put("category", categoryVal);
                    contentValues.put("cashcredit",spinnerval);

                    dbhelper.updateExpense(id,contentValues);

                    displayexpense(getDate());

                    dialog.dismiss();
//                    ((Navigation_drawer)getActivity()).refreshlist();
                }
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

    public boolean isAmountEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("This field cannot be empty");
            //password.setError("Enter Password");
            return false;
        }
    }

    public ArrayList<String> getSpinner(){

        ArrayList<String> spinner = new ArrayList<>();

        spinner.add("Choose category");
        spinner.add("Cash");
        spinner.add("Credit Card");

        return spinner;
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

    private void initlist(){

        categorylist = new ArrayList<>();
        categorylist.add(new Categories_item("Clothing", R.drawable.ic_clothing));
        categorylist.add(new Categories_item("Eating-out", R.drawable.ic_eating_out));
        categorylist.add(new Categories_item("Education", R.drawable.ic_education));
        categorylist.add(new Categories_item("Entertainment", R.drawable.ic_entertainment));
        categorylist.add(new Categories_item("Grocery", R.drawable.ic_grocery));
        categorylist.add(new Categories_item("Household", R.drawable.ic_household));
        categorylist.add(new Categories_item("Medical", R.drawable.ic_medical));
        categorylist.add(new Categories_item("Personal", R.drawable.ic_personal));
        categorylist.add(new Categories_item("Shopping", R.drawable.ic_shopping));
        categorylist.add(new Categories_item("Transportation", R.drawable.ic_transportation));
        categorylist.add(new Categories_item("Utilities", R.drawable.ic_utilities));
        categorylist.add(new Categories_item("Others", R.drawable.savings));

    }

    @Override
    public void onResume() {
        super.onResume();

        if(incexp.equals("Expense")){
            displayexpense(getDate());
        }
        else{
            displayincome(getDate());
        }

        inctot.setText(String.valueOf(dbhelper.getincometot(getDate())));
        exptot.setText(String.valueOf(dbhelper.getexpensetot(getDate())));

        calShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcount = calcount + 1;
                if(calcount % 2 == 0){
                    calendarView.setVisibility(View.GONE);
                    calShow.setImageResource(R.drawable.ic_outline_keyboard_arrow_down_24);
                }
                else{
                    calendarView.setVisibility(View.VISIBLE);
                    calShow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
            }
        });
    }
}
