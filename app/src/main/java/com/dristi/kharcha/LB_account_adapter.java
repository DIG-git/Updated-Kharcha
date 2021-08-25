package com.dristi.kharcha;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class LB_account_adapter extends ArrayAdapter<Account_lb_info> {

    private Context context;

    private DatabaseHelper databaseHelper;

    public LB_account_adapter(@NonNull Context context, ArrayList<Account_lb_info> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.account_list, null);

        TextView name = view.findViewById(R.id.name),
                lend = view.findViewById(R.id.lend),
                borrow = view.findViewById(R.id.borrow);

        CardView account = view.findViewById(R.id.account);

        databaseHelper = new DatabaseHelper(context);

        final Account_lb_info info = getItem(position);

        borrow.setText(String.valueOf(databaseHelper.getborrowrec(info.id)));
        lend.setText(String.valueOf(databaseHelper.getlendrec(info.id)));
        name.setText(info.account);

//        if(info.lend >= 0){
//            lend.setText(String.valueOf(info.lend));
//        }
//        else{
//            lend.setText(0);
//        }
//
//        if(info.borrow >= 0){
//            borrow.setText(String.valueOf(info.borrow));
//        }
//        else{
//            borrow.setText(0);
//        }

        account.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.add(info.id, v.getId(), 1,"Add Records");
                menu.add(info.id, v.getId(), 2,"Edit");
                menu.add(info.id, v.getId(), 3,"Delete");

            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Lend_Borrow.class);
                intent.putExtra("id", info.id);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
