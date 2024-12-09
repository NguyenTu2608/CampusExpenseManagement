package com.example.campus_expensemanager.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SpendingLimitFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private EditText fromDateEditText, toDateEditText, budgetEditText, notesEditText;
    private Button saveButton,displayButton;
    private Calendar calendar;
    private String username;
    public SpendingLimitFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spending_limt, container, false);

        // Initialize views
        dbHelper = new DatabaseHelper(getContext());
        fromDateEditText = view.findViewById(R.id.from_date);
        toDateEditText = view.findViewById(R.id.to_date);
        budgetEditText = view.findViewById(R.id.budget_input);
        notesEditText = view.findViewById(R.id.note_input);
        saveButton = view.findViewById(R.id.save_button);
        displayButton = view.findViewById(R.id.display_spending_limit);

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }



        // Initialize calendar instance
        calendar = Calendar.getInstance();

        // Set up DatePicker dialogs for both dates
        setupDatePicker(fromDateEditText);
        setupDatePicker(toDateEditText);
        // Handle Save button click
        saveButton.setOnClickListener(v -> saveSpendingLimit());

        displayButton.setOnClickListener(v -> {
            DisplaySpendingLimitFragment displaySpendingLimitFragment = new DisplaySpendingLimitFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("username", username); // Truyền username
            displaySpendingLimitFragment.setArguments(bundle);
            transaction.replace(R.id.fragment_container, displaySpendingLimitFragment); // R.id.fragment_container là container của Fragment trong activity
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return view;
    }

    // Function to set up DatePicker dialog for the EditText
    private void setupDatePicker(EditText editText) {
        editText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        // Format the selected date
                        String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(new GregorianCalendar(year, month, dayOfMonth).getTime());
                        editText.setText(formattedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    // Function to save the entered expense settings
    private void saveSpendingLimit() {
        String description = notesEditText.getText().toString();
        String amount = budgetEditText.getText().toString();
        String startDate = fromDateEditText.getText().toString();
        String endDate = toDateEditText.getText().toString();

        if (description.isEmpty() || amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || username.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
        } else {
            // Insert spending limit into the database
            boolean isInserted = dbHelper.insertSpendingLimit(description, amount, startDate, endDate, username);
            if (isInserted) {
                Toast.makeText(getActivity(), "Spending Limit saved", Toast.LENGTH_SHORT).show();
                notesEditText.setText("");
                budgetEditText.setText("");
                fromDateEditText.setText("");
                toDateEditText.setText("");

            } else {
                Toast.makeText(getActivity(), "Error saving spending limit", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
