package com.dristi.kharcha;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BudgetListFragment extends Fragment {

    DatabaseHelper databaseHelper;

    ListView listView;

    SharedPreferences preferences;

    TextView norecord;

    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.budget_list_frag,null);

        listView = view.findViewById(R.id.budgetlist);

        norecord = view.findViewById(R.id.norecord);

        databaseHelper = new DatabaseHelper(getActivity());

        preferences = getActivity().getSharedPreferences("Detail_id",0);

        id = preferences.getInt("id",0);

        final BudgetInfo info = databaseHelper.getbudgetdetail(id);

        if(databaseHelper.getbudgetexpenselist(info.category, info.fromdate, info.todate).isEmpty()){
            norecord.setVisibility(View.VISIBLE);
        }
        else{
            norecord.setVisibility(View.INVISIBLE);
        }

        listView.setAdapter(new ListAdapter(getActivity(), databaseHelper.getbudgetexpenselist(info.category, info.fromdate, info.todate)));

        return view;
    }
}
