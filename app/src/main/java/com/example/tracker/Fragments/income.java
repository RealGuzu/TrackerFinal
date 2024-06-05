package com.example.tracker.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracker.Activities.MainActivity;
import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.example.tracker.Utilities.DatabaseHelper;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class income extends Fragment {

    private static final String TAG = "ExpenseFragment";

    Spinner spinCategory, spinMethod;
    EditText inputAmount, inputTitle;
    TextView displayCategory, displayMethod;
    String title, method, category, amount;
    Button saveButton;
    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_expense, container,false);

    initViews(view);
    setupSpinner1();
    setupSpinner2();

    databaseHelper = new DatabaseHelper(getActivity());

saveButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        updateData();
    }
});
    return view;

    }

    private void setupSpinner1() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Select Category");
        categories.add("Food and Groceries");
        categories.add("Housing");
        categories.add("Transportation");
        categories.add("Utilities");
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    displayCategory.setText("Category: " + categories.get(position));
                } else {
                    displayCategory.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                displayCategory.setText("");
            }
        });

    }
    private void setupSpinner2() {
        ArrayList<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("Select Payment Method");
        paymentMethods.add("Cash");
        paymentMethods.add("Credit Card");
        paymentMethods.add("Debit Card");
        paymentMethods.add("Bank Transfer");
        paymentMethods.add("Mobile Payment");
        paymentMethods.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMethod.setAdapter(adapter);

        spinMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    displayMethod.setText("Method: " + paymentMethods.get(position));
                } else {
                    displayMethod.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                displayMethod.setText("");
            }
        });
    }
    private void initViews(View view) {
        spinCategory = view.findViewById(R.id.selectCategory);
        spinMethod = view.findViewById(R.id.spinMethod);
        inputAmount = view.findViewById(R.id.expenseAmount);
        displayCategory = view.findViewById(R.id.displayCategory);
        displayMethod = view.findViewById(R.id.txtDisplayMeth);
        inputTitle = view.findViewById(R.id.expenseTitle);
        saveButton = view.findViewById(R.id.btnSubmit);
    }
    private void openMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    public void updateData() {
        title = inputTitle.getText().toString().trim();
        method = spinMethod.getSelectedItem().toString();
        category = spinCategory.getSelectedItem().toString();
        amount = inputAmount.getText().toString().trim();

        // Validation
        if (title.isEmpty() || method.equals("Select Payment Method") || category.equals("Select Category") || amount.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        DataClass dataClass = new DataClass(title, amount, category, method);

        // Assume that the id of the expense to update is passed as an argument to the fragment
        int expenseId = getArguments().getInt("expense_id", -1);
        if (expenseId != -1) {
            Log.d(TAG, "Updating expense with ID: " + expenseId);
            boolean isUpdated = databaseHelper.updateExpense(dataClass, expenseId);
            if (isUpdated) {
                showToast("Data Updated");
                openMain();
            } else {
                showToast("Update Failed");
            }
        } else {
            showToast("Invalid expense ID");
        }

    }
}