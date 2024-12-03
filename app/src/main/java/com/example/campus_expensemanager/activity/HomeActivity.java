package com.example.campus_expensemanager.activity;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;
import com.example.campus_expensemanager.fragment.HomeFragment;
import com.example.campus_expensemanager.fragment.LoginFragment;

public class HomeActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Start with Login Fragment by default
        loadFragment(new HomeFragment());
//        databaseHelper = new DatabaseHelper(this);
//        databaseHelper.deleteDatabase(this);
    }

    // Method to replace the fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
