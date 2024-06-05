package com.example.tracker.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.example.tracker.Utilities.DatabaseHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class add_expense extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner paymentMethodSpinner;
    private Button submitButton;
    private EditText amountEditText;
    private EditText titleEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupCategorySpinner();
        setupPaymentMethodSpinner();
        setupSubmitButton();
    }

    private void initViews() {
        categorySpinner = findViewById(R.id.spinnerCategory);
        paymentMethodSpinner = findViewById(R.id.spinMethod);
        submitButton = findViewById(R.id.btnSubmit);
        amountEditText = findViewById(R.id.editTextAmount);
        titleEditText = findViewById(R.id.editExpenseName);
    }

    private void setupCategorySpinner() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Select Category");
        categories.add("Food and Groceries");
        categories.add("Housing");
        categories.add("Transportation");
        categories.add("Healthcare");
        categories.add("Personal Care");
        categories.add("Entertainment");
        categories.add("Debts");
        categories.add("Education");
        categories.add("Savings");
        categories.add("Gifts and Donations");
        categories.add("Travel");
        categories.add("Insurance");
        categories.add("Miscellaneous");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        categorySpinner.setAdapter(adapter);
    }

    private void setupPaymentMethodSpinner() {
        ArrayList<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("Select Payment Method");
        paymentMethods.add("Cash");
        paymentMethods.add("Credit Card");
        paymentMethods.add("Debit Card");
        paymentMethods.add("Bank Transfer");
        paymentMethods.add("Mobile Payment");
        paymentMethods.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        paymentMethodSpinner.setAdapter(adapter);
    }

    private void setupSubmitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    uploadData();
                } else {
                    showToast("Please fill in all fields.");
                }
            }
        });
    }

    private boolean validateFields() {
        String title = titleEditText.getText().toString().trim();
        String amount = amountEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();

        if (title.isEmpty() || amount.isEmpty() || category.equals("Select Category") || paymentMethod.equals("Select Payment Method")) {
            return false;
        }
        return true;
    }

    private void uploadData() {
        String title = titleEditText.getText().toString();
        String amount = amountEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_METHOD, paymentMethod);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

        if (newRowId != -1) {
            showToast("Data saved successfully");
            openHome();
        } else {
            showToast("Failed to save data");
            Log.e("SQLite", "Error: Failed to insert row");
        }
    }

    private void openHome() {
        String amount = amountEditText.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("amount", amount);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
