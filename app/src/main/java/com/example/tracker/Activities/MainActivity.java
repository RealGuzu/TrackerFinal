package com.example.tracker.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.Fragments.FullscreenDialogFragment;
import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.example.tracker.Utilities.DatabaseHelper;
import com.example.tracker.Utilities.MyAdapter;
import com.example.tracker.Utilities.onTransactionCLick;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements onTransactionCLick, FullscreenDialogFragment.FullscreenDialogListener  {

    private ExtendedFloatingActionButton fab;
    private FloatingActionButton fabAddExpense;
    private FloatingActionButton fabAddIncome;
    private TextView addExpenseText;
    private TextView addIncomeText;
    private TextView txtTotal;

    TextView txtViewAll, txtAddExpense, txtAddIncome;
    Boolean isAllAvailable;
    private MyAdapter adapter;
    private List<DataClass> dataList;
    private DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    private String deletedExpense;
    private int pos;
    private TextView totalexpense, totalincome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initVews();
        setupRecyclerView();
        loadDataFromDatabase();
        updateExpense();
        updateIncome();
        // Initially hide the extended FAB options
        hideFabMenu();


    }

    private void initVews() {
        // Initialize UI components
        totalexpense = findViewById(R.id.txtExpenseTotal);
        totalincome = findViewById(R.id.incomeTotal);
        fab = findViewById(R.id.fab);
        fabAddExpense = findViewById(R.id.fabAddExpense);
        fabAddIncome = findViewById(R.id.fabAddIncome);
        addExpenseText = findViewById(R.id.addExpenseText);
        addIncomeText = findViewById(R.id.addIncomeText);
        txtViewAll = findViewById(R.id.txtViewAll);
        recyclerView = findViewById(R.id.preview_list);
        txtTotal = findViewById(R.id.txt_total);

        // Set click listeners
        if (fab != null) {
            fab.setOnClickListener(v -> toggleFabMenu());
        }
        if (fabAddExpense != null) {
            fabAddExpense.setOnClickListener(v -> openAddExpense());
        }
        if (fabAddIncome != null) {
            fabAddIncome.setOnClickListener(v -> openAddIncome());
        }
        if (txtViewAll != null) {
            txtViewAll.setOnClickListener(v -> openViewExpense());
        }

        dbHelper = new DatabaseHelper(this);
    }

    private void toggleFabMenu() {
        if (fabAddExpense.getVisibility() == View.VISIBLE) {
            hideFabMenu();
        } else {
            showFabMenu();
        }
    }

    private void hideFabMenu() {
        animateFab(fabAddExpense, View.GONE);
        animateFab(fabAddIncome, View.GONE);
        animateFabText(addExpenseText, View.GONE);
        animateFabText(addIncomeText, View.GONE);
    }

    private void showFabMenu() {
        animateFab(fabAddExpense, View.VISIBLE);
        animateFab(fabAddIncome, View.VISIBLE);
        animateFabText(addExpenseText, View.VISIBLE);
        animateFabText(addIncomeText, View.VISIBLE);
    }

    private void animateFab(final View fab, final int visibility) {
        float translationY = visibility == View.VISIBLE ? 0f : fab.getHeight() + getResources().getDimension(R.dimen.fab_margin);
        ObjectAnimator animator = ObjectAnimator.ofFloat(fab, "translationY", translationY);

        if (visibility == View.VISIBLE) {
            fab.setVisibility(View.VISIBLE);
            fab.setAlpha(0f);
            fab.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .setListener(null)
                    .start();
        } else {
            fab.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fab.setVisibility(visibility);
                        }
                    }).start();
        }

        animator.setDuration(200);
        animator.start();
    }

    private void animateFabText(final View textView, final int visibility) {
        float translationY = visibility == View.VISIBLE ? 0f : textView.getHeight() + getResources().getDimension(R.dimen.fab_margin);
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "translationY", translationY);

        if (visibility == View.VISIBLE) {
            textView.setVisibility(View.VISIBLE);
            textView.setAlpha(0f);
            textView.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .setListener(null)
                    .start();
        } else {
            textView.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            textView.setVisibility(visibility);
                        }
                    }).start();
        }

        animator.setDuration(200);
        animator.start();
    }

    private void openViewExpense() {
        Intent intent = new Intent(this, ViewExpenses.class);
        startActivity(intent);
    }

    private void openAddExpense() {
        Intent intent = new Intent(this, add_expense.class);
        intent.putExtra("type", "expense"); // Pass type as expense
        startActivity(intent);
    }

    private void openAddIncome() {
        Intent intent = new Intent(this, add_income.class);
        intent.putExtra("type", "expense"); // Pass type as expense
        startActivity(intent);
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(this, dataList, this);
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
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE)); // Get type

                DataClass dataClass = new DataClass(title, amount, category, paymentMethod, type); // Include type
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


    @Override
    public void onTransactionItemClick(int position) {
        pos = position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FullscreenDialogFragment dialogFragment = new FullscreenDialogFragment(); // Corrected variable name
        dialogFragment.show(fragmentManager, "dialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        ArrayList<DataClass> dataList = new ArrayList<>();
        Bundle args = new Bundle();
        args.putParcelableArrayList("dataList", dataList);
        args.putInt("position", pos);
        dialog.setArguments(args); // Updated method call to set arguments
        if (dialog instanceof FullscreenDialogFragment) {
            ((FullscreenDialogFragment) dialog).updateContent(); // Call updateContent method
        }
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

    private void updateExpense() {
        double totalExpense = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            DataClass data = dataList.get(i);
            if ("expense".equals(data.getType())) {
                totalExpense += Double.parseDouble(data.getAmount());
            }
        }
        Currency usd = Currency.getInstance("USD");
        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedExpense = usdFormat.format(totalExpense);
        totalexpense.setText(formattedExpense);
    }


    private void updateIncome() {
        double totalIncome = 0;
        double totalExpense = 0;

        // Calculate total expense
        for (int i = 0; i < adapter.getItemCount(); i++) {
            DataClass data = dataList.get(i);
            if ("expense".equals(data.getType())) {
                totalExpense += Double.parseDouble(data.getAmount());
            }
        }

        // Calculate total income
        for (int i = 0; i < adapter.getItemCount(); i++) {
            DataClass data = dataList.get(i);
            if ("income".equals(data.getType())) {
                totalIncome += Double.parseDouble(data.getAmount());
            }
        }

        // Deduct total expense from total income
        totalIncome -= totalExpense;

        Currency usd = Currency.getInstance("USD");
        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedIncome = usdFormat.format(totalIncome);
        totalincome.setText(formattedIncome);
        txtTotal.setText(formattedIncome);
    }


}