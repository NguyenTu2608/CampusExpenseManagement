package com.example.campus_expensemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campus_expensemanager.R;

public class NotificationFragment extends Fragment {

    private Button btnMonthlyLimit, btnSpendingChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        btnMonthlyLimit = view.findViewById(R.id.btn_monthly_limit);
        btnSpendingChart = view.findViewById(R.id.btn_spending_chart);

        // Xử lý sự kiện bấm nút
        btnMonthlyLimit.setOnClickListener(v ->
                Toast.makeText(getContext(), "Hạn mức chi tiêu", Toast.LENGTH_SHORT).show());

        btnSpendingChart.setOnClickListener(v ->
                Toast.makeText(getContext(), "Biểu đồ chi tiêu", Toast.LENGTH_SHORT).show());

        return view;
    }
}
