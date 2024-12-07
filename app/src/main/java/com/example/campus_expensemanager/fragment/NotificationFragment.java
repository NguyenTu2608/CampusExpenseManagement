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
import androidx.fragment.app.FragmentTransaction;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

public class NotificationFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private String username;
    private Button btnMonthlyLimit, btnSpendingChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        btnMonthlyLimit = view.findViewById(R.id.btn_monthly_limit);
        btnSpendingChart = view.findViewById(R.id.btn_spending_chart);

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Xử lý sự kiện bấm nút
        btnMonthlyLimit.setOnClickListener(v -> {
            ViewSpendingLimitScreen();
        });

        btnSpendingChart.setOnClickListener(v -> {
            ViewSpendingChartScreen();
        });

        return view;
    }
    private void ViewSpendingLimitScreen() {
        // Giả sử bạn có một AddExpenseFragment hoặc Activity để thêm chi phí
        SpendingLimitFragment spendingLimitFragment = new SpendingLimitFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("username", username); // Truyền username
        spendingLimitFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, spendingLimitFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void ViewSpendingChartScreen() {
        // Giả sử bạn có một AddExpenseFragment hoặc Activity để thêm chi phí
        SpendingChartFragment spendingCharttFragment = new SpendingChartFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("username", username); // Truyền username
        spendingCharttFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, spendingCharttFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
