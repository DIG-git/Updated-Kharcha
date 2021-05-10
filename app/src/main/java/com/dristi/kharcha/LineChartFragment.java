package com.dristi.kharcha;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartFragment extends Fragment {

    LineChartView houselinechart;

    String date;

    DatabaseHelper databaseHelper;

    private ArrayList<Categories_item> categorylist;

    private Categories_adapter Adapter;

    SharedPreferences preferences;

    String fromd, tod, categoryVal;

    Spinner categories;

    int i = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.line_chart,null);

        houselinechart = view.findViewById(R.id.housechart);
        categories = view.findViewById(R.id.categories);

        databaseHelper = new DatabaseHelper(getActivity());

        preferences = getActivity().getSharedPreferences("ChartDate", 0);

        initlist();
        Adapter = new Categories_adapter(getActivity(),categorylist);
        categories.setAdapter(Adapter);

        fromd = preferences.getString("fromd"," ");
        tod = preferences.getString("tod"," ");

//        if(set == 0){
//            setDate(preferences.getString("Date",""));
//            set++;
//        }

//        if(i == 0){
//            i  = 1;
//            makechart("Household");
//        }

        Categories_item categoryval = (Categories_item) categories.getSelectedItem();
        categoryVal = categoryval.getName().toString();

        makechart(categoryVal);

        return view;
    }

    public void makechart(String category){

        setDate(fromd);

        int count = 0;
        int[] yAxisData =  new int[30];
        int[] axisData =  new int[30];

        while(tod.compareTo(getDate()) > 0){
            axisData[count] = count + 1;
            yAxisData[count] = databaseHelper.getlinecategorytotal(category, getDate());
            setDate(getnewdate(getDate()));
            count++;
        }

        System.out.println(axisData[5]);
        System.out.println(yAxisData[0]);

//        for(int i=0; i<5; i++){
//
//            yAxisData[i] = databaseHelper.getlinecategorytotal(category, getDate());
//            setDate(getnewdate(getDate()));
//        }

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#FFF46E72"));

        for(int i = 0; i<=count; i++){

            axisValues.add(i,new AxisValue(i).setLabel(String.valueOf(axisData[i])));
        }

        for(int i = 0; i<=count;i++){
            yAxisValues.add(new PointValue(i,yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        switch (category){
            case "Household":
                houselinechart.setLineChartData(data);
                break;

            case "Eating-out":
                houselinechart.setLineChartData(data);
                break;

            case "Personal":
                houselinechart.setLineChartData(data);
                break;

            case "Grocery":
                houselinechart.setLineChartData(data);
                break;

            case "Utilities":
                houselinechart.setLineChartData(data);
                break;

            case "Medical":
                houselinechart.setLineChartData(data);
                break;

            case "Education":
                houselinechart.setLineChartData(data);
                break;

                default:
                    break;
        }

        Axis axis = new Axis();
        axis.setValues(axisValues);
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);

        axis.setTextSize(12);
        axis.setTextColor(Color.BLACK);

        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(12);

        axis.setName("Weeks");
        yAxis.setName("Expenses");

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
