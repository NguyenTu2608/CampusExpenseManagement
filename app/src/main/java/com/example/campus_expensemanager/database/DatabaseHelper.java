package com.example.campus_expensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Các hằng số cơ sở dữ liệu và tên bảng
    private static final String DATABASE_NAME = "campus_expense_manager.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_EXPENSES = "expenses";

    // Các cột trong bảng
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";


    // SQL để tạo bảng
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_EXPENSES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_AMOUNT + " REAL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            "category TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Thêm cột 'category' vào bảng nếu phiên bản cơ sở dữ liệu cũ hơn 2
            db.execSQL("ALTER TABLE " + TABLE_EXPENSES + " ADD COLUMN category TEXT");
        }
    }

    // Phương thức để thêm chi tiêu vào cơ sở dữ liệu
    public boolean insertExpense(double amount, String description, String date, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put("category", category);  // Thêm loại chi tiêu vào cơ sở dữ liệu

        long result = db.insert(TABLE_EXPENSES, null, values);
        db.close();

        return result != -1; // Trả về true nếu chèn thành công
    }
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, amount, description, date, category FROM " + TABLE_EXPENSES, null);
    }
}
