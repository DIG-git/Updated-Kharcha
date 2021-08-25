package com.dristi.kharcha;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Lend_Borrow extends AppCompatActivity {

    private TextView lend,borrow;

    private ListView listView;

    private DatabaseHelper databaseHelper;

    private FloatingActionButton menu;

    private EditText amount,description;

    private int al;

    private char fin;

    private Button add,cancel;

    private CheckBox balance_change;

    private Spinner income_spinner, spinnercategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend__borrow);

        fin = getIntent().getCharExtra("fin",'n');
        al = getIntent().getIntExtra("id",0);

        if(fin == 'y'){
            finish();
        }

        lend = findViewById(R.id.lend);
        borrow = findViewById(R.id.borrow);

        databaseHelper = new DatabaseHelper(this);

        listView = findViewById(R.id.listview);
        registerForContextMenu(listView);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        menu = findViewById(R.id.menu);

        if(al == 0){
            lend.setText(String.valueOf(databaseHelper.getlentbalance()));
            borrow.setText(String.valueOf(databaseHelper.getborrowbalance()));
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_account();
                }
            });
            listView.setAdapter(new LB_account_adapter(this,databaseHelper.getlbacclist()));
        }
        else{
            actionBar.setTitle("Records");
            lend.setText(String.valueOf(databaseHelper.getlentbalance(al)));
            borrow.setText(String.valueOf(databaseHelper.getborrowbalance(al)));
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_lend_borrow(al);
                }
            });
            listView.setAdapter(new Adapter_Listview(this,databaseHelper.getlblist(al)));
        }
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

    private int Id;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getOrder()) {

            case 1:
                Id = item.getGroupId();
                if(al == 0){
                    add_lend_borrow(Id);
                }
                else{
                    add_installment(Id);
                }
                break;

            case 2:
                Id = item.getGroupId();
                if(al == 0){
                    update_account(Id);
                }
                else{
                    update_lend_borrow(Id);
                }
                break;

            case 3:
                Id = item.getGroupId();
                delete_lb(Id);
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public void delete_lb(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete item !!");
        builder.setMessage("Are you sure you want to delete this?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(al == 0){
                    databaseHelper.deletelbacc(id);
                }
                else{
                    databaseHelper.deletelb(id);
                    Toast.makeText(Lend_Borrow.this, "deleted", Toast.LENGTH_SHORT).show();
                }
                expadapt();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void add_account(){
        final Dialog dialog = new Dialog(Lend_Borrow.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Account");

        View view = LayoutInflater.from(Lend_Borrow.this).inflate(R.layout.add_income, null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);
        TextView title = view.findViewById(R.id.title);

        final Spinner income_spinner = view.findViewById(R.id.income_spinner);

        amount.setVisibility(View.GONE);
        income_spinner.setVisibility(View.GONE);
        title.setText("Add Account");
        description.setHint("Account Name");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameEmpty(description)) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("account", description.getText().toString());

                    databaseHelper.insert_lb_account(contentValues);

                    dialog.dismiss();
                }

                expadapt();
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

    public void update_account(final int id){
        final Dialog dialog = new Dialog(Lend_Borrow.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Account");

        View view = LayoutInflater.from(Lend_Borrow.this).inflate(R.layout.add_income, null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);
        TextView title = view.findViewById(R.id.title);

        final Spinner income_spinner = view.findViewById(R.id.income_spinner);

        if(id != 0){
            String account = databaseHelper.getlbaccinfo(id);
            description.setText(String.valueOf(account));
        }

        title.setText(String.valueOf(id));

        amount.setVisibility(View.GONE);
        income_spinner.setVisibility(View.GONE);
        title.setText("Update Account");
        description.setHint("Account Name");
        add.setText("Update");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameEmpty(description)) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("account", description.getText().toString());

                    databaseHelper.updatelbacc(id, contentValues);

                    dialog.dismiss();
                }

                expadapt();
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

    public void add_lend_borrow(final int al){

        final Dialog dialog = new Dialog(Lend_Borrow.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Lend/Borrow");

        View view = LayoutInflater.from(Lend_Borrow.this).inflate(R.layout.activity_add__lend__borrow,null);

        add = view.findViewById(R.id.add);
        cancel = view.findViewById(R.id.cancel);
        amount = view.findViewById(R.id.amount);
        description = view.findViewById(R.id.description);
        income_spinner = view.findViewById(R.id.cashcredit);
        spinnercategories = view.findViewById(R.id.categories);
        balance_change = view.findViewById(R.id.balance_change);

        spinnercategories.setAdapter(new Income_spinner(this, getlbspinner()));

        income_spinner.setAdapter(new Income_spinner(this, getSpinner()));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {
                    final String spinnerval = income_spinner.getSelectedItem().toString();
                    final String categoryval = spinnercategories.getSelectedItem().toString();

                    if (spinnerval.equals("Choose category")){
                        Toast.makeText(Lend_Borrow.this, "Choose category", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String date = df.format(Calendar.getInstance().getTime());

                        String a = amount.getText().toString();
                        int amountVal = Integer.parseInt(a);

                        String descriptionVal = description.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ac_id", al);
                        contentValues.put("date", date);
                        contentValues.put("amount", amountVal);
                        contentValues.put("description", descriptionVal);
                        contentValues.put("category", categoryval);
                        contentValues.put("cashcredit",spinnerval);

                        if(balance_change.isChecked()){
                            contentValues.put("deduct", "No");
                        }
                        else{
                            contentValues.put("deduct", "Yes");
                        }

                        databaseHelper.insertlb(contentValues);

                        dialog.dismiss();
                    }


                    expadapt();
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

    public void add_installment(final int id_lend){

        final Dialog dialog = new Dialog(Lend_Borrow.this,R.style.Theme_AppCompat_Light_Dialog_Alert);

        View view = LayoutInflater.from(Lend_Borrow.this).inflate(R.layout.add_income,null);

        TextView title = view.findViewById(R.id.title);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        databaseHelper = new DatabaseHelper(this);

        final Spinner income_spinner = view.findViewById(R.id.income_spinner);

        description.setVisibility(View.GONE);
        income_spinner.setVisibility(View.GONE);

        add.setText("OK");
        title.setText("Installment");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAmountEmpty(amount)){

                    int lbamount = databaseHelper.getlbamount(id_lend);

                    String a = amount.getText().toString();
                    int amountVal = Integer.parseInt(a);

                    if(amountVal<lbamount){
                        int final_amount = lbamount - amountVal;

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("amount", final_amount);

                        databaseHelper.updatelb(id_lend,contentValues);

                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(Lend_Borrow.this, "Installment is greater than amount", Toast.LENGTH_SHORT).show();
                    }

                    expadapt();
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

    public void update_lend_borrow(int id){

        final Dialog dialog = new Dialog(Lend_Borrow.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Update Lend/Borrow");

        View view = LayoutInflater.from(Lend_Borrow.this).inflate(R.layout.activity_add__lend__borrow,null);

        add = view.findViewById(R.id.add);
        cancel = view.findViewById(R.id.cancel);
        amount = view.findViewById(R.id.amount);
        description = view.findViewById(R.id.description);
        income_spinner = view.findViewById(R.id.cashcredit);
        spinnercategories = view.findViewById(R.id.categories);
        balance_change = view.findViewById(R.id.balance_change);

        spinnercategories.setAdapter(new Income_spinner(this, getlbspinner()));

        income_spinner.setAdapter(new Income_spinner(this, getSpinner()));

        if(Id!=0){
            ExpenseInfo info = databaseHelper.getlbinfo(id);
            amount.setText(String.valueOf(info.amount));
            description.setText(info.description);
            int spinnerPosition = ((ArrayAdapter<String>)income_spinner.getAdapter()).getPosition(info.cashcredit);
            income_spinner.setSelection(spinnerPosition);
            int spinnerPos = ((ArrayAdapter<String>)spinnercategories.getAdapter()).getPosition(info.category);
            spinnercategories.setSelection(spinnerPos);

            if(info.deduct.equals("No")){
                balance_change.setChecked(true);
            }

            add.setText("Update");
        }

//        spinnercategories.setSelection(findIndex(info.category));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {

                    final String spinnerval = income_spinner.getSelectedItem().toString();
                    final String categoryval = spinnercategories.getSelectedItem().toString();

                    if (spinnerval.equals("Choose category")){
                        Toast.makeText(Lend_Borrow.this,"Choose category",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String date = df.format(Calendar.getInstance().getTime());

                        String a = amount.getText().toString();
                        int amountVal = Integer.parseInt(a);

                        String descriptionVal = description.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("date", date);
                        contentValues.put("amount", amountVal);
                        contentValues.put("description", descriptionVal);
                        contentValues.put("category", categoryval);
                        contentValues.put("cashcredit",spinnerval);

                        if(balance_change.isChecked()){
                            contentValues.put("deduct", "No");
                        }
                        else{
                            contentValues.put("deduct", "Yes");
                        }

                        databaseHelper.updatelb(Id,contentValues);

                        dialog.dismiss();
                    }

                    expadapt();
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

    public ArrayList<String> getSpinner(){

        ArrayList<String> spinner = new ArrayList<>();

        spinner.add("Choose category");
        spinner.add("Cash");
        spinner.add("Credit Card");

        return spinner;
    }

    public ArrayList<String> getlbspinner(){

        ArrayList<String> lbspinner = new ArrayList<>();

        lbspinner.add("Lend");
        lbspinner.add("Borrow");

        return lbspinner;
    }

    public boolean isAmountEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("This field cannot be empty");
            return false;
        }
    }

    public boolean isNameEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("This field cannot be empty");
            return false;
        }
    }

    public void expadapt(){
        if(al == 0){
            lend.setText(String.valueOf(databaseHelper.getlentbalance()));
            borrow.setText(String.valueOf(databaseHelper.getborrowbalance()));
            listView.setAdapter(new LB_account_adapter(this,databaseHelper.getlbacclist()));
        }
        else{
            lend.setText(String.valueOf(databaseHelper.getlentbalance(al)));
            borrow.setText(String.valueOf(databaseHelper.getborrowbalance(al)));
            listView.setAdapter(new Adapter_Listview(this,databaseHelper.getlblist(al)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        expadapt();
    }
}