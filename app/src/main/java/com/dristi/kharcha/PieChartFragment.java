package com.dristi.kharcha;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;

public class PieChartFragment extends Fragment {

    DatabaseHelper databaseHelper;

    SharedPreferences preferences;

    String fromd, tod;

    PieChart pieChart;

    PieData data;

    PieDataSet dataSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.pie_chart,null);

        databaseHelper = new DatabaseHelper(getActivity());

        preferences = getActivity().getSharedPreferences("ChartDate", 0);

        pieChart = view.findViewById(R.id.piechart);

        ArrayList<PieEntry> entries = new ArrayList<>();

        fromd = preferences.getString("fromd"," ");
        tod = preferences.getString("tod"," ");

//        Log.d("from",fromd);
//        Log.d("to",tod);

        final int[] color = {Color.parseColor("#4DB6AC"), Color.parseColor("#FFF0B33A"), Color.parseColor("#FFE682AE"),
                        Color.parseColor("#FF45B4E3"), Color.parseColor("#FFF2E446"), Color.parseColor("#FFF86473"),
                        Color.parseColor("#FFAEACAC"), Color.parseColor("#FFA260CE"), Color.parseColor("#FFAADEE3"),
                        Color.parseColor("#558B2F"), Color.parseColor("#FFCB4B3D"), Color.parseColor("#FF37D244")};

        ArrayList<String> categories = new ArrayList();

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

        ArrayList<Integer> colour = new ArrayList<>();

        for(int i = 0; i<categories.size(); i++){
            if((databaseHelper.getcategorytotal(categories.get(i), fromd, tod) > 0)){
                colour.add(color[i]);
                entries.add(new PieEntry(databaseHelper.getcategorytotal(categories.get(i), fromd, tod), categories.get(i)));
            }
        }

        dataSet = new PieDataSet(entries, null);

        data = new PieData(dataSet);

        dataSet.setSelectionShift(5f);
        dataSet.setColors(colour);
        dataSet.setDrawValues(true);
        dataSet.setSliceSpace(3f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueTextSize(10);

        pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleColor(Color.parseColor("White"));
        pieChart.setCenterTextColor(Color.parseColor("#FF808080"));
        pieChart.animateXY(800,800);
        pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        pieChart.setEntryLabelColor(Color.parseColor("#FF808080"));
        pieChart.setEntryLabelTextSize(10);

        if (databaseHelper.getChartTotal(fromd, tod) == 0){
            pieChart.setCenterText("No Records \n" + "From "+ fromd + "\n" + "To " + tod);
            pieChart.setCenterTextSize(13);
        }
        else{
            pieChart.setCenterText("Expense Records \n" + "From "+ fromd + "\n" + "To " + tod);
            pieChart.setCenterTextSize(13);
        }

        pieChart.setData(data);

        return view;
    }

}
