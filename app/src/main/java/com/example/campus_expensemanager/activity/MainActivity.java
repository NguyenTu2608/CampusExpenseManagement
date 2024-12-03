package com.example.campus_expensemanager.activity;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;


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
        // Start with Login Fragment by default
        loadFragment(new LoginFragment());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.checkTableStructure();
    }

    // Method to replace the fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void checkTableStructure() {
        // Lấy một đối tượng SQLiteDatabase từ DatabaseHelper
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Thực thi câu lệnh PRAGMA table_info để lấy thông tin về các cột trong bảng Categories
        Cursor cursor = db.rawQuery("PRAGMA table_info(Categories);", null);

        // Kiểm tra nếu có dữ liệu trả về từ truy vấn
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Lấy tên cột
                @SuppressLint("Range") String columnName = cursor.getString(cursor.getColumnIndex("name"));
                // Lấy kiểu dữ liệu của cột
                @SuppressLint("Range") String columnType = cursor.getString(cursor.getColumnIndex("type"));
                // Log thông tin của cột ra Logcat
                Log.d("TableInfo", "Column Name: " + columnName + ", Type: " + columnType);
            }
            cursor.close();  // Đóng con trỏ sau khi sử dụng
        } else {
            Log.d("TableInfo", "No columns found in Categories table.");
        }
    }



}
