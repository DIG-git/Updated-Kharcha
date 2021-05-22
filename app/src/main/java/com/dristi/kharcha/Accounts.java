package com.dristi.kharcha;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Accounts extends Fragment {

    FloatingActionMenu menu;

    FloatingActionButton b1, b2;

    private ArrayList<Categories_item> categorylist;

    ArrayList<String> currencylist;

    private Categories_adapter Adapter;

    TextView credit, cash, balance, norecord;

    DatabaseHelper databaseHelper, dbhelper, dbHelper;

    ListView listView;

    SharedPreferences preferences, execute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_accounts, null);

        databaseHelper = new DatabaseHelper(getActivity());

        credit = view.findViewById(R.id.credit);
        cash = view.findViewById(R.id.cash);
        balance = view.findViewById(R.id.balance);
        norecord = view.findViewById(R.id.norecord);

        listView = view.findViewById(R.id.recent);

        execute = getActivity().getSharedPreferences("Execute1", 0);
        preferences = getActivity().getSharedPreferences("Currency", 0);

        cash.setText(String.valueOf(databaseHelper.getcashtotal()));
        credit.setText(String.valueOf(databaseHelper.getcredittotal()));
        balance.setText(String.valueOf(databaseHelper.getbalance()));

        if (databaseHelper.getrecentexpenselist().isEmpty()) {
            norecord.setVisibility(View.VISIBLE);
        } else {
            norecord.setVisibility(View.INVISIBLE);
            listView.setAdapter(new ListAdapter(getActivity(), databaseHelper.getrecentexpenselist()));
        }

        menu = view.findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);

        menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

            }
        });

        b1 = view.findViewById(R.id.b1);
        b2 = view.findViewById(R.id.b2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                addExpense();
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                addIncome();
            }
        });

        return view;
    }

    public void addExpense() {

        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Expense");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense, null);

        dbHelper = new DatabaseHelper(getActivity());

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        final Spinner income_spinner = view.findViewById(R.id.cashcredit);
        final Spinner spinnercategories = view.findViewById(R.id.categories);
        final CheckBox payments = view.findViewById(R.id.payments);

        income_spinner.setAdapter(new Income_spinner(getActivity(), getSpinner()));

        initlist();
        Adapter = new Categories_adapter(getActivity(), categorylist);
        spinnercategories.setAdapter(Adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAmountEmpty(amount)) {

                    Categories_item categoryval = (Categories_item) spinnercategories.getSelectedItem();
                    String categoryVal = categoryval.getName().toString();

                    final String spinnerval = income_spinner.getSelectedItem().toString();

                    if (spinnerval.equals("Choose category")) {
                        Toast.makeText(getActivity(), "Choose category", Toast.LENGTH_SHORT).show();
                    } else {

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String date = df.format(Calendar.getInstance().getTime());

                        String a = amount.getText().toString();
                        int amountVal = Integer.parseInt(a);

                        String descriptionVal = description.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("date", date);
                        contentValues.put("amount", amountVal);
                        contentValues.put("description", descriptionVal);
                        contentValues.put("category", categoryVal);
                        contentValues.put("cashcredit", spinnerval);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("amount", amountVal);
                        contentValues1.put("description", descriptionVal);
                        contentValues1.put("category", categoryVal);
                        contentValues1.put("cashcredit",spinnerval);

                        databaseHelper.insertexpense(contentValues);

                        if (payments.isChecked()) {
                            databaseHelper.insertPayment(contentValues1);
                        }

                        dialog.dismiss();
                    }
                }

                if (databaseHelper.getrecentexpenselist().isEmpty()) {
                    norecord.setVisibility(View.VISIBLE);
                } else {
                    norecord.setVisibility(View.INVISIBLE);
                    listView.setAdapter(new ListAdapter(getActivity(), databaseHelper.getrecentexpenselist()));
                }

                cash.setText(String.valueOf(databaseHelper.getcashtotal()));
                credit.setText(String.valueOf(databaseHelper.getcredittotal()));
                balance.setText(String.valueOf(databaseHelper.getbalance()));
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

    public void addIncome() {

        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Income");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_income, null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        dbhelper = new DatabaseHelper(getActivity());

        final Spinner income_spinner = view.findViewById(R.id.income_spinner);

        income_spinner.setAdapter(new Income_spinner(getActivity(), getSpinner()));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAmountEmpty(amount)) {
                    String categoryval = income_spinner.getSelectedItem().toString();

                    if (categoryval.equals("Choose category")) {
                        Toast.makeText(getActivity(), "Choose category", Toast.LENGTH_SHORT).show();
                    } else {
                        String a = amount.getText().toString();
                        int amountval = Integer.parseInt(a);

                        String descriptionval = description.getText().toString();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String date = df.format(Calendar.getInstance().getTime());

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("date", date);
                        contentValues.put("amount", amountval);
                        contentValues.put("description", descriptionval);
                        contentValues.put("category", categoryval);

                        databaseHelper.insertincome(contentValues);

                        dialog.dismiss();
                    }
                }

                cash.setText(String.valueOf(databaseHelper.getcashtotal()));
                credit.setText(String.valueOf(databaseHelper.getcredittotal()));
                balance.setText(String.valueOf(databaseHelper.getbalance()));
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

    public ArrayList<String> getSpinner() {

        ArrayList<String> spinner = new ArrayList<>();

        spinner.add("Choose category");
        spinner.add("Cash");
        spinner.add("Credit Card");

        return spinner;
    }

    private void initlist() {

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

    public boolean isAmountEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("This field cannot be empty");
            //password.setError("Enter Password");
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (databaseHelper.getrecentexpenselist().isEmpty()) {
            norecord.setVisibility(View.VISIBLE);
            listView.setAdapter(new ListAdapter(getActivity(), databaseHelper.getrecentexpenselist()));
        } else {
            norecord.setVisibility(View.INVISIBLE);
            listView.setAdapter(new ListAdapter(getActivity(), databaseHelper.getrecentexpenselist()));
        }

        cash.setText(String.valueOf(databaseHelper.getcashtotal()));
        credit.setText(String.valueOf(databaseHelper.getcredittotal()));
        balance.setText(String.valueOf(databaseHelper.getbalance()));

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.currency_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.currency:
                setcurrency();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setcurrency() {

        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_income, null);

        TextView title = view.findViewById(R.id.title);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        final Spinner spinner = view.findViewById(R.id.income_spinner);


        amount.setVisibility(View.GONE);
        description.setVisibility(View.GONE);

        title.setText("Set Currency");
        add.setText("OK");

        spinner.setAdapter(new Income_spinner(getActivity(), getCurrencylist()));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putString("currency", spinner.getSelectedItem().toString()).apply();

                if (databaseHelper.getrecentexpenselist().isEmpty()) {
                    norecord.setVisibility(View.VISIBLE);
                } else {
                    norecord.setVisibility(View.INVISIBLE);
                    listView.setAdapter(new ListAdapter(getActivity(), databaseHelper.getrecentexpenselist()));
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

    private ArrayList<String> getCurrencylist() {

        currencylist = new ArrayList<>();

        currencylist.add("Rs.");
        currencylist.add("$");
        currencylist.add("€");
        currencylist.add("£");
        currencylist.add("₽");
        currencylist.add("¥");
        currencylist.add("฿");
        currencylist.add("₱");
        currencylist.add("₩");
        currencylist.add("元");

        return currencylist;

    }
}
