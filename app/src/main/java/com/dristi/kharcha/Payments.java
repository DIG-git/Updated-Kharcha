package com.dristi.kharcha;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Payments extends AppCompatActivity {

    ListView listView;

    Payment_adapter adapter;

    DatabaseHelper databaseHelper;

    FloatingActionButton floatingActionButton;

    private ArrayList<Categories_item> categorylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        listView = findViewById(R.id.listview);
        registerForContextMenu(listView);

        floatingActionButton = findViewById(R.id.entry);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPayment();
            }
        });

        databaseHelper =new DatabaseHelper(this);

        adapter = new Payment_adapter(this, databaseHelper.getPaymentList());
    }

    private int id;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getOrder()) {
            case 1:
                id = item.getGroupId();
                update_payments(id);
                break;

            case 2:
                id = item.getGroupId();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Delete item !!");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deletePayment(id);
                        expadapt();
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

    public void update_payments(int Id){
        final Dialog dialog = new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Update Payment");

        View view = LayoutInflater.from(this).inflate(R.layout.add_expense,null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        final Spinner income_spinner = view.findViewById(R.id.cashcredit);
        final Spinner spinnercategories = view.findViewById(R.id.categories);

        final CheckBox payments = view.findViewById(R.id.payments);
        payments.setVisibility(View.GONE);

        income_spinner.setAdapter(new Income_spinner(this, getSpinner()));
        initlist();

        final Categories_adapter Adapter = new Categories_adapter(this,categorylist);
        spinnercategories.setAdapter(Adapter);

        PaymentInfo info = databaseHelper.getPaymentInfo(Id);
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
                        makeText(Payments.this, "Choose category", LENGTH_SHORT).show();
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
                    databaseHelper.updatePayment(id,contentValues);
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

    public void addPayment(){
        final Dialog dialog = new Dialog(Payments.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Payment");

        View view = LayoutInflater.from(Payments.this).inflate(R.layout.add_expense,null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);
        TextView title = view.findViewById(R.id.title);

        title.setText("Add Payment");

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        final Spinner income_spinner = view.findViewById(R.id.cashcredit);
        final Spinner spinnercategories = view.findViewById(R.id.categories);

        final CheckBox payments = view.findViewById(R.id.payments);
        payments.setVisibility(View.GONE);

        income_spinner.setAdapter(new Income_spinner(this, getSpinner()));
        initlist();

        final Categories_adapter Adapter = new Categories_adapter(this,categorylist);
        spinnercategories.setAdapter(Adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {

                    Categories_item categoryval = (Categories_item) spinnercategories.getSelectedItem();
                    String categoryVal = categoryval.getName().toString();

                    if (categoryval.equals("Choose category")){
                        makeText(Payments.this, "Choose category", LENGTH_SHORT).show();
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
                    databaseHelper.insertPayment(contentValues);
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

    public void expadapt(){
        listView.setAdapter(new Payment_adapter(this,databaseHelper.getPaymentList()));
    }

    @Override
    public void onResume() {
        super.onResume();
        expadapt();
    }
}