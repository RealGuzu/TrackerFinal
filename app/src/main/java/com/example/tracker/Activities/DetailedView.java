package com.example.tracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tracker.Fragments.ExpenseFragment;
import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DetailedView extends AppCompatActivity {

    TabLayout tabLayout;

    String key;
    DatabaseReference databaseReference;
    Button btnBack;

    private ExpenseFragment expenseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Intent intent = getIntent();
        ArrayList<DataClass> dataList = intent.getParcelableArrayListExtra("dataList");

        tabLayout = findViewById(R.id.tab_layout);
        btnBack = findViewById(R.id.btnBack);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ExpenseFragment expenseFragment1 = new ExpenseFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("dataList", dataList);
        expenseFragment1.setArguments(bundle);
        fragmentTransaction.replace(R.id.framepager, expenseFragment1);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Back button listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });

        // TabLayout listener for selecting tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed here
            }
        });


    }

    private void openMain() {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}
