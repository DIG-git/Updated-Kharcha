package com.dristi.kharcha;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Income_spinner extends ArrayAdapter {

    Context context;

    public Income_spinner(Context context, ArrayList<String> list){
        super(context,0,list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String spinnerlist = (String) getItem(position);

        TextView textView = new TextView(context);
        textView.setText(spinnerlist);
        textView.setPadding(10,10,10,10);
        textView.setTextSize(17);

        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String spinnerlist = (String) getItem(position);

        TextView textView = new TextView(context);
        textView.setText(spinnerlist);
        textView.setPadding(10,10,10,10);
        textView.setTextSize(17);

        return textView;
    }
}
