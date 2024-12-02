package com.example.campus_expensemanager.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.campus_expensemanager.activity.HomeActivity;
import com.example.campus_expensemanager.activity.MainActivity;
import com.example.campus_expensemanager.database.DatabaseHelper;
import com.example.campus_expensemanager.entities.User;

public class HomeFragment extends Fragment {

    private TextView tvName, tvFullName, tvBalance;
    private String username;
    private Button btnAddExpense, btnDisplayExpenses, btnLogout, btnInformation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvFullName = view.findViewById(R.id.tv_full_name);
        tvBalance = view.findViewById(R.id.tv_balance);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);
        btnDisplayExpenses = view.findViewById(R.id.btn_display_expenses);
        btnInformation = view.findViewById(R.id.btn_Information);
        btnLogout = view.findViewById(R.id.btn_logout);
        // Lấy username từ Bundle truyền vào
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        btnInformation.setOnClickListener(view1 -> {
            ViewInformationScreen();
        });
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
        btnLogout.setOnClickListener(v -> logout());
        // Gọi phương thức cập nhật UI
        updateUI();

        return view;
    }

    private void logout() {
        // Xóa thông tin đăng nhập trong SharedPreferences
        if (getActivity() != null) {
            getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE)
                    .edit().clear().apply();
        }

        // Quay về màn hình đăng nhập
        // Giả sử bạn có một LoginActivity để xử lý đăng nhập
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp và tạo mới
        startActivity(intent);
        getActivity().finish(); // Kết thúc HomeActivity hoặc màn hình hiện tại
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

    private void ViewInformationScreen() {
        // Giả sử bạn có một AddExpenseFragment hoặc Activity để thêm chi phí
        UserInformationFragment userInformationFragment = new UserInformationFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("username", username); // Truyền username
        userInformationFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, userInformationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        Bundle bundle = new Bundle();
        bundle.putString("username", username); // Truyền username
        displayExpenseFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, displayExpenseFragment); // R.id.fragment_container là container của Fragment trong activity
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

