package com.dristi.kharcha;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LineChartFragment extends Fragment {

    LineChart linechart;

    String date;

    DatabaseHelper databaseHelper;

    private ArrayList<Categories_item> categorylist;

    private Categories_adapter Adapter;

    SharedPreferences preferences;

    String fromd, tod, categoryVal;

    Spinner categories;

    ImageView cat_icon;

    TextView cat_text, from ,to;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.line_chart,null);

        linechart = view.findViewById(R.id.linechart);
        categories = view.findViewById(R.id.categories);
        cat_text = view.findViewById(R.id.cat_text);
        cat_icon = view.findViewById(R.id.cat_icon);
        from = view.findViewById(R.id.from);
        to = view. findViewById(R.id.to);

        databaseHelper = new DatabaseHelper(getActivity());

        preferences = getActivity().getSharedPreferences("ChartDate", 0);

        initlist();
        Adapter = new Categories_adapter(getActivity(),categorylist);
        categories.setAdapter(Adapter);

        fromd = preferences.getString("fromd"," ");
        tod = preferences.getString("tod"," ");

        from.setText(fromd);
        to.setText(tod);

//        if(set == 0){
//            setDate(preferences.getString("Date",""));
//            set++;
//        }

        Categories_item categoryval = (Categories_item) categories.getSelectedItem();
        categoryVal = categoryval.getName().toString();

        makechart(categoryVal);

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categories_item categoryval = (Categories_item) categories.getSelectedItem();
                categoryVal = categoryval.getName().toString();

                makechart(categoryVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void makechart(String category){

        setDate(fromd);

        ArrayList<Entry> dataSet = new ArrayList<>();

        int count = 1;

        while(tod.compareTo(getDate()) > 0){
            dataSet.add(new Entry(count, databaseHelper.getlinecategorytotal(category, getDate())));
            System.out.println(count);
            setDate(getnewdate(getDate()));
            count++;
        }

        LineDataSet lineDataSet = new LineDataSet(dataSet, category);
        lineDataSet.setColors(Color.parseColor("#FF808080"));
        lineDataSet.setLineWidth(4);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(85);

        LineData lineData = new LineData(lineDataSet);

        linechart.setData(lineData);
        linechart.invalidate();
        linechart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        linechart.getDescription().setText("Weeks");
        linechart.getDescription().setTextSize(10f);
        linechart.getLegend().setEnabled(false);
        linechart.getXAxis().setGranularity(1);

        switch (category){
            case "Household":
                cat_icon.setImageResource(R.drawable.ic_household);
                cat_text.setText(category);
                break;

            case "Eating-out":
                cat_icon.setImageResource(R.drawable.ic_eating_out);
                cat_text.setText(category);
                break;

            case "Personal":
                cat_icon.setImageResource(R.drawable.ic_personal);
                cat_text.setText(category);
                break;

            case "Grocery":
                cat_icon.setImageResource(R.drawable.ic_grocery);
                cat_text.setText(category);
                break;

            case "Utilities":
                cat_icon.setImageResource(R.drawable.ic_utilities);
                cat_text.setText(category);
                break;

            case "Medical":
                cat_icon.setImageResource(R.drawable.ic_medical);
                cat_text.setText(category);
                break;

            case "Education":
                cat_icon.setImageResource(R.drawable.ic_education);
                cat_text.setText(category);
                break;

            case "Entertainment":
                cat_icon.setImageResource(R.drawable.ic_entertainment);
                cat_text.setText(category);
                break;

            case "Clothing":
                cat_icon.setImageResource(R.drawable.ic_clothing);
                cat_text.setText(category);
                break;

            case "Transportation":
                cat_icon.setImageResource(R.drawable.ic_transportation);
                cat_text.setText(category);
                break;

            case "Shopping":
                cat_icon.setImageResource(R.drawable.ic_shopping);
                cat_text.setText(category);
                break;

            case "Others":
                cat_icon.setImageResource(R.drawable.savings);
                cat_text.setText(category);
                break;

                default:
                    break;
        }

    }

    public String getnewdate(String date){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try{
            calendar.setTime(df.parse(date));
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        calendar.add(Calendar.DATE,7);
        String newdate = df.format(calendar.getTime());
        return newdate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String datevalue){
        date = datevalue;
    }

    private void initlist(){

        categorylist = new ArrayList<>();
        categorylist.add(new Categories_item("Household",R.drawable.ic_household));
        categorylist.add(new Categories_item("Eating-out",R.drawable.ic_eating_out));
        categorylist.add(new Categories_item("Grocery",R.drawable.ic_grocery));
        categorylist.add(new Categories_item("Personal",R.drawable.ic_personal));
        categorylist.add(new Categories_item("Utilities",R.drawable.ic_utilities));
        categorylist.add(new Categories_item("Medical",R.drawable.ic_medical));
        categorylist.add(new Categories_item("Education",R.drawable.ic_education));
        categorylist.add(new Categories_item("Entertainment",R.drawable.ic_entertainment));
        categorylist.add(new Categories_item("Clothing",R.drawable.ic_clothing));
        categorylist.add(new Categories_item("Transportation",R.drawable.ic_transportation));
        categorylist.add(new Categories_item("Shopping",R.drawable.ic_shopping));
        categorylist.add(new Categories_item("Others",R.drawable.savings));

    }

}
