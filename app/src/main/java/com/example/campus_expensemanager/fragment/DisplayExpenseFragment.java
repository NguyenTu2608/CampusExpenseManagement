package com.example.campus_expensemanager.fragment;


import static android.app.DownloadManager.COLUMN_DESCRIPTION;
import static android.media.tv.TvContract.WatchNextPrograms.COLUMN_TYPE;

import static com.example.campus_expensemanager.database.DatabaseHelper.COLUMN_AMOUNT;
import static com.example.campus_expensemanager.database.DatabaseHelper.COLUMN_CATEGORY;
import static com.example.campus_expensemanager.database.DatabaseHelper.COLUMN_DATE;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ExpenseAdapter adapter;
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_expense, container, false);

        recyclerView = view.findViewById(R.id.rv_expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy username từ Bundle hoặc SharedPreferences
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Lấy danh sách chi phí của người dùng và hiển thị
        displayUserExpenses(username);

        return view;
    }

    private void displayUserExpenses(String username) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getExpensesByUsername(username);

        if (cursor != null && cursor.moveToFirst()) {
            List<Expense> expenses = new ArrayList<>();
            do {
                @SuppressLint("Range") int amount = cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                expenses.add(new Expense(amount, description, date, type, category));
            } while (cursor.moveToNext());

            // Thiết lập adapter cho RecyclerView
            adapter = new ExpenseAdapter(expenses);
            recyclerView.setAdapter(adapter);
        }
    }
}
