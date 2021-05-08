package com.thenewme.gharbachat;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Add_income extends AppCompatActivity {

    EditText amount,description;

    Button add, cancel;

    Spinner income_spinner;

    String categoryval;

    Integer id;

    DatabaseHelper databaseHelper;

    private ArrayList<Categories_item> list;
    private Categories_adapter Adapterincome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_income);

        add = findViewById(R.id.add);
        cancel = findViewById(R.id.cancel);
        amount=findViewById(R.id.amount);
        description=findViewById(R.id.description);
        databaseHelper = new DatabaseHelper(this);

        id=getIntent().getIntExtra("id",0);

        income_spinner = findViewById(R.id.income_spinner);

        income_spinner.setAdapter(new Income_spinner(this, getSpinner()));

        if(id!=0){
            ExpenseInfo info = databaseHelper.getincomeinfo(id);
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
                    categoryval = income_spinner.getSelectedItem().toString();

                    if (categoryval.equals("Choose category")){
                        Toast.makeText(com.thenewme.gharbachat.Add_income.this,"Choose category",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String a = amount.getText().toString();
                        int amountval = Integer.parseInt(a);

                        String descriptionval = description.getText().toString();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String date = df.format(Calendar.getInstance().getTime());

                        ContentValues contentValues=new ContentValues();
                        contentValues.put("date",date);
                        contentValues.put("amount",amountval);
                        contentValues.put("description",descriptionval);
                        contentValues.put("category", categoryval);

                        if(id==0){
                            databaseHelper.insertincome(contentValues);

                            Intent intent = new Intent(com.thenewme.gharbachat.Add_income.this,Income.class);
                            startActivity(intent);
                            finish();
                        }else {
                            databaseHelper.updateincome(id,contentValues);
                            Toast.makeText(com.thenewme.gharbachat.Add_income.this,"Income updated",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id==0){
                    Intent intent = new Intent(com.thenewme.gharbachat.Add_income.this, Navigation_drawer.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(com.thenewme.gharbachat.Add_income.this, Income.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

        /*initlist();
        Spinner spinnercatergories = findViewById(R.id.categories);

        Adapterincome = new Categories_adapter(this,list);
        spinnercatergories.setAdapter(Adapterincome);

        spinnercatergories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categories_item clickeditem = (Categories_item)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

        ArrayList<String> spinner = new ArrayList<>();

        public ArrayList<String> getSpinner(){

            spinner.add("Choose category");
            spinner.add("Cash");
            spinner.add("Credit Card");

            return spinner;
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

    /*private void initlist() {
        list = new ArrayList<>();
        list.add(new Categories_item("Cash", R.drawable.ic_household));
        list.add(new Categories_item("Credit Card", R.drawable.ic_eating_out));
    }*/
}
