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

public class HomeFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private TextView tvWelcomeMessage;

    public HomeFragment() {
        // Default constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // Initialize views
        tvWelcomeMessage = view.findViewById(R.id.tv_name);
        Button btnAddExpense = view.findViewById(R.id.btn_add_expense);

        // Get current user information
        Cursor cursor = databaseHelper.getCurrentUser(); // Add a method to fetch the current logged-in user
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));

            // Set welcome message
            tvWelcomeMessage.setText("Hello, " + username + "!");
            cursor.close();
        }

        // Set onClickListener for adding expense
        btnAddExpense.setOnClickListener(v -> loadFragment(new AddExpenseFragment()));

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close the database to avoid memory leaks
        databaseHelper.close();
    }
}
