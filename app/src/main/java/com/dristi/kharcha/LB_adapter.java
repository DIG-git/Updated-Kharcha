package com.dristi.kharcha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LB_adapter extends ArrayAdapter<ExpenseInfo>{

    private Context context;

    private DatabaseHelper databaseHelper;

    public LB_adapter(@NonNull Context context, ArrayList<ExpenseInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SharedPreferences preferences = context.getSharedPreferences("Currency",0);

        final View view = LayoutInflater.from(context).inflate(R.layout.lb_inflator,null);

        TextView name = view.findViewById(R.id.name),
                currency = view.findViewById(R.id.currency),
                description = view.findViewById(R.id.description),
                amount = view.findViewById(R.id.amount);

        CardView install = view.findViewById(R.id.install);

        final ListView install_list = view.findViewById(R.id.install_list);

        ImageView imageView = view.findViewById(R.id.image);

        final CheckBox checkBox = view.findViewById(R.id.check);

        databaseHelper = new DatabaseHelper(getContext());

        final ExpenseInfo info = getItem(position);

        name.setText(info.category);
        currency.setText(preferences.getString("currency","Rs.") + " ");
        amount.setText(String.valueOf(info.amount));

        if(info.category.equals("Lend")){
            imageView.setColorFilter(Color.RED);
        }
        else{
            imageView.setColorFilter(Color.GREEN);
        }

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
                        intent.putExtra("fin", 'y');
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

        final int[] i = {0};
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0]++;
                if(i[0] % 2 != 0){
                    install_list.setVisibility(View.GONE);
                }
                else{
                    install_list.setVisibility(View.VISIBLE);
                    install_list.setAdapter((android.widget.ListAdapter) new Installment_adapter(context, databaseHelper.getInstallmentList(info.id)));
                }
            }
        });
        return view;
    }
}
