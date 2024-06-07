package com.example.tracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tracker.Fragments.ExpenseFragment;
import com.example.tracker.R;
import com.example.tracker.Utilities.DataClass;
import com.example.tracker.Utilities.MyViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DetailedView extends AppCompatActivity {

    TabLayout tabLayout;
//    ViewPager2 viewPager2;
//    MyViewPager myViewPager;
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
//        viewPager2 = findViewById(R.id.view_pager);
        btnBack = findViewById(R.id.btnBack);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ExpenseFragment expenseFragment1 = new ExpenseFragment();

        // Add, replace, or remove the fragment
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("dataList", dataList);
        expenseFragment1.setArguments(bundle);
        fragmentTransaction.replace(R.id.framepager, expenseFragment1);
        fragmentTransaction.addToBackStack(null);

// Commit the transaction
        fragmentTransaction.commit();

        // Ensure MyViewPager is correctly implemented and takes this context
//        myViewPager = new MyViewPager(this);
//        viewPager2.setAdapter(myViewPager);

        // Adjust the window to handle soft input
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Back button listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        // ViewPager2 page change callback
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                TabLayout.Tab tab = tabLayout.getTabAt(position);
//                if (tab != null) {
//                    tab.select();
//                }
//            }
//        });
    }
}
