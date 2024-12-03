package com.example.campus_expensemanager.fragment;

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

public class AddCategoryFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText etCategoryName, etCategoryDescription;
    private Button btnAdd;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);


        dbHelper = new DatabaseHelper(getContext());
        // Initialize views
        etCategoryName = view.findViewById(R.id.et_category_name);
        etCategoryDescription = view.findViewById(R.id.et_category_description);
        btnAdd = view.findViewById(R.id.btn_add);

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Set click listener for Add button
        btnAdd.setOnClickListener(v -> addCategory());

        return view;
    }

    private void addCategory() {
        String name = etCategoryName.getText().toString().trim();
        String description = etCategoryDescription.getText().toString().trim();

        // Kiểm tra nếu tên hoặc mô tả bị trống
        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.isEmpty()) {
            Toast.makeText(getActivity(), "Category description cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.addCategory(username, name, description);

        if (isInserted) {
            Toast.makeText(getActivity(), "Category added", Toast.LENGTH_SHORT).show();
            // Xóa dữ liệu trong các ô nhập liệu sau khi thêm thành công
            etCategoryName.setText("");
            etCategoryDescription.setText("");
        } else {
            Toast.makeText(getActivity(), "Failed to add category", Toast.LENGTH_SHORT).show();
        }
    }
}
