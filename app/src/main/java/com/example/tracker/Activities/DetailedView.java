package com.example.tracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tracker.Fragments.ExpenseFragment;
import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DetailedView extends AppCompatActivity {

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform your desired action here, such as navigating back
                finish();
            }
        });
        Intent intent = getIntent();
        ArrayList<DataClass> dataList = intent.getParcelableArrayListExtra("dataList");


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ExpenseFragment expenseFragment = new ExpenseFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("dataList", dataList);
        expenseFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.framepager, expenseFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.grey_font));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }
}

