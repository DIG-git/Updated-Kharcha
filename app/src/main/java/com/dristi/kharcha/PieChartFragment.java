package com.dristi.kharcha;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class PieChartFragment extends Fragment {

    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.pie_chart,null);

        databaseHelper = new DatabaseHelper(getActivity());

        final PieChart pieChart = view.findViewById(R.id.piechart);

        ArrayList Expenses = new ArrayList();

        final int[] color = {Color.parseColor("#4DB6AC"), Color.parseColor("#FFF0B33A"), Color.parseColor("#FFEDA667"),
                        Color.parseColor("#FF45B4E3"), Color.parseColor("#FFF2E446"), Color.parseColor("#FFF86473"),
                        Color.parseColor("#FFAEACAC"), Color.parseColor("#FFA260CE"), Color.parseColor("#FFAADEE3"),
                        Color.parseColor("#FF45A29A"), Color.parseColor("#FFCB4B3D"), Color.parseColor("#FF37D244")};

        Expenses.add(new Entry(databaseHelper.getcategorytotal("Household"),0));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Eating-out"),1));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Grocery"),2));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Personal"),3));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Utilities"),4));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Medical"),5));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Education"),6));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Entertainment"),7));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Clothing"),8));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Transportation"),9));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Shopping"),10));
        Expenses.add(new Entry(databaseHelper.getcategorytotal("Others"),11));

        final PieDataSet dataSet = new PieDataSet(Expenses, "Total Expenses");
        ArrayList categories = new ArrayList();

        categories.add("Household");
        categories.add("Eating-out");
        categories.add("Grocery");
        categories.add("Personal");
        categories.add("Utilities");
        categories.add("Medical");
        categories.add("Education");
        categories.add("Entertainment");
        categories.add("Clothing");
        categories.add("Transportation");
        categories.add("Shopping");
        categories.add("Others");

        PieData data = new PieData(categories, dataSet);
        
        pieChart.setData(data);
        pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.setHoleColor(Color.parseColor("#fcd5ece5"));

        dataSet.setColors(color);
        dataSet.setDrawValues(false);

        pieChart.animateXY(1000,1000);

        return view;
    }

}
