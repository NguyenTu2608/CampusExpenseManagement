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

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

public class UserInformationFragment extends Fragment {

    private TextView tvFullName, tvEmail, tvUsername, tvPassword, tvPhone, tvAmount;
    private Button btnChangePassword;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        // Ánh xạ các thành phần UI
        tvFullName = view.findViewById(R.id.tv_full_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvUsername = view.findViewById(R.id.tv_username);
        tvPassword = view.findViewById(R.id.tv_password);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvAmount = view.findViewById(R.id.tv_amount);
        btnChangePassword = view.findViewById(R.id.btn_change_password);

        // Lấy username từ Bundle
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Gọi phương thức cập nhật UI
        updateUI();

        // Xử lý sự kiện khi người dùng bấm nút Change Password
        btnChangePassword.setOnClickListener(v -> openChangePasswordScreen());

        return view;
    }

    @SuppressLint({"Range", "SetTextI18n"})
    private void updateUI() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor user = dbHelper.getUserByUsername(username);

        if (user != null && user.moveToFirst()) {
            tvFullName.setText(user.getString(user.getColumnIndex(DatabaseHelper.COLUMN_FULL_NAME)));
            tvEmail.setText(user.getString(user.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
            tvUsername.setText(user.getString(user.getColumnIndex(DatabaseHelper.COLUMN_USERNAME)));
            tvPassword.setText("********"); // Hiển thị mật khẩu ẩn
            tvPhone.setText(user.getString(user.getColumnIndex(DatabaseHelper.COLUMN_PHONE)));
            tvAmount.setText(user.getDouble(user.getColumnIndex(DatabaseHelper.COLUMN_BALANCE)) + " VND");
        }

        if (user != null) {
            user.close();
        }
        dbHelper.close();
    }

    private void openChangePasswordScreen() {
        // Chuyển sang màn hình đổi mật khẩu
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username); // Truyền username
        changePasswordFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, changePasswordFragment)
                .addToBackStack(null)
                .commit();
    }
}
