package com.dristi.kharcha;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Income_adapter extends ArrayAdapter<ExpenseInfo> {

    Context context;

    DatabaseHelper databaseHelper;

    public Income_adapter(@NonNull Context context, ArrayList<ExpenseInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View view = LayoutInflater.from(context).inflate(R.layout.incomeinflator,null);

        TextView name=view.findViewById(R.id.category),
                description = view.findViewById(R.id.description),
                amount=view.findViewById(R.id.amount);

        LinearLayout divider = view.findViewById(R.id.divider);

        ImageView imageView = view.findViewById(R.id.image);

        databaseHelper = new DatabaseHelper(getContext());

        final ExpenseInfo info = getItem(position);

        name.setText(info.category);
        amount.setText(String.valueOf(info.amount));

        final int id = info.id;

        if(info.description.isEmpty()){
            description.setText("No description");
        }
        else{
            description.setText(info.description);
        }

        if ("Cash".equals(info.category)) {
            imageView.setImageResource(R.drawable.ic_cash);
        }
        else if ("Credit Card".equals(info.category)) {
            imageView.setImageResource(R.drawable.ic_credit_card);
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
