package com.example.campus_expensemanager.fragment;


import static android.app.DownloadManager.COLUMN_DESCRIPTION;
import static android.media.tv.TvContract.WatchNextPrograms.COLUMN_TYPE;

import static com.example.campus_expensemanager.database.DatabaseHelper.COLUMN_AMOUNT;
import static com.example.campus_expensemanager.database.DatabaseHelper.COLUMN_CATEGORY;
import static com.example.campus_expensemanager.database.DatabaseHelper.COLUMN_DATE;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.activity.ExpenseAdapter;
import com.example.campus_expensemanager.database.DatabaseHelper;
import com.example.campus_expensemanager.entities.Expense;

import java.util.ArrayList;
import java.util.List;

public class DisplayExpenseFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button btnSearchAmount, btnSearchType, btnSearchDate, btnSearchCategory;
    private ExpenseAdapter adapter;
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_expense, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        btnSearchAmount = view.findViewById(R.id.btn_search_amount);
        btnSearchType = view.findViewById(R.id.btn_search_type);
        btnSearchDate = view.findViewById(R.id.btn_search_date);
        btnSearchCategory = view.findViewById(R.id.btn_search_category);
        // Nhận username từ Bundle nếu có
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.rv_expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Hiển thị chi tiêu của người dùng
        displayUserExpenses(username);

        btnSearchAmount.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (!query.isEmpty()) {
                searchExpenses(COLUMN_AMOUNT, query); // Truyền đúng giá trị vào hàm tìm kiếm
            } else {
                displayUserExpenses(username);
                Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

        btnSearchType.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (!query.isEmpty()) {
                searchExpenses(COLUMN_TYPE, query); // Truyền đúng giá trị vào hàm tìm kiếm
            } else {
                displayUserExpenses(username);
                Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });
        btnSearchDate.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (!query.isEmpty()) {
                searchExpenses(COLUMN_DATE, query); // Truyền đúng giá trị vào hàm tìm kiếm
            } else {
                displayUserExpenses(username);
                Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

        btnSearchCategory.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (!query.isEmpty()) {
                searchExpenses(COLUMN_CATEGORY, query); // Truyền đúng giá trị vào hàm tìm kiếm
            } else {
                displayUserExpenses(username);
                Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Hàm để hiển thị chi tiêu của người dùng từ cơ sở dữ liệu
    private void displayUserExpenses(String username) {
        if (username == null || username.isEmpty()) {
            Toast.makeText(getContext(), "Username is null or empty", Toast.LENGTH_SHORT).show();
            return; // Dừng phương thức nếu username không hợp lệ
        }
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getExpensesByUsername(username);

        if (cursor != null && cursor.moveToFirst()) {
            List<Expense> expenses = new ArrayList<>();
            do {
                // Lấy dữ liệu từ cursor
                @SuppressLint("Range") int amount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));

                // Thêm chi tiêu vào danh sách
                expenses.add(new Expense(amount, description, date, type, category));
            } while (cursor.moveToNext());

            // Thiết lập adapter cho RecyclerView
            adapter = new ExpenseAdapter(getContext(), expenses);
            recyclerView.setAdapter(adapter);
        }
    }
    private void searchExpenses(String column, String query) {
        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.searchExpensesByCriteria(username, column, query);

        if (cursor != null && cursor.moveToFirst()) {
            List<Expense> expenses = new ArrayList<>();
            do {
                @SuppressLint("Range") int amount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));

                expenses.add(new Expense(amount, description, date, type, category));
            } while (cursor.moveToNext());

            // Cập nhật RecyclerView với kết quả tìm kiếm
            adapter.updateData(expenses);
        } else {
            Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
        }
    }
}
