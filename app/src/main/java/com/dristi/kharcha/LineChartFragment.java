package com.dristi.kharcha;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    SharedPreferences preferences;

    int set;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.line_chart,null);

        houselinechart = view.findViewById(R.id.housechart);

        set = 0;

        databaseHelper = new DatabaseHelper(getActivity());

        preferences = getActivity().getSharedPreferences("Date",0);

//        if(set == 0){
//            setDate(preferences.getString("Date",""));
//            set++;
//        }

        makechart("Household");
        makechart("Eating-out");
        makechart("Grocery");
        makechart("Personal");
        makechart("Utilities");
        makechart("Medical");
        makechart("Education");

        return view;
    }

    public void makechart(String category){

        setDate("2019-07-01");

        String[] axisData = {"1" , "2", "3" , "4" , "5" };
        int[] yAxisData =  new int[5];

        for(int i=0; i<5; i++){

            yAxisData[i] = databaseHelper.getlinecategorytotal(category, getDate());
            setDate(getnewdate(getDate()));
        }

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#FFF46E72"));

        for(int i = 0; i<axisData.length; i++){
            axisValues.add(i,new AxisValue(i).setLabel(axisData[i]));
        }

        for(int i = 0; i<yAxisData.length;i++){
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
}
