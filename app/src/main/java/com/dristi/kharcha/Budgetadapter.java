package com.dristi.kharcha;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Budgetadapter extends ArrayAdapter<BudgetInfo> {

    Context context;

    DatabaseHelper databaseHelper;

    public Budgetadapter(@NonNull Context context, ArrayList<BudgetInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SharedPreferences preferences = context.getSharedPreferences("Currency",0);

        final View view = LayoutInflater.from(context).inflate(R.layout.budget_listview,null);

        TextView category = view.findViewById(R.id.category),
                currency = view.findViewById(R.id.currency),
                amount = view.findViewById(R.id.amount),
                left = view.findViewById(R.id.left),
                date = view.findViewById(R.id.date);

        ProgressBar progressBar = view.findViewById(R.id.myprogress);

        ImageView imageView = view.findViewById(R.id.image);

        CardView cardView = view.findViewById(R.id.budgetlist);

        databaseHelper = new DatabaseHelper(getContext());

        final BudgetInfo info = getItem(position);

        category.setText(info.category);
        currency.setText(preferences.getString("currency", "Rs.") + " ");
        date.setText(info.fromdate + "  -  " + info.todate);

        progressBar.setMax(info.amount);

        int total = databaseHelper.getBudgettotal(info.category, info.fromdate, info.todate);

        if(total == 0){
            progressBar.setProgress(0);
            amount.setText(String.valueOf(info.amount));
        }

        progressBar.setProgress(total);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#03cd75"), PorterDuff.Mode.SRC_IN);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#03cd75"), PorterDuff.Mode.SRC_IN);

        amount.setText(String.valueOf(info.amount-total));

        if(total>info.amount){
            progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            left.setText("Overspent");
        }

        final int id = info.id;

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

        cardView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.add(id, v.getId(), 1,"Edit");
                menu.add(id, v.getId(), 2,"Delete");

            }
        });

        return view;
    }
}
