package com.example.campus_expensemanager.activity;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.campus_expensemanager.R;
import com.example.campus_expensemanager.database.DatabaseHelper;
import com.example.campus_expensemanager.fragment.AddExpenseFragment;
import com.example.campus_expensemanager.fragment.HomeFragment;
import com.example.campus_expensemanager.fragment.LoginFragment;


public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.checkTableStructure();
        if (isUserLoggedIn()) {
            // Người dùng đã đăng nhập
            String username = getLoggedInUsername();
            Toast.makeText(this, "Welcomeback , " + username, Toast.LENGTH_SHORT).show();

            // Tải HomeFragment hoặc màn hình chính
            loadFragment(new HomeFragment());
        } else {
            // Người dùng chưa đăng nhập
            loadFragment(new LoginFragment());
        }
    }

    // Method to replace the fragment
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false); // Trả về false nếu chưa đăng nhập
    }
    private String getLoggedInUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getString("username", ""); // Trả về username nếu có, hoặc chuỗi rỗng
    }

}
