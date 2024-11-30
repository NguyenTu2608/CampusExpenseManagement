package com.example.campus_expensemanager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campus_expensemanager.entities.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Các hằng số cơ sở dữ liệu và tên bảng
    private static final String DATABASE_NAME = "campus_expense_manager.db";
    private static final int DATABASE_VERSION = 2;

    // Bảng expenses
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CATEGORY = "category";

    // Bảng users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_FULL_NAME = "fullName";
    private static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";

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
            "is_logged_in INTEGER DEFAULT 0" + ")";

    private static final String CREATE_TABLE_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CATEGORY_DESCRIPTION + " TEXT);";

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
        if (oldVersion < 2) {  // Cập nhật số phiên bản để thêm cột is_logged_in
            // Kiểm tra xem cột is_logged_in có tồn tại không, nếu không thì thêm vào
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN is_logged_in INTEGER DEFAULT 0");
        }
        // Tiến hành tạo lại các bảng
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);  // Tạo lại tất cả các bảng
    }
    // Phương thức thêm chi tiêu vào cơ sở dữ liệu
    public boolean insertExpense(double amount, String description, String date, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_CATEGORY, category);

        long result = db.insert(TABLE_EXPENSES, null, values);
        db.close();

        return result != -1; // Trả về true nếu chèn thành công
    }

    // Phương thức thêm người dùng vào cơ sở dữ liệu
    public boolean insertUser(String fullName, String email, String username, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FULL_NAME, email);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_PHONE, phone);;

        long result = db.insert(TABLE_USERS, null, contentValues);
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


    // Phương thức lấy danh sách chi tiêu
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, amount, description, date, category FROM " + TABLE_EXPENSES, null);
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
    public void setLoggedIn(String username, boolean isLoggedIn) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Đặt tất cả các tài khoản về không đăng nhập
        if (isLoggedIn) {
            ContentValues resetValues = new ContentValues();
            resetValues.put("is_logged_in", 0);
            db.update(TABLE_USERS, resetValues, null, null);
        }

        // Cập nhật trạng thái cho tài khoản hiện tại
        ContentValues values = new ContentValues();
        values.put("is_logged_in", isLoggedIn ? 1 : 0);
        db.update(TABLE_USERS, values, "username = ?", new String[]{username});

        db.close();
    }
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return isValid;
    }
    public String getLoggedInUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username FROM " + TABLE_USERS + " WHERE is_logged_in = 1";
        Cursor cursor = db.rawQuery(query, null);

        String loggedInUser = null;
        if (cursor.moveToFirst()) {
            loggedInUser = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return loggedInUser;
    }
}
