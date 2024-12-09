package com.example.campus_expensemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

public class SpendingChartFragment extends Fragment {

    private String username;
    private DatabaseHelper dbHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        dbHelper = new DatabaseHelper(getContext());
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        return view;
    }

}
