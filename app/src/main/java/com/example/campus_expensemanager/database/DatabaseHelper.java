package com.example.campus_expensemanager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.campus_expensemanager.entities.User;

    public class DatabaseHelper extends SQLiteOpenHelper {

        // Các hằng số cơ sở dữ liệu và tên bảng
        private static final String DATABASE_NAME = "campus_expense_manager.db";
        private static final int DATABASE_VERSION = 6;

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
                        COLUMN_USERNAME + " TEXT); ";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_EXPENSES);
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_CATEGORIES);
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
                Log.d("DatabaseHelper", "Upgrading from version 5 to 6 - Dropping and recreating tables");
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
                onCreate(db); // Tạo lại tất cả các bảng với cấu trúc mới
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

        public boolean addCategory(String username, String name, String description) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_CATEGORY_NAME, name);
            values.put(COLUMN_DESCRIPTION, description);
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
    public Cursor getCurrentUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Cập nhật truy vấn để tìm người dùng đang đăng nhập
        String query = "SELECT username FROM " + TABLE_USERS + " WHERE is_logged_in = 1";
        return db.rawQuery(query, null);
    }

    public void checkTableStructure() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_CATEGORIES + ")", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String columnName = cursor.getString(cursor.getColumnIndex("name"));
                    Log.d("DatabaseInfo", "Column name: " + columnName);
                }
                cursor.close();
            }
        }
    }

