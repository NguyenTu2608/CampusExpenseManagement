package com.example.campus_expensemanager.fragment;

import android.app.DatePickerDialog;
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
import com.example.campus_expensemanager.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddCategoryFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText etCategoryName, etCategoryDescription, etCategoryDate;
    private Button btnAdd;
    private String username;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);


        dbHelper = new DatabaseHelper(getContext());
        // Initialize views
        etCategoryName = view.findViewById(R.id.et_category_name);
        etCategoryDescription = view.findViewById(R.id.et_category_description);
        etCategoryDate = view.findViewById(R.id.et_category_date);
        btnAdd = view.findViewById(R.id.btn_add);
        calendar = Calendar.getInstance();

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        setupDatePicker(etCategoryDate);

        // Set click listener for Add button
        btnAdd.setOnClickListener(v -> addCategory());

        return view;
    }

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

    private void addCategory() {
        String name = etCategoryName.getText().toString().trim();
        String description = etCategoryDescription.getText().toString().trim();
        String date = etCategoryDate.getText().toString().trim();  // Optional date field

        // Validate if category name and description are provided
        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(getActivity(), "Category description cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // If date is required, check if it is empty
        if (date.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a date for the category", Toast.LENGTH_SHORT).show();
            return;
        }


        if (dbHelper.isCategoryExists(username, name)) {
            Toast.makeText(getActivity(), "Category already exists. Please choose a different name.", Toast.LENGTH_SHORT).show();
            etCategoryName.setText("");
            etCategoryDescription.setText("");
            etCategoryDate.setText("");
            return;
        }

        // Add category to database
        boolean isInserted = dbHelper.addCategory(username, name, description, date);

        if (isInserted) {
            Toast.makeText(getActivity(), "Category added successfully", Toast.LENGTH_SHORT).show();
            // Clear the input fields after successful addition
            etCategoryName.setText("");
            etCategoryDescription.setText("");
            etCategoryDate.setText(""); // Clear date field if it's added
            // Go back to the previous fragment
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Failed to add category", Toast.LENGTH_SHORT).show();
        }
    }
}
