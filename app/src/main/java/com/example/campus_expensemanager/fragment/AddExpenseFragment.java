package com.example.campus_expensemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

public class AddExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;
    private Spinner categorySpinner;
    private String selectedCategory;
    private Spinner typeSpinner;
    private String selectedType;
    private String username;

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        dbHelper = new DatabaseHelper(getContext());

        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        typeSpinner = view.findViewById(R.id.typeSpinner);


        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        Button addButton = view.findViewById(R.id.addButton);
        Button btnDisplay = view.findViewById(R.id.btnDisplay);


        btnDisplay.setOnClickListener(v -> {
            DisplayExpenseFragment displayExpenseFragment = new DisplayExpenseFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("username", username); // Truyền username
            displayExpenseFragment.setArguments(bundle);
            transaction.replace(R.id.fragment_container, displayExpenseFragment); // R.id.fragment_container là container của Fragment trong activity
            transaction.addToBackStack(null);
            transaction.commit();
        });

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.expense_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedType = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedType = "Expense";
            }
        });

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedCategory = "Other";
            }
        });

        // Add expense action
        addButton.setOnClickListener(v -> addExpense());

        return view;
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountEditText.getText().toString().trim());
            String description = descriptionEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();

            
            boolean inserted = dbHelper.addExpense(username, amount, description, date, selectedCategory, selectedType);
            if (inserted) {
                Toast.makeText(getContext(), "Expense added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error adding expense", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }
}
