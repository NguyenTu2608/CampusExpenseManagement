package com.example.campus_expensemanager.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;

public class DisplayExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ListView expenseListView;

    public DisplayExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_expense, container, false);

        dbHelper = new DatabaseHelper(getContext());
        expenseListView = view.findViewById(R.id.expenseListView);

        displayExpenses();

        return view;
    }

    private void displayExpenses() {
        Cursor cursor = dbHelper.getAllExpenses();

        if (cursor != null && cursor.getCount() > 0) {
            String[] columns = {"description", "amount", "date", "category"};
            int[] views = {R.id.itemDescription, R.id.itemAmount, R.id.itemDate, R.id.itemCategory};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    getContext(),
                    R.layout.expense_list_item,
                    cursor,
                    columns,
                    views,
                    0
            );

            expenseListView.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "No expenses found", Toast.LENGTH_SHORT).show();
        }
    }
}
