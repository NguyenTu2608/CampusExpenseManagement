package com.example.campus_expensemanager.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private List<Category> filteredList;


    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.filteredList = new ArrayList<>(categoryList);
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged(); // Notify the adapter to update the RecyclerView
    }

//    public void filter(String category, String type, String amount, String date) {
//        filteredList.clear();
//        for (Category item : categoryList) {
//            boolean matchesCategory = category.isEmpty() || item.getName().toLowerCase().contains(category.toLowerCase());
//            boolean matchesType = type.isEmpty() || item.getType().toLowerCase().contains(type.toLowerCase());
//            boolean matchesAmount = amount.isEmpty() || String.valueOf(item.getAmount()).equals(amount);
//            boolean matchesDate = date.isEmpty() || item.getDate().equals(date);
//
//            if (matchesCategory &&  matchesAmount) {
//                filteredList.add(item);
//            }
//        }
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Get the category at the current position
        Category category = categoryList.get(position);

        // Set the category name, amount, and date in the respective TextViews
        holder.itemName.setText(category.getName()); // Assuming 'getName()' returns the category name
        holder.itemAmount.setText("Amount: " + category.getAmount()); // Assuming 'getAmount()' returns the amount
        holder.itemDate.setText(category.getDateCreated()); // Assuming 'getDate()' returns the date
        if (category.getAmount() < 0) {
            // If amount is negative, set the expense image (for example, a red or negative icon)
            holder.itemImage.setImageResource(R.drawable.ic_expense);
        } else {
            // If amount is positive, set the income image (for example, a green or positive icon)
            holder.itemImage.setImageResource(R.drawable.item7);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        // Declare the views
        TextView itemName, itemAmount, itemDate;
        ImageView itemImage; // You can add an ImageView to display an icon for the category if needed

        public CategoryViewHolder(View itemView) {
            super(itemView);
            // Initialize the views
            itemName = itemView.findViewById(R.id.item_Title);
            itemAmount = itemView.findViewById(R.id.item_amount_display);
            itemDate = itemView.findViewById(R.id.item_time);
            itemImage = itemView.findViewById(R.id.ic_expense);
        }
    }
}

