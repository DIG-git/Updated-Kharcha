package com.dristi.kharcha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_Listview extends ArrayAdapter<ExpenseInfo>{

    Context context;
    DatabaseHelper databaseHelper;

    public Adapter_Listview(@NonNull Context context, ArrayList<ExpenseInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SharedPreferences preferences = context.getSharedPreferences("Currency",0);

        final View view = LayoutInflater.from(context).inflate(R.layout.listview_inflator,null);

        TextView name = view.findViewById(R.id.name),
                currency = view.findViewById(R.id.currency),
                description = view.findViewById(R.id.description),
                amount = view.findViewById(R.id.amount);

        ImageView imageView = view.findViewById(R.id.image);

        final CheckBox checkBox = view.findViewById(R.id.check);

        databaseHelper = new DatabaseHelper(getContext());

        final ExpenseInfo info = getItem(position);

        name.setText(info.category);
        currency.setText(preferences.getString("currency","Rs.") + " ");
        amount.setText(String.valueOf(info.amount));

        final int id = info.id;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Transaction settled!!");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseHelper.deletelb(info.id);

                        Intent intent = new Intent(context, Lend_Borrow.class);
                        intent.putExtra("id", 1);
                        context.startActivity(intent);

                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkBox.setChecked(false);
                    }
                });

                checkBox.setChecked(false);
                builder.show();
            }
        });


        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(id, v.getId(), 1,"Installment");
                menu.add(id, v.getId(), 2,"Edit");
                menu.add(id, v.getId(), 3,"Delete");
            }
        });

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
                imageView.setImageResource(R.drawable.ic_household);
                break;
            default:
                break;
        }
        return view;
    }
}
