package com.example.tracker.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FullscreenDialogFragment extends DialogFragment {

    public interface FullscreenDialogListener {
        // Define methods to handle dialog events here
        void onDialogPositiveClick(DialogFragment dialog);
    }

    private FullscreenDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FullscreenDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FullscreenDialogListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dismiss());

        Button btnSubmit = view.findViewById(R.id.btnComplete);
        btnSubmit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogPositiveClick(FullscreenDialogFragment.this);
            }
            dismiss();
        });

        // Update content when the view is created
        updateContent(view);

        return view;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Optional: make dialog background transparent
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            Window window = getDialog().getWindow();
            if (window != null) {
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setWindowAnimations(R.style.DialogAnimation); // Optional, for adding animations
            }
        }
    }

    public void updateContent(View view) {
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<DataClass> dataList = args.getParcelableArrayList("dataList");
            int position = args.getInt("position");

            if (dataList != null && position >= 0 && position < dataList.size()) {
                DataClass data = dataList.get(position);

                TextInputEditText transactionNameEditText = view.findViewById(R.id.editTransactionName);
                TextInputEditText amountEditText = view.findViewById(R.id.editTextAmount);
                Spinner categorySpinner = view.findViewById(R.id.spinnerCategory);
                Spinner paymentMethodSpinner = view.findViewById(R.id.spinMethod);

                // Set values to views
                transactionNameEditText.setText(data.getTitle());
                amountEditText.setText(data.getAmount());

                // Load spinner data based on type
                if ("Income".equals(data.getType())) {
                    loadSpinnerData(categorySpinner, R.array.income_categories);
                } else if ("Expense".equals(data.getType())) {
                    loadSpinnerData(categorySpinner, R.array.expense_categories);
                }
                loadSpinnerData(paymentMethodSpinner, R.array.payment_methods);

                // Set selected spinner values
                setSpinnerToValue(categorySpinner, data.getCategory());
                setSpinnerToValue(paymentMethodSpinner, data.getPaymentMethod());

            } else {
                dismiss(); // Dismiss the dialog if data is invalid
            }
        } else {
            dismiss(); // Dismiss the dialog if arguments are null
        }
    }



    private void loadSpinnerData(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
