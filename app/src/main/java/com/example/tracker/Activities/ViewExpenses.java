package com.example.tracker.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.example.tracker.Utilities.MyAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class ViewExpenses extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<DataClass> dataList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private SearchView searchView;
    private MyAdapter adapter;
    private String key = "";

    String deletedExpense = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);

        initViews();
        setupRecyclerView();
        setupSearchView();
        setupDatabase();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("Key");
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        // Attach ItemTouchHelper here
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    private void setupDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        final AlertDialog dialog = builder.create();
        dialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Expense Tracker");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    if (dataClass != null) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter after data change
                dialog.dismiss(); // Dismiss dialog after data loading is complete
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
    }

    private void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass : dataList) {
            if (dataClass.getTitle().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.updateList(searchList); // Add this method in your adapter to update the displayed list
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                    Snackbar.make(recyclerView, "Expense Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        DataClass dataClass = new Gson().fromJson(deletedExpense, DataClass.class);
                                        dataList.add(position, dataClass);
                                        adapter.notifyItemInserted(position);
                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).show();

                    // Delete the expense from the database
                    deleteExpenseFromDatabase(expenseKeyToDelete);
                    break;

                case ItemTouchHelper.RIGHT:
                    // Define behavior for right swipe if needed
                    break;
            }
        }

        private void deleteExpenseFromDatabase(String expenseKey) {
            DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference("Expense Tracker").child(expenseKey);
            expenseRef.removeValue();
        }
    };
}
