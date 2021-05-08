package com.dristi.kharcha;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Recent_listview extends ArrayAdapter<ExpenseInfo> {

    Context context;

    public Recent_listview(@NonNull Context context, ArrayList<ExpenseInfo> list) {
        super(context, 0,list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_adapter,null);

        TextView name=view.findViewById(R.id.name),
                category = view.findViewById(R.id.category),
                amount=view.findViewById(R.id.amount),
                date = view.findViewById(R.id.date);
        ImageView imageView = view.findViewById(R.id.image);

        final ExpenseInfo info = getItem(position);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String datevalue = df.format(Calendar.getInstance().getTime());

        if((info.date).equals(datevalue)){
            date.setText("Today");
        }
        else {
            date.setText(info.date);
        }

        category.setText(info.cashcredit);
        name.setText(info.category);
        amount.setText(String.valueOf(info.amount));

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
            default:
                break;
        }
        return view;
    }
}
