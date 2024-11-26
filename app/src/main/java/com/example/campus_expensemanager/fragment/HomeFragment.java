package com.example.campus_expensemanager.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public  HomeFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");

        // Set "Hello " + username in the TextView

        // Lay ve thong tin mat khau va email
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null); // Retrieve the hashed/encrypted version
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//
//        // Set LayoutManager for RecyclerView (Linear Layout)
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Initialize item list
//        List<Item> itemList = new ArrayList<>();
//
//        // Add data to the list (image resource + text)
//        itemList.add(new Item(R.drawable.item1, "Item 1"));
//        itemList.add(new Item(R.drawable.item2, "Item 2"));
//        itemList.add(new Item(R.drawable.item3, "Item 3"));
//
//        // Add data to the list (image resource + text)
//        itemList.add(new Item(R.drawable.item4, "Item 4"));
//        itemList.add(new Item(R.drawable.item5, "Item 5"));
//        itemList.add(new Item(R.drawable.item6, "Item 6"));
//
//        // Initialize the adapter and set it to the RecyclerView
//        ItemAdapter itemAdapter = new ItemAdapter(getContext(), itemList);
//        recyclerView.setAdapter(itemAdapter);
//
//
        Button btnAddExpense = view.findViewById(R.id.btn_add_expense);
//        Button btnDisplayAll = view.findViewById(R.id.btnDisplay);
//
//
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new AddExpenseFragment());

            }
        });
//
//        btnDisplayAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                loadFragment(new DisplayExpenseFragment());
//
//            }
//        });


        return  view;

    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
