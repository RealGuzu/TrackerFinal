package com.example.tracker.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tracker.R;
import com.example.tracker.Utilities.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;

public class edit_transaction extends AppCompatActivity {
    private Spinner categorySpinner;

    private Spinner paymentMethodSpinner;
    private Button submitButton;
    private EditText amountEditText;
    private EditText titleEditText;
    private DatabaseHelper dbHelper;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });


    }
    private void initViews() {
        categorySpinner = findViewById(R.id.spinnerCategory);
        paymentMethodSpinner = findViewById(R.id.spinMethod);
        submitButton = findViewById(R.id.btnSubmit);
        amountEditText = findViewById(R.id.editTextAmount);
        titleEditText = findViewById(R.id.editTransactionName);

        type = getIntent().getStringExtra("type");


        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform your desired action here, such as navigating back
                finish();
            }
        });
    }
}