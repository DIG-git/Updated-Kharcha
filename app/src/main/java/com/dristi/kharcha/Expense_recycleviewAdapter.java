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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Expense_recycleviewAdapter extends RecyclerView.Adapter<Expense_recycleviewAdapter.ViewHolder> {

    private List<ExpenseInfo> data;
    Activity activity;

    public Expense_recycleviewAdapter(Activity activity, List<ExpenseInfo> data)
    {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Expense_recycleviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater LI = activity.getLayoutInflater();
        View vw = LI.inflate(R.layout.expenseinflator, null);
        return new ViewHolder(vw);
    }

    @Override
    public void onBindViewHolder(@NonNull Expense_recycleviewAdapter.ViewHolder holder, int position) {

        final ExpenseInfo info = data.get(position);

        SharedPreferences preferences = activity.getSharedPreferences("Currency",0);

        holder.name.setText(info.cashcredit);
        holder.currency.setText(preferences.getString("currency","Rs.") + " ");
        holder.amount.setText(String.valueOf(info.amount));

        final int id = info.id;

        if(info.description.isEmpty()){
            holder.description.setText("No description");
        }
        else{
            holder.description.setText(info.description);
        }

        switch (info.category){
            case "Household":
                holder.imageView.setImageResource(R.drawable.ic_household);
                break;
            case "Eating-out":
                holder.imageView.setImageResource(R.drawable.ic_eating_out);
                break;
            case "Grocery":
                holder.imageView.setImageResource(R.drawable.ic_grocery);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.ic_personal);
                break;
            case "Utilities":
                holder.imageView.setImageResource(R.drawable.ic_utilities);
                break;
            case "Medical":
                holder.imageView.setImageResource(R.drawable.ic_medical);
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.ic_education);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Clothing":
                holder.imageView.setImageResource(R.drawable.ic_clothing);
                break;
            case "Transportation":
                holder.imageView.setImageResource(R.drawable.ic_transportation);
                break;
            case "Shopping":
                holder.imageView.setImageResource(R.drawable.ic_shopping);
                break;
            case "Others":
                holder.imageView.setImageResource(R.drawable.savings);
                break;
            case "Lend":
                holder.imageView.setImageResource(R.drawable.ic_utilities);
                break;
            case "Borrow":
                holder.imageView.setImageResource(R.drawable.ic_cash);
                break;
            default:
                break;
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
