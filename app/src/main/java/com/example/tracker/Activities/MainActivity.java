package com.example.tracker.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

        fab = findViewById(R.id.fab_add);
        txtViewAll = findViewById(R.id.txtViewAll);
        recyclerView = findViewById(R.id.preview_list);

        if (fab != null) {
            fab.setOnClickListener(v -> openAddExpense());
        }
        if (txtViewAll != null) {
            txtViewAll.setOnClickListener(v -> openViewExpense());
        }

        dbHelper = new DatabaseHelper(this);
        setupRecyclerView();
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

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
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE)); // Assuming this column exists

                DataClass dataClass = new DataClass(title, amount, category, paymentMethod, type);
                dataClass.setKey(String.valueOf(id));
                dataList.add(dataClass);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        adapter.notifyDataSetChanged();
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
            String expenseKeyToDelete = dataList.get(position).getKey();

            if (direction == ItemTouchHelper.LEFT) {
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
                                    reinsertExpenseToDatabase(dataClass, recyclerView);
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).show();

                deleteExpenseFromDatabase(expenseKeyToDelete, recyclerView);
            }
        }
    }

    private void deleteExpenseFromDatabase(String expenseKey, View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=?", new String[]{expenseKey});
            if (rowsDeleted > 0) {
                Snackbar.make(view, "Expense deleted successfully", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, "No expense found with the given key", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Snackbar.make(view, "Failed to delete expense: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private void reinsertExpenseToDatabase(DataClass dataClass, View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TITLE, dataClass.getTitle());
            values.put(DatabaseHelper.COLUMN_AMOUNT, dataClass.getAmount());
            values.put(DatabaseHelper.COLUMN_CATEGORY, dataClass.getCategory());
            values.put(DatabaseHelper.COLUMN_METHOD, dataClass.getPaymentMethod());
            db.insert(DatabaseHelper.TABLE_NAME, null, values);
            Snackbar.make(view, "Expense reinserted successfully", Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Snackbar.make(view, "Failed to reinsert expense: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
