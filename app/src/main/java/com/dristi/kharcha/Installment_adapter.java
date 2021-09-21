package com.dristi.kharcha;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Installment_adapter extends RecyclerView.Adapter<Installment_adapter.ViewHolder> {

    private List<BudgetInfo> data;
    Context context;

    public Installment_adapter(Context context, List<BudgetInfo> data)
    {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public Installment_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View vw = LayoutInflater.from(context).inflate(R.layout.lb_inflator,null);
        return new Installment_adapter.ViewHolder(vw);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BudgetInfo info = data.get(position);

        SharedPreferences preferences = context.getSharedPreferences("Currency",0);

        holder.amount.setText(String.valueOf(info.amount));
        holder.date.setText(info.fromdate);
        holder.currency.setText(preferences.getString("currency", "Rs.") + " ");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount , date, currency;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.amount = itemView.findViewById(R.id.amount);
            this.date = itemView.findViewById(R.id.date);
            this.currency = itemView.findViewById(R.id.currency);
        }
    }
}
