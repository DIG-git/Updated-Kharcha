package com.dristi.kharcha;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class BudgetChartFragment extends Fragment {

    DatabaseHelper databaseHelper;

    LineChart linechart;

    String date;

    SharedPreferences preferences;

    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.budget_chart_frag,null);

        databaseHelper = new DatabaseHelper(getActivity());

        linechart = view.findViewById(R.id.linechart);

        preferences = getActivity().getSharedPreferences("Detail_id",0);

        id = preferences.getInt("id",0);

        final BudgetInfo info = databaseHelper.getbudgetdetail(id);

        setDate(info.fromdate);

        ArrayList<Entry> dataSet = new ArrayList<>();

        int count = 1;

        while(info.todate.compareTo(getDate()) > 0){
            dataSet.add(new Entry(count, databaseHelper.getbudgetdetailtotal(info.category, getDate())));
            System.out.println(count);
            setDate(getnewdate(getDate()));
            count++;
        }

        LineDataSet lineDataSet = new LineDataSet(dataSet, info.category);
        lineDataSet.setColors(Color.parseColor("#FF808080"));
        lineDataSet.setLineWidth(4);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(85);

        LineData lineData = new LineData(lineDataSet);

        linechart.setData(lineData);
        linechart.invalidate();
        linechart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        linechart.getDescription().setText("Days");
        linechart.getDescription().setTextSize(10f);
        linechart.getLegend().setEnabled(false);
        linechart.getXAxis().setGranularity(1);

        return view;
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

        calendar.add(Calendar.DATE,1);
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
