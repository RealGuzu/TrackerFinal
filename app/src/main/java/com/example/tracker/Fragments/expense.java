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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class expense extends Fragment {

    private static final String TAG = "ExpenseFragment";

    private Spinner spinCategory, spinMethod;
    private EditText inputAmount, inputTitle;
    private TextView displayCategory, displayMethod;
    private Button saveButton;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        initViews(view);
        setupSpinner(spinCategory, getCategories(), displayCategory, "Category: ");
        setupSpinner(spinMethod, getPaymentMethods(), displayMethod, "Method: ");

        databaseHelper = new DatabaseHelper(getActivity());

        saveButton.setOnClickListener(v -> updateData());

        return view;
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

    private ArrayList<String> getCategories() {
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
        return categories;
    }

    private ArrayList<String> getPaymentMethods() {
        ArrayList<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("Select Payment Method");
        paymentMethods.add("Cash");
        paymentMethods.add("Credit Card");
        paymentMethods.add("Debit Card");
        paymentMethods.add("Bank Transfer");
        paymentMethods.add("Mobile Payment");
        paymentMethods.add("Other");
        return paymentMethods;
    }

    private void setupSpinner(Spinner spinner, ArrayList<String> items, TextView displayText, String displayPrefix) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayText.setText(position > 0 ? displayPrefix + items.get(position) : "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                displayText.setText("");
            }
        });
    }

    private void openMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateData() {
        String title = inputTitle.getText().toString().trim();
        String method = spinMethod.getSelectedItem().toString();
        String category = spinCategory.getSelectedItem().toString();
        String amount = inputAmount.getText().toString().trim();

        if (title.isEmpty() || method.equals("Select Payment Method") || category.equals("Select Category") || amount.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        DataClass dataClass = new DataClass(title, amount, category, method);
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
