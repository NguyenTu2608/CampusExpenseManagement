package com.example.campus_expensemanager.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

public class ChangePasswordFragment extends Fragment {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        etOldPassword = view.findViewById(R.id.et_old_password);
        etNewPassword = view.findViewById(R.id.et_new_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        btnChangePassword = view.findViewById(R.id.btn_change_password);

        // Lấy username từ Bundle
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        btnChangePassword.setOnClickListener(v -> handleChangePassword());

        return view;
    }

    private void handleChangePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.equals(oldPassword)) {
            Toast.makeText(getContext(), "New password cannot be the same as the old password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor userCursor = dbHelper.getUserByUsername(username);

        if (userCursor != null && userCursor.moveToFirst()) {
            @SuppressLint("Range") String storedPassword = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));

            if (!oldPassword.equals(storedPassword)) {
                Toast.makeText(getContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isUpdated = dbHelper.updatePassword(username, newPassword);

            if (isUpdated) {
                Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                // Quay lại màn hình trước hoặc chuyển đến HomeFragment
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Failed to change password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}
