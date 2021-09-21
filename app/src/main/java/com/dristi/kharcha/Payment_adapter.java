package com.dristi.kharcha;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Payment_adapter extends ArrayAdapter<PaymentInfo> {

    Context context;
    DatabaseHelper databaseHelper;
    private ArrayList<Categories_item> categorylist;

    public Payment_adapter(@NonNull Context context, ArrayList<PaymentInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View view = LayoutInflater.from(context).inflate(R.layout.paymentinflator,null);

        TextView name = view.findViewById(R.id.category),
                description = view.findViewById(R.id.description),
                amount = view.findViewById(R.id.amount);

        LinearLayout divider = view.findViewById(R.id.divider);

        ImageView imageView = view.findViewById(R.id.image);

        databaseHelper = new DatabaseHelper(getContext());
        final  PaymentInfo info = getItem(position);

        name.setText(info.cashcredit);
        amount.setText(String.valueOf(info.amount));

        final int id = info.id;

        if(info.description.isEmpty()){
            description.setText("No description");
        }
        else{
            description.setText(info.description);
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

        divider.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.add(id, v.getId(), 1,"Edit");
                menu.add(id, v.getId(), 2,"Delete");

            }
        });

        divider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense(info.id);
            }
        });

        return view;

    }

    public void addExpense(Integer Id){
        final Dialog dialog = new Dialog(getContext(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Expense");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_expense,null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        final Spinner income_spinner = view.findViewById(R.id.cashcredit);
        final Spinner spinnercategories = view.findViewById(R.id.categories);

        final CheckBox payments = view.findViewById(R.id.payments);

        payments.setVisibility(View.GONE);

        income_spinner.setAdapter(new Income_spinner(getContext(), getSpinner()));
        initlist();

        final Categories_adapter Adapter = new Categories_adapter(getContext(),categorylist);
        spinnercategories.setAdapter(Adapter);

        PaymentInfo info = databaseHelper.getPaymentInfo(Id);
        amount.setText(String.valueOf(info.amount));
        description.setText(info.description);
        int spinnerPosition = ((ArrayAdapter<String>)income_spinner.getAdapter()).getPosition(info.cashcredit);
        income_spinner.setSelection(spinnerPosition);
        spinnercategories.setSelection(findIndex(info.category));
        add.setText("Add");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {

                    Categories_item categoryval = (Categories_item) spinnercategories.getSelectedItem();
                    String categoryVal = categoryval.getName().toString();

                    final String spinnerval = income_spinner.getSelectedItem().toString();

                    if (categoryval.equals("Choose category")){
                        makeText(getContext(), "Choose category", LENGTH_SHORT).show();
                    }
                    else {
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

                        databaseHelper.insertExpense(contentValues);

                    }

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
}
