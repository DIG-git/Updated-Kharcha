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

import java.util.ArrayList;

public class Categories_adapter extends ArrayAdapter<Categories_item> {

    public Categories_adapter(Context context, ArrayList<Categories_item> categorieslist){
        super(context,0,categorieslist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertview,ViewGroup parent){
        if (convertview==null){
            convertview = LayoutInflater.from(getContext()).inflate(R.layout.categories_item,parent,false);
        }

        ImageView imageView = convertview.findViewById(R.id.imagec);
        TextView textView = convertview.findViewById(R.id.categoryc);

        Categories_item currentItem = getItem(position);

        if(convertview!=null){
            imageView.setImageResource(currentItem.getImage());
            textView.setText(currentItem.getName());
        }

        return convertview;
    }
}
