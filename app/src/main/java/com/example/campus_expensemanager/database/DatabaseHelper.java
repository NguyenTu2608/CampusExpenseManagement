package com.example.campus_expensemanager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.campus_expensemanager.entities.Category;
import com.example.campus_expensemanager.entities.Expense;
import com.example.campus_expensemanager.entities.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

        // Các hằng số cơ sở dữ liệu và tên bảng
        public static final String DATABASE_NAME = "campus_expense_manager1.db";
        private static final int DATABASE_VERSION = 9;

        // Bảng expenses
        private static final String TABLE_EXPENSES = "expenses";
        private static final String COLUMN_ID = "id";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_CATEGORY = "category";

        // Bảng users
        private static final String TABLE_USERS = "users";
        private static final String COLUMN_USER_ID = "id";
        public static final String COLUMN_FULL_NAME = "fullName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_BALANCE = "balance";

        // Bảng Categories
        private static final String TABLE_CATEGORIES = "Categories";
        private static final String COLUMN_CATEGORY_ID = "id";
        private static final String COLUMN_CATEGORY_NAME = "name";
        private static final String COLUMN_CATEGORY_DESCRIPTION = "description";
        private static final String COLUMN_DATE_CREATED = "date";

        // bang spending limit
        private static final String TABLE_SPENDING_LIMITS = "SpendingLimit";
        private static final String COLUMN_SPENDING_LIMIT_ID = "id";
        public static final String COLUMN_SPENDING_LIMIT_DESCRIPTION = "description";
        public static final String COLUMN_SPENDING_LIMIT_AMOUNT = "amount";
        public static final String COLUMN_SPENDING_START_DATE = "startDate";
        public static final String COLUMN_SPENDING_END_DATE = "endDate";

        // SQL để tạo bảng expenses
        private static final String CREATE_TABLE_EXPENSES =
                "CREATE TABLE " + TABLE_EXPENSES + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_AMOUNT + " REAL, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_USERNAME + " TEXT, " +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_CATEGORY + " TEXT, " +
                        COLUMN_TYPE + " TEXT);";


        // SQL để tạo bảng users
        private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                "is_logged_in INTEGER DEFAULT 0, " +
                COLUMN_BALANCE + " REAL DEFAULT 0" + ");";

        private static final String CREATE_TABLE_CATEGORIES =
                "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                        COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                        COLUMN_CATEGORY_DESCRIPTION + " TEXT, " +
                        COLUMN_DATE_CREATED + " TEXT, " +
                        COLUMN_BALANCE + " REAL DEFAULT 0," +
                        COLUMN_USERNAME + " TEXT); ";

        private static final String CREATE_TABLE_SPENDING_LIMIT =
                "CREATE TABLE " + TABLE_SPENDING_LIMITS + " (" +
                        COLUMN_SPENDING_LIMIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SPENDING_LIMIT_DESCRIPTION + " TEXT, " +
                        COLUMN_SPENDING_LIMIT_AMOUNT + " TEXT, " +
                        COLUMN_SPENDING_START_DATE + " TEXT, " +
                        COLUMN_SPENDING_END_DATE + " TEXT, " +
                        COLUMN_USERNAME + " TEXT); ";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_EXPENSES);
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_CATEGORIES);
            db.execSQL(CREATE_TABLE_SPENDING_LIMIT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("DatabaseHelper", "onUpgrade - oldVersion: " + oldVersion + " newVersion: " + newVersion);

            if (oldVersion < 2) {
                Log.d("DatabaseHelper", "Upgrading from version 1 to 2 - Adding is_logged_in column");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN is_logged_in INTEGER DEFAULT 0");
            }
            if (oldVersion < 3) {
                Log.d("DatabaseHelper", "Upgrading from version 2 to 3 - Adding username column in expenses table");
                db.execSQL("ALTER TABLE " + TABLE_EXPENSES + " ADD COLUMN " + COLUMN_USERNAME + " TEXT;");
            }
            if (oldVersion < 4) {
                Log.d("DatabaseHelper", "Upgrading from version 3 to 4 - Adding balance column");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_BALANCE + " REAL DEFAULT 0");
            }
            if (oldVersion < 5) {
                Log.d("DatabaseHelper", "Upgrading from version 4 to 5 - Adding username column in categories table");
                db.execSQL("ALTER TABLE " + TABLE_CATEGORIES + " ADD COLUMN " + COLUMN_USERNAME + " TEXT;");
            }
            if (oldVersion < 6) {
                Log.d("DatabaseHelper", "Upgrading from version 4 to 5 - Adding username column in categories table");
                db.execSQL("ALTER TABLE " + TABLE_CATEGORIES + " ADD COLUMN " + COLUMN_DATE_CREATED + " TEXT;");
            }
            if(oldVersion < 7)
            {
                db.execSQL("ALTER TABLE " + TABLE_CATEGORIES + " ADD COLUMN " + COLUMN_BALANCE + " REAL DEFAULT 0");
            }
            if (oldVersion < 8) {
                Log.d("DatabaseHelper", "Upgrading from version 5 to 6 - Dropping and recreating tables");
                db.execSQL("ALTER TABLE " + TABLE_SPENDING_LIMITS + " ADD COLUMN " + COLUMN_USERNAME + " TEXT;");
            }
            if(oldVersion < 9)
            {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            }
        }
    // Phương thức thêm chi tiêu vào cơ sở dữ liệu
    public boolean addExpense(String username, double amount, String description, String date, String category, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_TYPE, type);

        boolean isIncome = type.equalsIgnoreCase("Income");
        boolean expenseInserted = db.insert("expenses", null, values) != -1;

        if (expenseInserted) {
            return updateBalance(username, amount, isIncome);
        }
        return false;
    }

    public boolean updateBalance(String username, double amount, boolean isIncome) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_BALANCE + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") double currentBalance = cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE));
            cursor.close();

            double updatedBalance = isIncome ? currentBalance + amount : currentBalance - amount;

            ContentValues values = new ContentValues();
            values.put(COLUMN_BALANCE, updatedBalance);
            int result = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
            return result > 0;
        }
        return false;
    }

        public Cursor getExpensesByUsername(String username) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_USERNAME + " = ?";
            return db.rawQuery(query, new String[]{username});
        }

    // Phương thức thêm người dùng vào cơ sở dữ liệu
    public boolean insertUser(String fullName, String email, String username, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FULL_NAME, fullName);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_PHONE, phone);;

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1; // Trả về true nếu thêm thành công
    }

    public boolean addCategory(String username, String name, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE_CREATED, date);  // Thêm giá trị ngày vào ContentValues

        long result = db.insert("categories", null, values);
        return result != -1; // Trả về true nếu thêm thành công
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
    public Cursor getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;  // Nếu username là null hoặc rỗng, không thực hiện truy vấn.
        }

        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn để lấy thông tin người dùng từ bảng users
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        return cursor;
    }

    public List<String> getCategoriesByUser(String username) {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn danh sách category theo username
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATEGORY_NAME +
                        " FROM " + TABLE_CATEGORIES +
                        " WHERE " + COLUMN_USERNAME + " = ?",
                new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0)); // Lấy giá trị từ cột COLUMN_CATEGORY_NAME
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }
    public boolean isCategoryExists(String username, String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_CATEGORIES +
                " WHERE " + COLUMN_CATEGORY_NAME + " = ? AND " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryName, username});
        boolean exists = (cursor.getCount() > 0); // Nếu có kết quả, danh mục đã tồn tại
        cursor.close();
        return exists;
    }

    public boolean updateCategoryBalance(String username,String categoryName, String type, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Lấy balance hiện tại của category
        String query = "SELECT " + COLUMN_BALANCE + " FROM " + TABLE_CATEGORIES +
                " WHERE " + COLUMN_CATEGORY_NAME + " = ? AND " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryName, username});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") double currentBalance = cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE));
            cursor.close();

            // Tính toán balance mới
            double newBalance;
            if (type.equalsIgnoreCase("Income")) {
                newBalance = currentBalance + amount;
            } else { // Expense
                newBalance = currentBalance - amount;
            }

            // Cập nhật balance trong bảng categories
            ContentValues values = new ContentValues();
            values.put(COLUMN_BALANCE, newBalance);

            int rowsUpdated = db.update(TABLE_CATEGORIES, values,
                    COLUMN_CATEGORY_NAME + " = ? AND " + COLUMN_USERNAME + " = ?",
                    new String[]{categoryName, username});

            return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
        }

        return false; // Trả về false nếu không tìm thấy category
    }

        public boolean updatePassword(String username, String newPassword) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PASSWORD, newPassword);

            int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
            db.close();

            return rowsAffected > 0;
        }

    // Phương thức lấy danh sách người dùng
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, email, username, phone FROM " + TABLE_USERS, null);
    }
    public List<Category> getCategoriesByUsername(String username) {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories WHERE username = ?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE));
                @SuppressLint("Range") String dateCreated = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_CREATED));

                categoryList.add(new Category(name, amount, dateCreated));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return categoryList;
    }



    public boolean insertSpendingLimit(String description, String amount, String startDate, String endDate, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SPENDING_LIMIT_DESCRIPTION, description);
        values.put(COLUMN_SPENDING_LIMIT_AMOUNT, amount);
        values.put(COLUMN_SPENDING_START_DATE, startDate);
        values.put(COLUMN_SPENDING_END_DATE, endDate);
        values.put(COLUMN_USERNAME, username);

        // Insert the new row and return the row ID
        long result = db.insert("SpendingLimit", null, values);
        return result != -1;
    }

    public Cursor getSpendingLimitByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SPENDING_LIMITS + " WHERE " + COLUMN_USERNAME + " = ?";
        return db.rawQuery(query, new String[]{username});
    }
    @SuppressLint("Range")
    public double getNetBudgetInRange(String username, String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Tính tổng Income
        String incomeQuery = "SELECT SUM(amount) AS total FROM expenses WHERE username = ? AND type = 'Income' AND date >= ? AND date <= ?";
        Cursor incomeCursor = db.rawQuery(incomeQuery, new String[]{username, startDate, endDate});
        double totalIncome = 0.0;

        if (incomeCursor != null && incomeCursor.moveToFirst()) {
            totalIncome = incomeCursor.getDouble(incomeCursor.getColumnIndex("total"));
            incomeCursor.close();
        }

        // Tính tổng Expense
        String expenseQuery = "SELECT SUM(amount) AS total FROM expenses WHERE username = ? AND type = 'Expense' AND date >= ? AND date <= ?";
        Cursor expenseCursor = db.rawQuery(expenseQuery, new String[]{username, startDate, endDate});
        double totalExpense = 0.0;

        if (expenseCursor != null && expenseCursor.moveToFirst()) {
            totalExpense = expenseCursor.getDouble(expenseCursor.getColumnIndex("total"));
            expenseCursor.close();
        }

        // Ngân sách thực tế = Tổng Income - Tổng Expense
        return totalIncome - totalExpense;
    }

    public void checkTableStructure() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_SPENDING_LIMITS + ")", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String columnName = cursor.getString(cursor.getColumnIndex("name"));
                    Log.d("DatabaseInfo", "Column name: " + columnName);
                }
                cursor.close();
            }
        }
    public void deleteDatabase(Context context) {
        context.deleteDatabase("campus_expense_manager.db");
    }
    }




