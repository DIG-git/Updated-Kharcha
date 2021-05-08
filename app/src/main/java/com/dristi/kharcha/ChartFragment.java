package com.dristi.kharcha;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import static android.widget.Toast.LENGTH_SHORT;

public class ChartFragment extends Fragment {

    TextView pie, line;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chart,null);

        pie = view.findViewById(R.id.pie);
        line = view.findViewById(R.id.line);

        pie.setBackgroundResource(R.drawable.layerlist_chart);

        PieChartFragment pieChartFragment = new PieChartFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.frame,pieChartFragment).commit();

        pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabclick(pie);
            }
        });

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabclick(line);
            }
        });

        return view;
    }

    public void tabclick(View view)
    {
        pie.setBackgroundColor(Color.TRANSPARENT);
        line.setBackgroundColor(Color.TRANSPARENT);
        if(view.getId()==R.id.pie)
        {
            pie.setBackgroundResource(R.drawable.layerlist_chart);
            PieChartFragment pieChartFragment = new PieChartFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.frame,pieChartFragment).commit();
        }
        else{
            line.setBackgroundResource(R.drawable.layerlist_chart);
            LineChartFragment lineChartFragment = new LineChartFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.frame,lineChartFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PieChartFragment pieChartFragment = new PieChartFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.frame,pieChartFragment).commit();
    }
}
