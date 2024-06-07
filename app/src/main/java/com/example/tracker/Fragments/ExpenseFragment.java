package com.example.tracker.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.example.tracker.Utilities.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {

    // Declare any views you need to interact with here
    private EditText expenseTitle;
    private EditText expenseAmount;
    private Spinner selectCategory;
    private Spinner spinMethod;
    private Button btnSubmit;
    private Spinner categorySpinner;

    private Spinner paymentMethodSpinner;

    private Class Model(){

        return null;
    }
    private List<DataClass> dataClassArrayList;
    private DatabaseHelper databaseHelper;

    public ExpenseFragment(List<DataClass> dataClassArrayList) {
        this.dataClassArrayList = dataClassArrayList;
    }

    public ExpenseFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the XML layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        // Initialize views
        expenseTitle = view.findViewById(R.id.expenseTitle);
        expenseAmount = view.findViewById(R.id.expenseAmount);
        selectCategory = view.findViewById(R.id.selectCategorys);
       // categorySpinner = view.findViewById(R.id.spinnerCategorys);
        paymentMethodSpinner = view.findViewById(R.id.spinMethod);
        spinMethod = view.findViewById(R.id.spinMethod);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        databaseHelper = new DatabaseHelper(getContext());
        setupCategorySpinner();
       setupPaymentMethodSpinner();

            ArrayList<DataClass> dataList = getArguments().getParcelableArrayList("dataList");
        DataClass dataClass = new DataClass(dataList.get(0).getTitle(),dataList.get(0).getAmount(),dataList.get(0).getCategory(),dataList.get(0).getPaymentMethod());
        expenseTitle.setText(dataList.get(0).getTitle());
        expenseAmount.setText(dataList.get(0).getAmount());
        btnSubmit.setText("Update");

        // Set click listener for the button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.updateExpense(dataClass,1);
                // Handle button click event
                // For example, you can retrieve values from EditText and Spinner fields here
            }
        });

        // Return the inflated view
        return view;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        selectCategory.setAdapter(adapter);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        paymentMethodSpinner.setAdapter(adapter);
    }
}
