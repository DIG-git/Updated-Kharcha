package com.dristi.kharcha;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Expense_adapter extends ArrayAdapter<ExpenseInfo> {

    Context context;
    DatabaseHelper databaseHelper;

    public Expense_adapter(@NonNull Context context, ArrayList<ExpenseInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SharedPreferences preferences = context.getSharedPreferences("Currency",0);

        final View view = LayoutInflater.from(context).inflate(R.layout.expenseinflator,null);

        TextView name = view.findViewById(R.id.category),
                currency = view.findViewById(R.id.currency),
                description = view.findViewById(R.id.description),
                amount = view.findViewById(R.id.amount);

        LinearLayout divider = view.findViewById(R.id.divider);

        ImageView imageView = view.findViewById(R.id.image);

        databaseHelper = new DatabaseHelper(getContext());

        final ExpenseInfo info = getItem(position);

        name.setText(info.cashcredit);
        currency.setText(preferences.getString("currency","Rs.") + " ");
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

        return view;

    }
}
