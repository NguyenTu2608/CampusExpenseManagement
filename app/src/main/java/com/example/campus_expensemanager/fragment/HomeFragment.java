package com.example.campus_expensemanager.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;
import com.example.campus_expensemanager.entities.User;

public class HomeFragment extends Fragment {

    private TextView tvName, tvFullName, tvBalance;
    private String username;
    private Button btnAddExpense, btnDisplayExpenses;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvFullName = view.findViewById(R.id.tv_full_name);
        tvBalance = view.findViewById(R.id.tv_balance);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);
        btnDisplayExpenses = view.findViewById(R.id.btn_display_expenses);
        // Lấy username từ Bundle truyền vào
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Xử lý sự kiện click cho nút Add Expense
        btnAddExpense.setOnClickListener(v -> {
            // Mở màn hình thêm chi phí (AddExpenseFragment hoặc Activity)
            openAddExpenseScreen();
        });

        // Xử lý sự kiện click cho nút Display Expenses
        btnDisplayExpenses.setOnClickListener(v -> {
            // Mở màn hình hiển thị các chi phí đã thêm (DisplayExpensesFragment hoặc Activity)
            openDisplayExpensesScreen();
        });
        // Gọi phương thức cập nhật UI
        updateUI();

        return view;
    }

    private void updateUI() {
        // Lấy dữ liệu người dùng từ cơ sở dữ liệu
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor user = dbHelper.getUserByUsername(username);

        if (user != null && user.moveToFirst()) {
            // Cập nhật lời chào tên người dùng
            @SuppressLint("Range") String userName = user.getString(user.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
            tvName.setText("Chào " + userName);

            // Cập nhật tên đầy đủ trên thẻ ngân hàng
            @SuppressLint("Range") String fullName = user.getString(user.getColumnIndex(DatabaseHelper.COLUMN_FULL_NAME));
            tvFullName.setText(fullName);

            // Cập nhật số dư (mặc định là 0)
            @SuppressLint("Range") double balance = user.getDouble(user.getColumnIndex(DatabaseHelper.COLUMN_BALANCE));
            tvBalance.setText(balance + " VND");
        }
    }

    private void openAddExpenseScreen() {
        // Giả sử bạn có một AddExpenseFragment hoặc Activity để thêm chi phí
        AddExpenseFragment addExpenseFragment = new AddExpenseFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("username", username); // Truyền username
        addExpenseFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, addExpenseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDisplayExpensesScreen() {
        // Giả sử bạn có một DisplayExpensesFragment hoặc Activity để hiển thị chi phí
        DisplayExpenseFragment displayExpenseFragment = new DisplayExpenseFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, displayExpenseFragment); // R.id.fragment_container là container của Fragment trong activity
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

