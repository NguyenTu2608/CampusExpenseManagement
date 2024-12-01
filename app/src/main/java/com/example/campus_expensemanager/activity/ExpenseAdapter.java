package com.example.campus_expensemanager.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.entities.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenses;

    public ExpenseAdapter(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.amount.setText(String.valueOf(expense.getAmount()));
        holder.description.setText(expense.getDescription());
        holder.date.setText(expense.getDate());
        holder.type.setText(expense.getType());
        holder.category.setText(expense.getCategory());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView amount, description, date, type, category;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.tv_amount);
            description = itemView.findViewById(R.id.tv_description);
            date = itemView.findViewById(R.id.tv_date);
            type = itemView.findViewById(R.id.tv_type);
            category = itemView.findViewById(R.id.tv_category);
        }
    }
}


