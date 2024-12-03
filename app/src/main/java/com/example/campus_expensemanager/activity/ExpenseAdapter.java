package com.example.campus_expensemanager.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.entities.Expense;

import java.util.ArrayList;
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
        String descriptionText = "Description: " + expense.getDescription();
        String dateText = "Date: " + expense.getDate();
        String typeText = "Type: " + expense.getType();
        String categoryText = "Category: " + expense.getCategory();



        int amount = expense.getAmount();
        if (expense.getType().equals("Income")) {
            // Nếu là income (tiền vào), màu xanh lá cây
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        } else {
            // Nếu là expense (tiền chi tiêu), màu đỏ
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        }

        holder.description.setText(descriptionText);
        holder.amount.setText(String.format("Amount : %,d", amount));
        holder.date.setText(dateText);
        holder.type.setText(typeText);
        holder.category.setText(categoryText);


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
//    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
//        TextView categoryName;
//
//        public CategoryViewHolder(@NonNull View itemView) {
//            super(itemView);
//            categoryName = itemView.findViewById(R.id.expense_name);   }
//    }
//
//    public void updateExpenses(List<Expense> newExpenses) {
//        this.expenses.clear();
//        this.expenses.addAll(newExpenses);
//        notifyDataSetChanged();
//    }


}


