package com.example.campus_expensemanager.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AddExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;
    private Spinner categorySpinner;
    private String selectedCategory;
    private Spinner typeSpinner;
    private String selectedType;
    private String username;
    private Calendar calendar;

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
        calendar = Calendar.getInstance();


        if (getArguments() != null) {
            username = getArguments().getString("username");
            loadCategoriesForUser(username);
        }

        Button addButton = view.findViewById(R.id.addButton);
        Button btnDisplay = view.findViewById(R.id.btnDisplay);

        setupDatePicker(dateEditText);



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
                selectedType = "Expense"; // Giá trị mặc định nếu không chọn gì
            }
        });


        // Add expense action
        addButton.setOnClickListener(v -> addExpense());

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

    private void loadCategoriesForUser(String username) {
        List<String> categories = dbHelper.getCategoriesByUser(username);
        if (categories.isEmpty()) {
//            categories.add("Other"); // Nếu không có Category nào, thêm "Other" làm mặc định
        }


        // Tạo adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Gán sự kiện chọn category
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedCategory = "Other"; // Giá trị mặc định nếu không chọn gì
            }
        });
    }


    private void addExpense() {
        if (categorySpinner.getAdapter() == null || categorySpinner.getCount() == 0) {
            Toast.makeText(getContext(), "Please add a category before adding expenses.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double amount = Double.parseDouble(amountEditText.getText().toString().trim());
            String description = descriptionEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            if (amount <= 0 || description.isEmpty() || date.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.addExpense(username, amount, description, date, selectedCategory, selectedType);
            if (inserted) {
                boolean updated = dbHelper.updateCategoryBalance(username,selectedCategory, selectedType, amount);
                if (updated) {
                    Toast.makeText(getContext(), "Expense added and category balance updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error updating category balance", Toast.LENGTH_SHORT).show();
                }

                // Xóa dữ liệu sau khi thêm thành công
                amountEditText.setText("");
                descriptionEditText.setText("");
                dateEditText.setText("");
                categorySpinner.setSelection(0);
                typeSpinner.setSelection(0);
            } else {
                Toast.makeText(getContext(), "Error adding expense", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }

}
