package com.example.tracker.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.example.tracker.Utilities.DatabaseHelper;
import com.example.tracker.Utilities.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FloatingActionButton fab;
    TextView txtViewAll;
    private MyAdapter adapter;
    private List<DataClass> dataList;
    private DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    private String deletedExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        fab = findViewById(R.id.fab_add);
        txtViewAll = findViewById(R.id.txtViewAll);
        recyclerView = findViewById(R.id.preview_list);

        // Check for null views
        if (fab == null) {
            Log.e(TAG, "FloatingActionButton is null");
        }
        if (txtViewAll == null) {
            Log.e(TAG, "TextView is null");
        }
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView is null");
        }

        // Set listeners
        if (fab != null) {
            fab.setOnClickListener(v -> openAddExpense());
        }
        if (txtViewAll != null) {
            txtViewAll.setOnClickListener(v -> openViewExpense());
        }

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        setupRecyclerView();

        // Load data from SQLite database
        loadDataFromDatabase();
    }

    private void openViewExpense() {
        Intent intent = new Intent(this, ViewExpenses.class);
        startActivity(intent);
    }

    private void openAddExpense() {
        Intent intent = new Intent(this, add_expense.class);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView is null in setupRecyclerView");
            return;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        // Attach ItemTouchHelper here
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadDataFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
            dataList.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY));
                String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_METHOD));

                // Log data to ensure it is being retrieved correctly
                Log.d(TAG, "Data retrieved - ID: " + id + ", Title: " + title + ", Amount: " + amount +
                        ", Category: " + category + ", Payment Method: " + paymentMethod);

                // Check for null values
                if (title == null || amount == null || category == null || paymentMethod == null) {
                    Log.e(TAG, "Null values found in database for ID: " + id);
                }

                DataClass dataClass = new DataClass(title, amount, category, paymentMethod);
                dataClass.setKey(String.valueOf(id));
                dataList.add(dataClass);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        adapter.notifyDataSetChanged(); // Notify adapter after data change
    }

    private class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
        private MyAdapter adapter;

        public ItemTouchHelperCallback(MyAdapter adapter) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            String expenseKeyToDelete = dataList.get(position).getKey(); // Get the key of the expense to be deleted

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedExpense = new Gson().toJson(dataList.get(position));
                    dataList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, "Expense deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        DataClass dataClass = new Gson().fromJson(deletedExpense, DataClass.class);
                                        dataList.add(position, dataClass);
                                        adapter.notifyItemInserted(position);
                                        // Reinsert into database
                                        reinsertExpenseToDatabase(dataClass);
                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    // Delete the expense from the database
                    deleteExpenseFromDatabase(expenseKeyToDelete);
                    break;

                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    }

    private void deleteExpenseFromDatabase(String expenseKey) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=?", new String[]{expenseKey});
    }

    private void reinsertExpenseToDatabase(DataClass dataClass) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, dataClass.getTitle());
        values.put(DatabaseHelper.COLUMN_AMOUNT, dataClass.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, dataClass.getCategory());
        values.put(DatabaseHelper.COLUMN_METHOD, dataClass.getPaymentMethod());
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
    }
}
