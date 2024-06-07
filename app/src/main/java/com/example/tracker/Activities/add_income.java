package com.example.tracker.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker.R;
import com.example.tracker.Utilities.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class add_income extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner paymentMethodSpinner;
    private MaterialButton submitButton;
    private EditText amountEditText;
    private EditText titleEditText;
    private DatabaseHelper dbHelper;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupCategorySpinner();
        setupPaymentMethodSpinner();
        setupSubmitButton();
        setupListeners();
    }

    private void initViews() {
        categorySpinner = findViewById(R.id.spinnerCategory);
        paymentMethodSpinner = findViewById(R.id.spinMethod);
        submitButton = findViewById(R.id.btnSubmit);
        amountEditText = findViewById(R.id.editTextAmount);
        titleEditText = findViewById(R.id.editIncomeName);

        type = getIntent().getStringExtra("type");
    }

    private void setupCategorySpinner() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Select Category");
        categories.add("Salary");
        categories.add("Business");
        categories.add("Investment");
        categories.add("Gift");
        categories.add("Other");

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
        submitButton.setEnabled(false);  // Initially disable the submit button
    }

    private void setupListeners() {
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validateFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validateFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private boolean validateFields() {
        String title = titleEditText.getText().toString().trim();
        String amount = amountEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();

        boolean isValid = !title.isEmpty() && !amount.isEmpty() && !category.equals("Select Category") && !paymentMethod.equals("Select Payment Method");
        submitButton.setEnabled(isValid);
        return isValid;
    }

    private void uploadData() {
        String title = titleEditText.getText().toString();
        String amount = amountEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();
        String type = "income"; // Since this is the add_income activity, the type is always income
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_METHOD, paymentMethod);
        values.put(DatabaseHelper.COLUMN_TYPE, type); // Save type to database

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
