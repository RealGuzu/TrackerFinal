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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.google.android.material.appbar.MaterialToolbar;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        // Find toolbar and set close button click listener
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dismiss());

        // Find button and set click listener
        Button btnSubmit = view.findViewById(R.id.btnComplete);
        btnSubmit.setOnClickListener(v -> {
            // Handle the submit button click here
            // For example, if you want to dismiss the dialog when the submit button is clicked:
            if (listener != null) {
                listener.onDialogPositiveClick(FullscreenDialogFragment.this);
            }
            dismiss();
        });

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
    public void updateContent() {
        // Retrieve arguments
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<DataClass> dataList = args.getParcelableArrayList("dataList");
            int position = args.getInt("position");

            // Update dialog content based on dataList and position
            // For example:
            // if (dataList != null && position >= 0 && position < dataList.size()) {
            //     DataClass data = dataList.get(position);
            //     // Update UI elements with data
            // }
        }
    }
}
