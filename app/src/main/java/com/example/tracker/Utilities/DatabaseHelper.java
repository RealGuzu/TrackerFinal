package com.example.tracker.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_TYPE = "type" ;
    // Define the table and column names
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "expenses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_METHOD = "method"; // Changed to match the column in onCreate

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_AMOUNT + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_METHOD + " TEXT,"
                + COLUMN_TYPE + "TEXT)";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to update an expense in the database
    public boolean updateExpense(DataClass dataClass, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, dataClass.getTitle());
        values.put(COLUMN_AMOUNT, dataClass.getAmount());
        values.put(COLUMN_CATEGORY, dataClass.getCategory());
        values.put(COLUMN_METHOD, dataClass.getPaymentMethod()); // Ensure this matches the getter in DataClass
        values.put(COLUMN_METHOD, dataClass.getType());
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public List<DataClass> getAllExpenses() {
        List<DataClass> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DataClass dataClass = new DataClass();
                dataClass.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                dataClass.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                dataClass.setAmount(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)));
                dataClass.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                dataClass.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_METHOD)));
                dataClass.setKey(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                expenses.add(dataClass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

