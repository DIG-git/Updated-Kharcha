package com.dristi.kharcha;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Income_recycleviewAdapter extends RecyclerView.Adapter<Income_recycleviewAdapter.ViewHolder> {

    private List<ExpenseInfo> data;
    Activity activity;

    public Income_recycleviewAdapter(Activity activity, List<ExpenseInfo> data)
    {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Income_recycleviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater LI = activity.getLayoutInflater();
        View vw = LI.inflate(R.layout.incomeinflator, null);
        return new Income_recycleviewAdapter.ViewHolder(vw);
    }

    @Override
    public void onBindViewHolder(@NonNull Income_recycleviewAdapter.ViewHolder holder, int position) {

        final ExpenseInfo info = data.get(position);

        SharedPreferences preferences = activity.getSharedPreferences("Currency",0);

        holder.name.setText(info.category);
        holder.currency.setText(preferences.getString("currency","Rs.") + " ");
        holder.amount.setText(String.valueOf(info.amount));

        final int id = info.id;

        if(info.description.isEmpty()){
            holder.description.setText("No description");
        }
        else{
            holder.description.setText(info.description);
        }

        if ("Cash".equals(info.category)) {
            holder.imageView.setImageResource(R.drawable.ic_cash);
        }
        else if ("Credit Card".equals(info.category)) {
            holder.imageView.setImageResource(R.drawable.ic_credit_card);
        }

        holder.divider.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.add(id, v.getId(), 1,"Edit");
                menu.add(id, v.getId(), 2,"Delete");

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name , currency, description, amount;

        LinearLayout divider;

        ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.name = itemView.findViewById(R.id.category);
            this.currency = itemView.findViewById(R.id.currency);
            this.description = itemView.findViewById(R.id.description);
            this.amount = itemView.findViewById(R.id.amount);

            this.divider = itemView.findViewById(R.id.divider);

            this.imageView = itemView.findViewById(R.id.image);
        }
    }
}
