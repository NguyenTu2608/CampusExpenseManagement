package com.example.campus_expensemanager.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
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

public class RegisterFragment extends Fragment {

    private EditText etEmail, etUsername, etPassword, etPhone;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize the views
        etEmail = view.findViewById(R.id.etEmail);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        etPhone = view.findViewById(R.id.etPhone);
        btnRegister = view.findViewById(R.id.btnRegister);

        // Set onClick listener for the register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                // Validate the input
                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the user details using SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userPrefs", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.putString("phone", phone);
                editor.apply();

                Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();

                // Optionally, you can navigate back to the login screen or the home screen
                // Example: Navigate back to LoginFragment
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}