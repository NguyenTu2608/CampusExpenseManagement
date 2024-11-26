package com.example.campus_expensemanager.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.activity.HomeActivity;
import com.example.campus_expensemanager.database.DatabaseHelper;

public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private DatabaseHelper databaseHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize the views
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvRegister = view.findViewById(R.id.tvRegister);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(getContext());
        // Set onClickListener for the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = etUsername.getText().toString().trim();
                String inputPassword = etPassword.getText().toString().trim();

                // Validate input
                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check credentials in the database
                if (isValidUser(inputUsername, inputPassword)) {
                    // Mark user as logged in
                    databaseHelper.setLoggedIn(inputUsername, true);

                    // Successful login, navigate to HomeActivity
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    // Finish the current activity to prevent going back to the login screen
                    getActivity().finish();
                } else {
                    // Show error message
                    Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Navigate to RegisterFragment
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Navigate to ForgotPasswordFragment
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ForgotPasswordFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private boolean isValidUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        Cursor cursor = null;
        boolean isValid = false;

        try {
            cursor = databaseHelper.getReadableDatabase().rawQuery(query, new String[]{username, password});
            if (cursor != null && cursor.moveToFirst()) {
                isValid = true; // Người dùng hợp lệ
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error checking user credentials", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close(); // Đảm bảo đóng con trỏ
            }
        }

        return isValid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close the database to avoid memory leaks
        databaseHelper.close();
    }
}
