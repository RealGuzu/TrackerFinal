package com.example.tracker.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tracker.R;
import com.example.tracker.Utilities.MyViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

public class DetailedView extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPager myViewPager;
    String key;
    DatabaseReference databaseReference;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        btnBack = findViewById(R.id.btnBack);

        // Ensure MyViewPager is correctly implemented and takes this context
        myViewPager = new MyViewPager(this);
        viewPager2.setAdapter(myViewPager);

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
                viewPager2.setCurrentItem(tab.getPosition());
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
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }
        });
    }
}
